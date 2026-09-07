package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelMazeMother;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.common.entity.MazeMother;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MazeMotherRenderer<T extends MazeMother> extends MobRenderer<T, ModelMazeMother<T>> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/maze_mother.png");
    public static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/maze_mother_overlay.png");

    public MazeMotherRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMazeMother<>(context.bakeLayer(AquamiraeModelLayers.MAZE_MOTHER)), 1f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
