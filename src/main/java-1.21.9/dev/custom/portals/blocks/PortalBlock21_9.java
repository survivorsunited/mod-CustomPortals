package dev.custom.portals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * PortalBlock for 1.21.9+ which adds the boolean parameter to onEntityCollision.
 */
public class PortalBlock21_9 extends PortalBlock {

	public PortalBlock21_9(Settings settings) {
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl) {
		doOnEntityCollision(state, world, pos, entity);
	}
}
