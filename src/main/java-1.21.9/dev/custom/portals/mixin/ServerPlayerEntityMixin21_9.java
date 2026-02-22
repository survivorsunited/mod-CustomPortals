package dev.custom.portals.mixin;

import com.mojang.authlib.GameProfile;
import dev.custom.portals.util.EntityMixinAccess;
import dev.custom.portals.mixin.TeleportTargetAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin21_9 extends PlayerEntity {

    @Shadow
    private boolean inTeleportationState;
    @Shadow
    public ServerPlayNetworkHandler networkHandler;
    @Final
    @Shadow
    public MinecraftServer server;
    @Shadow
    private float syncedHealth;
    @Shadow
    private int syncedExperience;
    @Shadow
    private int syncedFoodLevel;

    @Shadow
    public abstract ServerWorld getWorld();
    @Shadow
    protected abstract void worldChanged(ServerWorld serverWorld);
    @Shadow
    public abstract void setServerWorld(ServerWorld world);
    @Shadow
    public abstract CommonPlayerSpawnInfo createCommonPlayerSpawnInfo(ServerWorld serverWorld);

    public ServerPlayerEntityMixin21_9(MinecraftServer minecraftServer, ServerWorld serverWorld, GameProfile gameProfile) {
        super(serverWorld, gameProfile);
    }

    @Inject(method = "teleportTo", at = @At("HEAD"), cancellable = true)
    public void customPortals$teleportTo(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir) {
        if (this.isRemoved())
            cir.setReturnValue(null);
        ServerWorld serverWorld = teleportTarget.world();
        ServerWorld serverWorld2 = this.getWorld();
        if (((EntityMixinAccess)this).isInCustomPortal()) {
            ServerPlayerEntity thisPlayer = (ServerPlayerEntity)(Object)this;
            this.inTeleportationState = true;
            WorldProperties worldProperties = serverWorld.getLevelProperties();
            this.networkHandler.sendPacket(new PlayerRespawnS2CPacket(this.createCommonPlayerSpawnInfo(serverWorld), (byte)3));
            this.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
            PlayerManager playerManager = this.server.getPlayerManager();
            playerManager.sendCommandTree(thisPlayer);
            serverWorld2.removePlayer(thisPlayer, RemovalReason.CHANGED_DIMENSION);
            this.unsetRemoved();
            Profiler profiler = Profilers.get();
            profiler.push("moving");
            profiler.pop();
            profiler.push("placing");
            this.setServerWorld(serverWorld);
            this.networkHandler.requestTeleport(EntityPosition.fromTeleportTarget(teleportTarget), ((TeleportTargetAccessor) (Object) teleportTarget).customPortals$getRelatives());
            this.networkHandler.syncWithPlayerPosition();
            serverWorld.onDimensionChanged(thisPlayer);
            profiler.pop();
            this.worldChanged(serverWorld2);
            this.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(this.getAbilities()));
            playerManager.sendWorldInfo(thisPlayer, serverWorld);
            playerManager.sendPlayerStatus(thisPlayer);
            playerManager.sendStatusEffects(thisPlayer);
            this.networkHandler.sendPacket(new WorldEventS2CPacket(1032, BlockPos.ORIGIN, 0, false));
            this.syncedExperience = -1;
            this.syncedHealth = -1.0F;
            this.syncedFoodLevel = -1;
            cir.setReturnValue(thisPlayer);
        }
    }
}
