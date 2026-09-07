package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelEel;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.common.entity.Eel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EelRenderer<T extends Eel> extends MobRenderer<T, ModelEel<T>> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/eel.png");
    public static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/eel_overlay.png");

    public EelRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelEel<>(context.bakeLayer(AquamiraeModelLayers.EEL)), 0f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
