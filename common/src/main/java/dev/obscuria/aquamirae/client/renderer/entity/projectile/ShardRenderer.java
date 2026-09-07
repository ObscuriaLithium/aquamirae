package dev.obscuria.aquamirae.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.client.model.ModelShard;
import dev.obscuria.aquamirae.common.entity.projectile.AbstractShard;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;

public abstract class ShardRenderer<T extends AbstractShard> extends EntityRenderer<T> {

    private final ModelShard<T> model;

    public ShardRenderer(EntityRendererProvider.Context context, ModelShard<T> model) {
        super(context);
        this.model = model;
    }

    @Override
    public void render(T shard, float yaw, float delta, PoseStack pose, MultiBufferSource source, int light) {
        super.render(shard, yaw, delta, pose, source, light);
        pose.pushPose();
        pose.scale(-1, -1, 1);
        var scale = shard.getShardScale();
        pose.scale(scale, scale, scale);
        pose.translate(0, -1.5, 0);
        this.model.setupAnim(shard, 0, 0,
                shard.tickCount + delta,
                Mth.lerp(delta, shard.localXRotO, shard.localXRot),
                Mth.lerp(delta, shard.localYRotO, shard.localYRot));
        this.model.renderToBuffer(pose,
                source.getBuffer(RenderType.eyes(this.getTextureLocation(shard))),
                light, OverlayTexture.NO_OVERLAY, CommonColors.WHITE);
        pose.popPose();
    }
}
