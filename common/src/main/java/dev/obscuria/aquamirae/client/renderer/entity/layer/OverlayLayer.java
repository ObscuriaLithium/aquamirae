package dev.obscuria.aquamirae.client.renderer.entity.layer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class OverlayLayer<T extends Entity, M extends EntityModel<T>> extends EyesLayer<T, M> {

    private final RenderType renderType;

    public OverlayLayer(RenderLayerParent<T, M> parent, ResourceLocation texture) {
        super(parent);
        this.renderType = RenderType.eyes(texture);
    }

    @Override
    public RenderType renderType() {
        return renderType;
    }
}
