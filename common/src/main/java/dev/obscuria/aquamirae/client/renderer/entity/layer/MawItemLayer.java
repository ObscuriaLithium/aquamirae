package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.client.model.ModelMaw;
import dev.obscuria.aquamirae.common.entity.Maw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;

public class MawItemLayer<T extends Maw> extends RenderLayer<T, ModelMaw<T>> {

    public MawItemLayer(RenderLayerParent<T, ModelMaw<T>> layer) {
        super(layer);
    }

    public void render(
            PoseStack pose, MultiBufferSource source, int i1, T maw,
            float f1, float f2, float f3, float f4, float f5, float f6
    ) {
        if (maw.getMouthItem().isEmpty()) return;
        pose.pushPose();
        getParentModel().translateToMouth(pose);
        pose.mulPose(Axis.XP.rotationDegrees(100.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(0.0F));
        pose.translate(0.0D, -0.8D, 0.02D);
        pose.scale(0.7f, 0.7f, 0.7f);
        Minecraft.getInstance().gameRenderer.itemInHandRenderer.renderItem(
                maw, maw.getMouthItem(), ItemDisplayContext.FIXED,
                false, pose, source, i1);
        pose.popPose();
    }
}
