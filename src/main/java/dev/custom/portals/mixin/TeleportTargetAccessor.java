package dev.custom.portals.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.world.TeleportTarget;

@Mixin(TeleportTarget.class)
public interface TeleportTargetAccessor {

	@Accessor("relatives")
	Set<PositionFlag> customPortals$getRelatives();
}
