
package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelAnglerfish;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.common.entity.Anglerfish;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class AnglerfishRenderer<T extends Anglerfish> extends MobRenderer<T, ModelAnglerfish<T>> {

    public static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/anglerfish.png");
    public static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/anglerfish_overlay.png");

    public AnglerfishRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAnglerfish<>(context.bakeLayer(AquamiraeModelLayers.ANGLERFISH)), 1.5f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
    }

    @Override
    public void render(
            @NotNull T entity, float entityYaw, float partialTicks,
            PoseStack poseStack, MultiBufferSource buffer, int packedLight
    ) {
        poseStack.pushPose();
        var groundMod = Mth.lerp(partialTicks, entity.clientGroundModO, entity.clientGroundMod);
        var offset = Mth.clampedMap(groundMod, 0f, 1f, -0.75f, 0f);
        poseStack.translate(0f, offset, 0f);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return TEXTURE;
    }
}
