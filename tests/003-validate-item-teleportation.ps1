# Logic regression checks using Minecraft doubles. In-game Mixin verification is separate.
param([string]$JavaHome = $env:JAVA_HOME)
$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
if (-not $JavaHome) { throw 'Set JAVA_HOME or pass -JavaHome for a JDK 21 or newer.' }
$javac = Join-Path $JavaHome $(if ($IsWindows) { 'bin/javac.exe' } else { 'bin/javac' })
$java = Join-Path $JavaHome $(if ($IsWindows) { 'bin/java.exe' } else { 'bin/java' })
$testDir = Join-Path ([IO.Path]::GetTempPath()) ('customportals-item-test-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $testDir | Out-Null
function Get-MethodBody([string]$source, [string]$signature) {
    $start = $source.IndexOf($signature)
    if ($start -lt 0) { throw "Missing method: $signature" }
    $open = $source.IndexOf('{', $start)
    $depth = 1
    for ($end = $open + 1; $depth -gt 0 -and $end -lt $source.Length; $end++) {
        if ($source[$end] -eq '{') { $depth++ }
        if ($source[$end] -eq '}') { $depth-- }
    }
    if ($depth -ne 0) { throw "Unbalanced method: $signature" }
    return $source.Substring($start, $end - $start)
}
$portalSource = Get-Content (Join-Path $root 'src/main/java/dev/custom/portals/blocks/PortalBlock.java') -Raw
$transfer = Get-MethodBody $portalSource 'public boolean teleportItem('
$mixin = Get-Content (Join-Path $root 'src/main/java/dev/custom/portals/mixin/ItemEntityMixin.java') -Raw
$mixin = $mixin -replace '(?m)^(package |import ).*\r?\n', '' -replace '(?m)^\s*@(Mixin|Inject)\(.*\)\r?\n', ''
# Model the Mixin transformation: the injected method runs on an ItemEntity instance.
$mixin = $mixin.Replace('public abstract class ItemEntityMixin {', 'abstract class ItemEntityMixin extends ItemEntity {')
$fixture = @'
import java.util.*;
import java.lang.reflect.*;
class World {}
class Server { boolean enterable=true; boolean isEnterableWithPortal(ServerWorld world){return enterable;} }
class ServerWorld extends World {
    Server server=new Server(); Map<BlockPos,BlockState> blocks=new HashMap<>();
    Server getServer(){return server;}
    BlockState getBlockState(BlockPos p){return blocks.getOrDefault(p,new BlockState(null,false));}
}
class BlockState {
    Object block; boolean lit;
    BlockState(Object block,boolean lit){this.block=block;this.lit=lit;}
    boolean isOf(Object block){return this.block==block;}
    boolean get(Object property){return lit;}
    Object getBlock(){return block;}
}
record BlockPos(int x,int y,int z) {
    static Iterable<BlockPos> iterate(int x0,int y0,int z0,int x1,int y1,int z1){
        List<BlockPos> positions=new ArrayList<>();
        for(int x=x0;x<=x1;x++) for(int y=y0;y<=y1;y++) for(int z=z0;z<=z1;z++) positions.add(new BlockPos(x,y,z));
        return positions;
    }
}
class Box {
    double minX,minY,minZ,maxX,maxY,maxZ;
    Box(double a,double b,double c,double d,double e,double f){minX=a;minY=b;minZ=c;maxX=d;maxY=e;maxZ=f;}
    Box contract(double e){return new Box(minX+e,minY+e,minZ+e,maxX-e,maxY-e,maxZ-e);}
}
class MathHelper { static int floor(double x){return (int)Math.floor(x);} }
class Stack { int count=64; boolean isEmpty(){return count==0;} }
class Entity { int cooldown; void resetPortalCooldown(){cooldown=300;} }
class ItemEntity extends Entity {
    World world; Box box=new Box(.4,64,.4,.65,64.25,.65); Stack stack=new Stack();
    boolean alive=true,canChangeDimension=true,failTransfer=false; int transfers; Entity result=this; String origin;
    World getEntityWorld(){return world;} boolean isAlive(){return alive;}
    Stack getStack(){return stack;} Box getBoundingBox(){return box;}
    boolean hasPortalCooldown(){return cooldown>0;}
    boolean canTeleportBetween(World a,World b){return canChangeDimension;}
    Entity teleportTo(TeleportTarget target){transfers++;if(failTransfer)return null;world=target.world();return result;}
}
record TeleportTarget(ServerWorld world) {}
class CallbackInfo { boolean cancelled; void cancel(){cancelled=true;} }
class PortalBlock {
    static final Object LIT=new Object(); TeleportTarget target;
    TeleportTarget createTeleportTarget(ServerWorld world,Entity item,BlockPos pos){return target;}
'@
$tests = @'
class TestItem extends ItemEntityMixin {}
public class ItemTeleportRegression {
    static int checks;
    static void check(boolean condition,String name){if(!condition)throw new AssertionError(name);checks++;}
    static CallbackInfo tick(TestItem item) throws Exception {
        CallbackInfo ci=new CallbackInfo();
        Method m=ItemEntityMixin.class.getDeclaredMethod("customPortals$teleportOverlappingItem",CallbackInfo.class);
        m.setAccessible(true);m.invoke(item,ci);return ci;
    }
    public static void main(String[] args) throws Exception {
        ServerWorld source=new ServerWorld(),destination=new ServerWorld();
        PortalBlock portal=new PortalBlock();BlockPos pos=new BlockPos(0,64,0);
        source.blocks.put(pos,new BlockState(portal,true));portal.target=new TeleportTarget(source);
        for(String origin:List.of("player","dropper","dispenser","block","mob")) {
            TestItem item=new TestItem();item.origin=origin;item.world=source;
            CallbackInfo ci=tick(item);
            check(item.transfers==1 && ci.cancelled,origin+" transfers on first overlapping tick");
            check(item.stack.count==64 && item.cooldown==300,origin+" preserves count and sets cooldown");
            ci=tick(item);check(item.transfers==1 && !ci.cancelled,origin+" does not bounce back");
        }
        TestItem edge=new TestItem();edge.world=source;edge.box=new Box(-.1,64,.4,.15,64.25,.65);
        check(tick(edge).cancelled && edge.transfers==1,"overlapping portal at block boundary");
        TestItem cross=new TestItem();cross.world=source;cross.result=new ItemEntity();portal.target=new TeleportTarget(destination);
        check(tick(cross).cancelled && cross.world==destination && cross.result.cooldown==300,"cross-dimension replacement is protected and old tick cancelled");
        TestItem inactive=new TestItem();inactive.world=source;source.blocks.put(pos,new BlockState(portal,false));
        check(!tick(inactive).cancelled && inactive.transfers==0,"inactive portal does not transfer");
        source.blocks.put(pos,new BlockState(portal,true));portal.target=null;
        check(!tick(inactive).cancelled && inactive.cooldown==0,"unlinked portal does not impose cooldown");
        portal.target=new TeleportTarget(destination);source.server.enterable=false;
        check(!tick(inactive).cancelled && inactive.transfers==0,"unavailable destination does not transfer");
        source.server.enterable=true;inactive.canChangeDimension=false;
        check(!tick(inactive).cancelled && inactive.transfers==0,"dimension restrictions remain enforced");
        TestItem empty=new TestItem();empty.world=source;empty.stack.count=0;
        check(!tick(empty).cancelled && empty.transfers==0,"empty stack does not transfer");
        TestItem dead=new TestItem();dead.world=source;dead.alive=false;
        check(!tick(dead).cancelled && dead.transfers==0,"removed item does not transfer");
        TestItem client=new TestItem();client.world=new World();
        check(!tick(client).cancelled && client.transfers==0,"client does not transfer");
        TestItem outside=new TestItem();outside.world=source;outside.box=new Box(1,64,.4,1.25,64.25,.65);
        check(!tick(outside).cancelled && outside.transfers==0,"no transfer without overlap");
        TestItem failure=new TestItem();failure.world=source;failure.failTransfer=true;
        check(!tick(failure).cancelled,"failed teleport does not cancel item tick");
        System.out.println("PASS: "+checks+" item teleportation regression checks (Minecraft doubles).");
    }
}
'@
$code = $fixture + "`n" + $transfer + "`n}`n" + $mixin + "`n" + $tests
$javaFile = Join-Path $testDir 'ItemTeleportRegression.java'
[IO.File]::WriteAllText($javaFile,$code,[Text.UTF8Encoding]::new($false))
& $javac -d $testDir $javaFile
if ($LASTEXITCODE -ne 0) { throw "Harness compilation failed. Sources retained in $testDir" }
& $java -cp $testDir ItemTeleportRegression
if ($LASTEXITCODE -ne 0) { throw "Regression checks failed. Sources retained in $testDir" }
