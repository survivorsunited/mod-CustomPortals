package dev.custom.portals.mixin;

import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {
    @Inject(method = "canUsePortals", at = @At("HEAD"), cancellable = true)
    private void allowFallingBlocksToUsePortals(boolean allowVehicles, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}


