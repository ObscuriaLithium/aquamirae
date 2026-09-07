package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelCaptainCornelia;
import dev.obscuria.aquamirae.client.renderer.entity.layer.CaptainCorneliaWeaponLayer;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.common.entity.CaptainCorneliaBoss;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CaptainCorneliaRenderer<T extends CaptainCorneliaBoss> extends MobRenderer<T, ModelCaptainCornelia<T>> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/captain_cornelia.png");
    public static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/captain_cornelia_overlay.png");

    public CaptainCorneliaRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCaptainCornelia<>(context.bakeLayer(AquamiraeModelLayers.CAPTAIN_CORNELIA)), 0.5f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
        this.addLayer(new CaptainCorneliaWeaponLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
