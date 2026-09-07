package dev.obscuria.aquamirae.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.common.entity.Spinefish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelSpinefish<T extends Spinefish> extends EntityModel<T> {

    protected final ModelPart main, bodyTop, bodyBottom, head, tail;

    public ModelSpinefish(ModelPart root) {
        this.main = root.getChild("main");
        this.bodyTop = main.getChild("bodyTop");
        this.bodyBottom = main.getChild("bodyBottom");
        this.head = bodyTop.getChild("head");
        this.tail = bodyBottom.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        var main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 17.5F, 0.0F));
        var bodyTop = main.addOrReplaceChild("bodyTop", CubeListBuilder.create().texOffs(16, 8).addBox(-1.0F, -2.5F, -3.0F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(10, 0).addBox(0.0F, -6.5F, -3.0F, 0.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));
        var head = bodyTop.addOrReplaceChild("head", CubeListBuilder.create().texOffs(6, 13).addBox(0.0F, -5.5F, -6.0303F, 0.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(18, 16).addBox(-1.0F, -2.5F, -3.0303F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 11).addBox(0.0F, -6.5F, -3.0303F, 0.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.9697F));
        head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -3.5F, -0.5F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, -5.5303F, -0.7854F, 0.0F, 0.0F));
        var bodyBottom = main.addOrReplaceChild("bodyBottom", CubeListBuilder.create().texOffs(12, 13).addBox(0.0F, -5.5F, 0.0F, 0.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(15, 24).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));
        bodyBottom.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.0F, -4.5F, 0.0F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        main.zRot = entity.isInWater() ? 0f : -Mth.PI / 2f;
        main.y = entity.isInWater() ? 17.5f : 25f;
        main.x = entity.isInWater() ? 1.5f * Mth.cos(ageInTicks * 0.5f) : 0f;
        head.yRot = 0.3f * Mth.cos(ageInTicks * 0.5f);
        bodyTop.yRot = 0.15f * Mth.cos(ageInTicks * 0.5f - 0.5f);
        bodyBottom.yRot = -0.15f * Mth.cos(ageInTicks * 0.5f + 0.8f);
        tail.yRot = -0.3f * Mth.cos(ageInTicks * 0.5f + 0.18f);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer vertexConsumer,
            int packedLight, int packedOverlay, int modulate
    ) {
        this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay, modulate);
    }
}