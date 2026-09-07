package dev.obscuria.aquamirae.integration.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.integration.hook.client.HookGameRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(require = 0, method = "bobView", at = @At("HEAD"))
    private void bobView$onHead(PoseStack poseStack, float partialTicks, CallbackInfo ci) {
        HookGameRenderer.bobView(poseStack, partialTicks);
    }
}