package dev.custom.portals.mixin;

import dev.custom.portals.blocks.PortalBlock;
import dev.custom.portals.util.ClientUtil;
import dev.custom.portals.util.ScreenTransitionPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {

    @Unique
    private boolean customPortalsPacketSent = false;

    public LevelLoadingScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    public void customPortals$renderBackground(DrawContext drawContext, int i, int j, float f, CallbackInfo ci) {
        if (ClientUtil.transitionBackgroundSpriteModel != null) {
            ClientUtil.isTransitioning = true;
            if (!customPortalsPacketSent) {
                ClientPlayNetworking.send(new ScreenTransitionPayload(true));
                customPortalsPacketSent = true;
            }
            drawContext.drawSpriteStretched(RenderPipelines.GUI_OPAQUE_TEX_BG, this.client.getBlockRenderManager().getModels().getModelParticleSprite(ClientUtil.transitionBackgroundSpriteModel.getDefaultState().with(PortalBlock.LIT, true)), 0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight());
            ci.cancel();
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void customPortals$close(CallbackInfo ci) {
        ClientUtil.isTransitioning = false;
        ClientPlayNetworking.send(new ScreenTransitionPayload(false));
        ClientUtil.transitionBackgroundSpriteModel = null;
        customPortalsPacketSent = false;
    }
}
