package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class OverlayGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

    protected final ResourceLocation texture;

    public OverlayGeoLayer(GeoRenderer<T> renderer) {
        super(renderer);
        this.texture = getTextureResource(renderer.getAnimatable()).withPath(this::pathToOverlay);
    }

    @Override
    public void render(
            PoseStack poseStack, T animatable, BakedGeoModel bakedModel,
            RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
            float partialTick, int packedLight, int packedOverlay
    ) {
        var overlayRenderType = RenderType.eyes(texture);
        getRenderer().reRender(
                bakedModel, poseStack, bufferSource, animatable, overlayRenderType,
                bufferSource.getBuffer(overlayRenderType), partialTick, LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY, -1);
        bufferSource.getBuffer(renderType);
    }

    protected String pathToOverlay(String path) {
        return path.replace(".png", "_overlay.png");
    }
}