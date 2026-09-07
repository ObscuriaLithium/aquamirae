package dev.obscuria.aquamirae.integration.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.api.common.entity.MultipartEntity;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher {

    @Inject(require = 0, method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLineBox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/AABB;FFFF)V", ordinal = 0))
    private static void renderHitbox$onRenderLineBox(
            PoseStack poseStack, VertexConsumer buffer, Entity entity,
            float red, float green, float blue, float alpha, CallbackInfo ci
    ) {
        var partialTicks = AquamiraeClient.partialTicks();
        if (entity instanceof MultipartEntity multipartEntity) {
            double d0 = -Mth.lerp(partialTicks, entity.xOld, entity.getX());
            double d1 = -Mth.lerp(partialTicks, entity.yOld, entity.getY());
            double d2 = -Mth.lerp(partialTicks, entity.zOld, entity.getZ());

            for (var subPart : multipartEntity.getSubParts()) {
                poseStack.pushPose();
                double d3 = d0 + Mth.lerp(partialTicks, subPart.xOld, subPart.getX());
                double d4 = d1 + Mth.lerp(partialTicks, subPart.yOld, subPart.getY());
                double d5 = d2 + Mth.lerp(partialTicks, subPart.zOld, subPart.getZ());
                poseStack.translate(d3, d4, d5);
                LevelRenderer.renderLineBox(poseStack, buffer,
                        subPart.getBoundingBox().move(-subPart.getX(), -subPart.getY(), -subPart.getZ()),
                        0.0F, 0.5F, 1.0F, 1.0F);
                poseStack.popPose();
            }
        }
    }
}
