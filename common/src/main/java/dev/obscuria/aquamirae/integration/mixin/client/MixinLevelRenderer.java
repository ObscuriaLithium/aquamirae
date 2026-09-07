package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.*;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.client.BlizzardContext;
import dev.obscuria.aquamirae.client.BlizzardVortexRenderer;
import dev.obscuria.aquamirae.client.event.ShipbreakerClientState;
import dev.obscuria.aquamirae.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {

    @Shadow @Nullable private ClientLevel level;
    @Shadow @Final private RenderBuffers renderBuffers;
    @Unique private int blizzard$vertexIndex = 0;

    @Inject(require = 0, method = "renderSnowAndRain", at = @At("HEAD"))
    private void renderSnowAndRain$onHead(
            LightTexture lightTexture, float partialTick,
            double camX, double camY, double camZ,
            CallbackInfo ci
    ) {
        if (!ClientConfig.BLIZZARD_CUSTOM_PARTICLES.get()) return;
        blizzard$vertexIndex = 0;
        float roughDelta = Math.max(partialTick / 20f, 0.001f);
        BlizzardContext.update(camX, camY, camZ, roughDelta);
    }

    @ModifyVariable(require = 0, method = "renderSnowAndRain", at = @At("STORE"), ordinal = 3)
    private float renderSnowAndRain$onRender(float speed) {
        if (!ClientConfig.BLIZZARD_CUSTOM_PARTICLES.get()) return speed;
        if (!BlizzardContext.active) return speed;
        return speed * BlizzardContext.speedFactor;
    }

    @Redirect(require = 0, method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;addVertex(FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer renderSnowAndRain$modifyVertex(BufferBuilder instance, float x, float y, float z) {
        if (!ClientConfig.BLIZZARD_CUSTOM_PARTICLES.get()) return instance.addVertex(x, y, z);
        if (BlizzardContext.active) {
            int idx = blizzard$vertexIndex % 4;
            if (idx == 0 || idx == 1) {
                x += BlizzardContext.windX;
                z += BlizzardContext.windZ;
            }
        }
        blizzard$vertexIndex++;
        return instance.addVertex(x, y, z);
    }

    @Inject(require = 0, method = "renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V", at = @At("TAIL"))
    private void renderSnowAndRain$onTail(
            LightTexture lightTexture, float partialTick,
            double camX, double camY, double camZ, CallbackInfo ci
    ) {
        if (level == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameRenderer == null) return;

        Camera camera = mc.gameRenderer.getMainCamera();
        if (camera == null) return;

        BlizzardVortexRenderer.INSTANCE.render(partialTick, camera, level);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderBuffers;bufferSource()Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;"))
    private void renderLevel$onBufferSource(
            DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
            LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci,
            @Local PoseStack poseStack,
            @Local Frustum frustum
    ) {
        if (level == null) return;
        ShipbreakerClientState.render(
                poseStack, level, frustum, camera,
                renderBuffers.bufferSource(),
                AquamiraeClient.partialTicks());
    }
}