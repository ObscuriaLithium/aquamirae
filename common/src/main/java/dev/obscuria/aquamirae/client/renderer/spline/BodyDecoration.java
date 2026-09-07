package dev.obscuria.aquamirae.client.renderer.spline;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.common.SplineBody;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.function.Function;

public abstract class BodyDecoration extends Model {

    private final ModelPart root;

    protected BodyDecoration(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
        this.root = root;
    }

    public abstract ResourceLocation getTexture();

    public abstract ResourceLocation getOverlayTexture();

    public void render(
            Context context, PoseStack pose, MultiBufferSource buffer,
            int packedOverlay, int packedLight, float partialTicks
    ) {
        pose.pushPose();
        var scale = resolveScale(context);
        pose.scale(scale, scale, scale);
        renderToBuffer(pose, buffer.getBuffer(renderType(getTexture())), packedLight, packedOverlay, -1);
        renderToBuffer(pose, buffer.getBuffer(RenderType.eyes(getOverlayTexture())), packedLight, packedOverlay, -1);
        pose.popPose();
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer vertexConsumer,
            int packedLight, int packedOverlay, int color
    ) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    protected float resolveScale(Context context) {
        return context.widthFactor;
    }

    public record Context(
            Level level, Vector3f origin, SplineBody body,
            SplineBody.BakedRing ring, float widthFactor) {}
}
