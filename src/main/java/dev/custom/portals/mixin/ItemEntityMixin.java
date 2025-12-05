package dev.custom.portals.mixin;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "canUsePortals", at = @At("HEAD"), cancellable = true)
    private void allowItemEntitiesToUsePortals(boolean allowVehicles, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}


