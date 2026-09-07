package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelTorturedSoul;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.common.entity.TorturedSoul;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TorturedSoulRenderer<T extends TorturedSoul> extends MobRenderer<T, ModelTorturedSoul<T>> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/tortured_soul.png");
    public static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/tortured_soul_overlay.png");

	public TorturedSoulRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelTorturedSoul<>(context.bakeLayer(AquamiraeModelLayers.TORTURED_SOUL)), 0.5f);
		this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}

	@Override
	protected boolean isShaking(TorturedSoul entity) {
		return true;
	}
}
