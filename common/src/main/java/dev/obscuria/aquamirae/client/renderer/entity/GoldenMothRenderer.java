package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.client.model.ModelGoldenMoth;
import dev.obscuria.aquamirae.common.entity.GoldenMoth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public final class GoldenMothRenderer extends GeoEntityRenderer<GoldenMoth> {

    public GoldenMothRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelGoldenMoth());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.shadowRadius = 0.2f;
    }
}
