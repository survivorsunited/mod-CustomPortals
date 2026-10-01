package dev.custom.portals.mixin;

import dev.custom.portals.blocks.PortalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void customPortals$teleportOverlappingItem(CallbackInfo ci) {
        ItemEntity item = (ItemEntity) (Object) this;
        if (!(item.getEntityWorld() instanceof ServerWorld world) || !item.isAlive()
                || item.getStack().isEmpty())
            return;

        // Include stationary items and items spawned directly inside a portal by automation.
        Box box = item.getBoundingBox().contract(1.0E-7);
        for (BlockPos pos : BlockPos.iterate(
                MathHelper.floor(box.minX), MathHelper.floor(box.minY), MathHelper.floor(box.minZ),
                MathHelper.floor(box.maxX), MathHelper.floor(box.maxY), MathHelper.floor(box.maxZ))) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof PortalBlock portal && portal.teleportItem(world, pos, item)) {
                // Cross-dimension teleporting replaces the entity; do not tick the old instance.
                ci.cancel();
                return;
            }
        }
    }
}
