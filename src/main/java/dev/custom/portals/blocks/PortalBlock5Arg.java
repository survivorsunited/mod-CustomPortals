package dev.custom.portals.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * PortalBlock for 1.21.8 and earlier: overrides the 5-arg onEntityCollision.
 * Excluded when building for 1.21.9+ (parent has 6-arg there).
 */
public class PortalBlock5Arg extends PortalBlock {

	public PortalBlock5Arg(Settings settings) {
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler entityCollisionHandler) {
		doOnEntityCollision(state, world, pos, entity);
	}
}
