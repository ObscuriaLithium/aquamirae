package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.FogShape;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.client.FogEvents;
import dev.obscuria.aquamirae.client.FogSmoother;
import dev.obscuria.aquamirae.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public abstract class MixinFogRenderer {

    @Shadow private static float fogRed;
    @Shadow private static float fogGreen;
    @Shadow private static float fogBlue;

    @Inject(require = 0, method = "setupColor", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", shift = At.Shift.BEFORE))
    private static void fogHook$beforeFinalClearColor(
            Camera camera,
            float partialTicks,
            ClientLevel level,
            int renderDistanceChunks,
            float bossColorModifier,
            CallbackInfo ci
    ) {
        @Nullable var callback = FogEvents.SET_COLOR;

        float vanillaR = fogRed;
        float vanillaG = fogGreen;
        float vanillaB = fogBlue;

        @Nullable FogEvents.FogColorResult override = null;
        if (callback != null) {
            override = callback.override(camera, level, camera.getFluidInCamera(),
                    partialTicks, renderDistanceChunks,
                    vanillaR, vanillaG, vanillaB);
        }

        var smoothed = FogSmoother.INSTANCE.smoothColor(
                vanillaR, vanillaG, vanillaB, override,
                aquamirae$getDeltaTime());

        fogRed = smoothed.r;
        fogGreen = smoothed.g;
        fogBlue = smoothed.b;
    }

    @Inject(require = 0, method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE))
    private static void fogHook$beforeSetShaderFog(
            Camera camera,
            FogRenderer.FogMode fogMode,
            float farPlaneDistance,
            boolean isFoggy,
            float partialTicks,
            CallbackInfo ci,
            @Local FogRenderer.FogData fogData
    ) {
        if (!ClientConfig.BLIZZARD_CUSTOM_FOG.get()) return;
        FogEvents.FogDistanceCallback cb = FogEvents.SET_DISTANCE;

        float vanillaStart = fogData.start;
        float vanillaEnd = fogData.end;
        FogShape vanillaShape = fogData.shape;

        FogEvents.FogDistanceResult override = null;
        if (cb != null) {
            override = cb.override(
                    camera,
                    camera.getFluidInCamera(),
                    aquamirae$toCommonFogMode(fogMode),
                    farPlaneDistance,
                    partialTicks,
                    vanillaStart, vanillaEnd, vanillaShape
            );
        }

        float dt = aquamirae$getDeltaTime();
        FogSmoother.DistResult smoothed = FogSmoother.INSTANCE.smoothDistance(
                vanillaStart, vanillaEnd, vanillaShape,
                override,
                dt
        );

        fogData.start = smoothed.start;
        fogData.end = smoothed.end;
        fogData.shape = smoothed.shape;
    }

    @Unique
    private static float aquamirae$getDeltaTime() {
        return AquamiraeClient.partialTicks() / 20.0f;
    }

    @Unique
    private static FogEvents.FogMode aquamirae$toCommonFogMode(FogRenderer.FogMode mode) {
        return switch (mode) {
            case FOG_SKY -> FogEvents.FogMode.FOG_SKY;
            case FOG_TERRAIN -> FogEvents.FogMode.FOG_TERRAIN;
        };
    }
}