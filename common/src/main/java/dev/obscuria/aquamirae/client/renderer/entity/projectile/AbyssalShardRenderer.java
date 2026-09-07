package dev.obscuria.aquamirae.client.renderer.entity.projectile;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelShard;
import dev.obscuria.aquamirae.common.entity.projectile.AbyssalShard;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AbyssalShardRenderer<T extends AbyssalShard> extends ShardRenderer<T> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/projectile/abyssal_shard.png");

    public AbyssalShardRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelShard<>(context.bakeLayer(AquamiraeModelLayers.ABYSSAL_SHARD)));
    }

    @Override
    public ResourceLocation getTextureLocation(T shard) {
        return TEXTURE;
    }
}
