package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.client.renderer.AquamiraeArmorRenderer;
import dev.obscuria.aquamirae.common.item.AquamiraeArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ArmorAutoGlowingLayer extends AutoGlowingGeoLayer<AquamiraeArmorItem> {

    public ArmorAutoGlowingLayer(AquamiraeArmorRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(
            PoseStack poseStack, AquamiraeArmorItem animatable, BakedGeoModel bakedModel,
            RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
            float partialTick, int packedLight, int packedOverlay
    ) {
        if (!animatable.hasGlowingLayer()) return;
        super.render(
                poseStack, animatable, bakedModel,
                renderType, bufferSource, buffer, partialTick,
                packedLight, packedOverlay);
    }
}