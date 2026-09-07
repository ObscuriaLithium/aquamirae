package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.ModelShellback;
import dev.obscuria.aquamirae.client.renderer.entity.layer.HangingChestLayer;
import dev.obscuria.aquamirae.client.renderer.entity.layer.ShellbackSaddleLayer;
import dev.obscuria.aquamirae.common.entity.Shellback;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShellbackRenderer extends GeoEntityRenderer<Shellback> {

    public ShellbackRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelShellback());
        addRenderLayer(new ShellbackSaddleLayer<>(context, this, true));
        addRenderLayer(new ShellbackSaddleLayer<>(context, this, false));
        addRenderLayer(HangingChestLayer.shellbackChest(context, this, 0));
        addRenderLayer(HangingChestLayer.shellbackChest(context, this, 1));
        addRenderLayer(HangingChestLayer.shellbackChest(context, this, 2));
        addRenderLayer(HangingChestLayer.shellbackChest(context, this, 3));
        this.shadowRadius = 1.5F;
    }

    @Override
    public void actuallyRender(
            PoseStack poseStack, Shellback animatable, BakedGeoModel model,
            @Nullable RenderType renderType, MultiBufferSource bufferSource,
            @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
            int packedLight, int packedOverlay, int colour
    ) {
        poseStack.pushPose();
        poseStack.scale(5f, 5f, 5f);
        super.actuallyRender(
                poseStack, animatable, model, renderType, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Shellback shellback) {
        return Aquamirae.id("textures/entity/shellback.png");
    }
}