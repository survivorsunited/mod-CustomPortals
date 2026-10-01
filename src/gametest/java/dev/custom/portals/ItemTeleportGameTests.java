package dev.custom.portals;

import dev.custom.portals.blocks.PortalBlock;
import dev.custom.portals.data.CustomPortal;
import dev.custom.portals.registry.CPBlocks;
import dev.custom.portals.util.PortalHelper;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.UUID;

public class ItemTeleportGameTests {
    private record Portals(BlockPos source, BlockPos destination) {}

    private Portals buildPortals(TestContext context) {
        ServerWorld world = context.getWorld();
        UUID creator = UUID.randomUUID();
        for (int x : new int[] {2, 6}) {
            for (int y = 1; y <= 5; y++) {
                for (int z = 1; z <= 4; z++) {
                    if (y == 1 || y == 5 || z == 1 || z == 4)
                        context.setBlockState(x, y, z, Blocks.STONE);
                }
            }
            BlockPos interior = context.getAbsolutePos(new BlockPos(x, 2, 2));
            context.assertTrue(PortalHelper.buildPortal(interior, CPBlocks.PURPLE_PORTAL, creator, world),
                    "Portal frame must activate");
        }
        BlockPos source = context.getAbsolutePos(new BlockPos(2, 2, 2));
        CustomPortal portal = CustomPortals.PORTALS.get(world).getPortalFromPos(source);
        context.assertTrue(portal != null && portal.hasLinked(), "Portal pair must link");
        // Activate explicitly so the test measures item transfer rather than block ticker ordering.
        for (CustomPortal p : List.of(portal, portal.getLinked())) {
            for (BlockPos pos : p.getPortalBlocks())
                world.setBlockState(pos, world.getBlockState(pos).with(PortalBlock.LIT, true), Block.NOTIFY_LISTENERS);
        }
        return new Portals(source, portal.getLinked().getSpawnPos());
    }

    @GameTest(maxTicks = 40)
    public void stationaryItemTeleportsOnFirstTick(TestContext context) {
        Portals portals = buildPortals(context);
        ServerWorld world = context.getWorld();
        ItemEntity item = new ItemEntity(world, portals.source().getX() + .5,
                portals.source().getY() + .1, portals.source().getZ() + .5,
                new ItemStack(Items.DIAMOND, 12));
        item.setVelocity(Vec3d.ZERO);
        world.spawnEntity(item);
        context.runAtTick(2, () -> {
            context.assertTrue(Math.abs(item.getX() - portals.destination().getX() - .5) < .01,
                    "Stationary item must reach destination; actual x=" + item.getX());
            context.assertTrue(item.getStack().getCount() == 12, "Item count must survive teleport");
            context.assertTrue(item.hasPortalCooldown(), "Arrival must establish a cooldown");
        });
        context.runAtTick(10, () -> {
            context.assertTrue(Math.abs(item.getX() - portals.destination().getX() - .5) < .01, "Item must not bounce back");
            context.complete();
        });
    }

    @GameTest(maxTicks = 60)
    public void realDropperOutputTeleports(TestContext context) {
        Portals portals = buildPortals(context);
        BlockPos dropper = new BlockPos(1, 2, 2);
        context.setBlockState(dropper, Blocks.DROPPER.getDefaultState().with(DispenserBlock.FACING, Direction.EAST));
        DispenserBlockEntity inventory = context.getBlockEntity(dropper, DispenserBlockEntity.class);
        inventory.setStack(0, new ItemStack(Items.EMERALD, 1));
        // Run the same scheduled dispense used by redstone, without powering the portal off.
        context.getWorld().scheduleBlockTick(context.getAbsolutePos(dropper), Blocks.DROPPER, 1);
        context.runAtTick(12, () -> {
            ServerWorld world = context.getWorld();
            List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class,
                    new Box(portals.destination()).expand(1), e -> e.getStack().isOf(Items.EMERALD));
            context.assertTrue(inventory.getStack(0).isEmpty(), "Dropper must dispense its item");
            context.assertTrue(items.size() == 1 && items.get(0).getStack().getCount() == 1,
                    "Dropper item must arrive once at linked portal");
            context.assertTrue(items.get(0).hasPortalCooldown(), "Dropper output must receive arrival cooldown");
            context.complete();
        });
    }
}
