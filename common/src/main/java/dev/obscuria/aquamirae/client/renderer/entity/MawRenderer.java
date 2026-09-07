package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelMaw;
import dev.obscuria.aquamirae.client.renderer.entity.layer.MawItemLayer;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.common.entity.Maw;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MawRenderer<T extends Maw> extends MobRenderer<T, ModelMaw<T>> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/maw.png");
    public static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/maw_overlay.png");

    public MawRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMaw<>(context.bakeLayer(AquamiraeModelLayers.MAW)), 0.9f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
        this.addLayer(new MawItemLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
