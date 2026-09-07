package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.ModelScuttler;
import dev.obscuria.aquamirae.common.entity.Scuttler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class ScuttlerRenderer extends GeoEntityRenderer<Scuttler> {

    public ScuttlerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelScuttler());
        addRenderLayer(new StolenItemLayer(this));
        this.shadowRadius = 0.2F;
    }

    @Override
    public ResourceLocation getTextureLocation(Scuttler scuttler) {
        return Aquamirae.id("textures/entity/scuttler.png");
    }

    @Override
    public void actuallyRender(
            PoseStack poseStack, Scuttler animatable, BakedGeoModel model,
            @Nullable RenderType renderType, MultiBufferSource bufferSource,
            @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
            int packedLight, int packedOverlay, int colour
    ) {
        poseStack.pushPose();

        var snowOffset = 0.125f * Mth.lerp(partialTick, animatable.localInSnowO, animatable.localInSnow);
        var waterOffset = 0.33f * Mth.lerp(partialTick, animatable.localInWaterO, animatable.localInWater);
        poseStack.translate(0, snowOffset + waterOffset, 0);

        super.actuallyRender(
                poseStack, animatable, model, renderType, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.popPose();
    }

    private static class StolenItemLayer extends BlockAndItemGeoLayer<Scuttler> {

        private static final String SEGMENT_2 = "segment_2";

        public StolenItemLayer(GeoRenderer<Scuttler> renderer) {
            super(renderer);
        }

        @Override
        protected ItemStack getStackForBone(GeoBone bone, Scuttler animatable) {
            return SEGMENT_2.equals(bone.getName())
                    ? animatable.getStolenItem()
                    : ItemStack.EMPTY;
        }

        @Override
        protected ItemDisplayContext getTransformTypeForStack(
                GeoBone bone,
                ItemStack stack,
                Scuttler animatable
        ) {
            return ItemDisplayContext.FIXED;
        }

        @Override
        protected void renderStackForBone(
                PoseStack pose,
                GeoBone bone,
                ItemStack stack,
                Scuttler animatable,
                MultiBufferSource source,
                float partialTick,
                int packedLight,
                int packedOverlay
        ) {
            pose.scale(0.5F, 0.5F, 0.5F);
            pose.translate(0.0F, 0.3F, 0.0F);
            pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
            super.renderStackForBone(pose, bone, stack, animatable, source, partialTick, packedLight, packedOverlay);
        }
    }
}