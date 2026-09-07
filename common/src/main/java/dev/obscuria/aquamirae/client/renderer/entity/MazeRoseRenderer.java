package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelMazeRose;
import dev.obscuria.aquamirae.common.entity.projectile.MazeRose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MazeRoseRenderer<T extends MazeRose> extends EntityRenderer<T> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/maze_rose.png");
    public static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/maze_rose_overlay.png");

    private final ModelMazeRose<T> model;

    public MazeRoseRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelMazeRose<>(context.bakeLayer(AquamiraeModelLayers.MAZE_ROSE));
    }

    @Override
    public void render(
            T entity, float entityYaw, float partialTicks,
            PoseStack pose, MultiBufferSource buffer, int packedLight
    ) {
        pose.pushPose();
        float yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        pose.mulPose(Axis.YP.rotationDegrees(yaw + 180.0F));
        pose.mulPose(Axis.XP.rotationDegrees(pitch));
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.translate(0.0F, -1.501F, 0.0F);

        var ageInTicks = entity.tickCount + partialTicks;
        this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, yaw, pitch);
        {
            var consumer = buffer.getBuffer(model.renderType(getTextureLocation(entity)));
            this.model.renderToBuffer(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
        {
            var consumer = buffer.getBuffer(RenderType.eyes(OVERLAY_TEXTURE));
            this.model.renderToBuffer(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
        pose.popPose();
        super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}