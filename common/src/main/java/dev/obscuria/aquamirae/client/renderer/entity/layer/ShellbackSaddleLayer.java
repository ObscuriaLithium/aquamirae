package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelShellbackSaddle;
import dev.obscuria.aquamirae.common.entity.Shellback;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

public class ShellbackSaddleLayer<T extends Shellback> extends GeoRenderLayer<T> {

    public static final ResourceLocation TEXTURE;

    protected final String boneName;
    protected final int requiredAmount;
    protected final ModelShellbackSaddle model;

    public ShellbackSaddleLayer(
            EntityRendererProvider.Context context,
            GeoRenderer<T> entityRendererIn,
            boolean mainSaddle
    ) {
        super(entityRendererIn);
        this.boneName = mainSaddle ? "main_saddle" : "extra_saddle";
        this.requiredAmount = mainSaddle ? 1 : 2;
        this.model = new ModelShellbackSaddle(context.bakeLayer(AquamiraeModelLayers.SHELLBACK_SADDLE));
    }

    @Override
    public void renderForBone(
            PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
            MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick,
            int packedLight, int packedOverlay
    ) {
        if (!bone.getName().equals(boneName)) return;
        if (animatable.getSaddleCount() < requiredAmount) return;

        poseStack.pushPose();
        RenderUtil.translateAndRotateMatrixForBone(poseStack, bone);
        poseStack.scale(-0.25f, -0.25f, 0.25f);
        this.model.renderToBuffer(
                poseStack, bufferSource.getBuffer(model.renderType(TEXTURE)),
                packedLight, packedOverlay, -1);
        poseStack.popPose();

        bufferSource.getBuffer(renderType);
    }

    static {
        TEXTURE = Aquamirae.id("textures/entity/shellback_saddle.png");
    }
}
