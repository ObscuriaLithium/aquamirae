package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelAbyssalScyphoid;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.common.entity.AbyssalScyphoid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AbyssalScyphoidRenderer<T extends AbyssalScyphoid> extends MobRenderer<T, ModelAbyssalScyphoid<T>> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/abyssal_scyphoid.png");
    public static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/abyssal_scyphoid_overlay.png");

    public AbyssalScyphoidRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAbyssalScyphoid<>(context.bakeLayer(AquamiraeModelLayers.ABYSSAL_SCYPHOID)), 0.25f);
        this.addLayer(new OverlayLayer<>(this, TEXTURE));
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
    }

    @Override
    public ResourceLocation getTextureLocation(AbyssalScyphoid entity) {
        return TEXTURE;
    }
}
