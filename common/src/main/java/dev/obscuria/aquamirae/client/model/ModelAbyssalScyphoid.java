package dev.obscuria.aquamirae.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.common.entity.AbyssalScyphoid;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import dev.obscuria.fragmentum.content.util.easing.EasingFunction;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class ModelAbyssalScyphoid<T extends AbyssalScyphoid> extends EntityModel<T> {

    protected static final EasingFunction MOTION_EASING;
    protected static final EasingFunction SQUISH_EASING;
    protected static final float SQUISH_START;
    protected static final float SQUISH_END;

    protected final ModelPart main;
    protected final ModelPart head;

    protected final ModelPart ten1_1, ten1_2, ten1_3, ten1_4, ten1_5;
    protected final ModelPart ten2_1, ten2_2, ten2_3, ten2_4, ten2_5;
    protected final ModelPart ten3_1, ten3_2, ten3_3, ten3_4, ten3_5;
    protected final ModelPart ten4_1, ten4_2, ten4_3, ten4_4, ten4_5;

    public ModelAbyssalScyphoid(ModelPart root) {

        this.main = root.getChild("main");
        this.head = main.getChild("head");

        this.ten1_1 = main.getChild("ten1_1");
        this.ten1_2 = ten1_1.getChild("ten1_2");
        this.ten1_3 = ten1_2.getChild("ten1_3");
        this.ten1_4 = ten1_3.getChild("ten1_4");
        this.ten1_5 = ten1_4.getChild("ten1_5");

        this.ten2_1 = main.getChild("ten2_1");
        this.ten2_2 = ten2_1.getChild("ten2_2");
        this.ten2_3 = ten2_2.getChild("ten2_3");
        this.ten2_4 = ten2_3.getChild("ten2_4");
        this.ten2_5 = ten2_4.getChild("ten2_5");

        this.ten3_1 = main.getChild("ten3_1");
        this.ten3_2 = ten3_1.getChild("ten3_2");
        this.ten3_3 = ten3_2.getChild("ten3_3");
        this.ten3_4 = ten3_3.getChild("ten3_4");
        this.ten3_5 = ten3_4.getChild("ten3_5");

        this.ten4_1 = main.getChild("ten4_1");
        this.ten4_2 = ten4_1.getChild("ten4_2");
        this.ten4_3 = ten4_2.getChild("ten4_3");
        this.ten4_4 = ten4_3.getChild("ten4_4");
        this.ten4_5 = ten4_4.getChild("ten4_5");
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        var main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(4, 13).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.2F)).texOffs(0, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, 0.0F));
        var ten1_1 = main.addOrReplaceChild("ten1_1", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -12.5F, 0.0F));
        var ten1_2 = ten1_1.addOrReplaceChild("ten1_2", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        var ten1_3 = ten1_2.addOrReplaceChild("ten1_3", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        var ten1_4 = ten1_3.addOrReplaceChild("ten1_4", CubeListBuilder.create().texOffs(0, 19).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        ten1_4.addOrReplaceChild("ten1_5", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        var ten2_1 = main.addOrReplaceChild("ten2_1", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -12.5F, 0.0F));
        var ten2_2 = ten2_1.addOrReplaceChild("ten2_2", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        var ten2_3 = ten2_2.addOrReplaceChild("ten2_3", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        var ten2_4 = ten2_3.addOrReplaceChild("ten2_4", CubeListBuilder.create().texOffs(0, 19).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        ten2_4.addOrReplaceChild("ten2_5", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        var ten3_1 = main.addOrReplaceChild("ten3_1", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -12.5F, -1.5F, 0.0F, 1.5708F, 0.0F));
        var ten3_2 = ten3_1.addOrReplaceChild("ten3_2", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        var ten3_3 = ten3_2.addOrReplaceChild("ten3_3", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        var ten3_4 = ten3_3.addOrReplaceChild("ten3_4", CubeListBuilder.create().texOffs(0, 19).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        ten3_4.addOrReplaceChild("ten3_5", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        var ten4_1 = main.addOrReplaceChild("ten4_1", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -12.5F, 1.5F, 0.0F, 1.5708F, 0.0F));
        var ten4_2 = ten4_1.addOrReplaceChild("ten4_2", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        var ten4_3 = ten4_2.addOrReplaceChild("ten4_3", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        var ten4_4 = ten4_3.addOrReplaceChild("ten4_4", CubeListBuilder.create().texOffs(0, 19).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        ten4_4.addOrReplaceChild("ten4_5", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer vertexConsumer,
            int packedLight, int packedOverlay, int modulate
    ) {
        this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay, modulate);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch
    ) {
        var variant = entity.getScale();
        var lifetime = ageInTicks + 1000F * variant;
        var cycle = 20F + 60F * variant;

        var motionProgress = (lifetime % cycle) / cycle;

        this.main.y = 24F + 12F - 12F * MOTION_EASING.compute(motionProgress);
        this.main.yRot = lifetime * 0.01F;

        var squishXZ = 1.0F + SQUISH_EASING.compute(squishProgress(motionProgress, 0.00F)) * 0.8F;
        var squishY = 1.0F - SQUISH_EASING.compute(squishProgress(motionProgress, 0.01F)) * 0.6F;

        this.head.xScale = squishXZ;
        this.head.yScale = squishY;
        this.head.zScale = squishXZ;

        animateTentacle(ten1_1, ten1_2, ten1_3, ten1_4, ten1_5, lifetime, false);
        animateTentacle(ten2_1, ten2_2, ten2_3, ten2_4, ten2_5, lifetime, true);
        animateTentacle(ten3_1, ten3_2, ten3_3, ten3_4, ten3_5, lifetime, false);
        animateTentacle(ten4_1, ten4_2, ten4_3, ten4_4, ten4_5, lifetime, true);

        animateTentacleSquish(ten1_1, ten1_2, ten1_3, ten1_4, ten1_5, motionProgress, true);
        animateTentacleSquish(ten2_1, ten2_2, ten2_3, ten2_4, ten2_5, motionProgress, false);
        animateTentacleSquish(ten3_1, ten3_2, ten3_3, ten3_4, ten3_5, motionProgress, true);
        animateTentacleSquish(ten4_1, ten4_2, ten4_3, ten4_4, ten4_5, motionProgress, false);
    }

    protected void animateTentacle(
            ModelPart part1, ModelPart part2, ModelPart part3, ModelPart part4,
            ModelPart part5, float lifetime, boolean mirrored
    ) {
        var sign = mirrored ? -1.0F : 1.0F;
        part1.zRot = (-0.02F + 0.02F * (float) Math.cos(lifetime * 0.1F - 0.0F)) * sign;
        part2.zRot = (-0.04F + 0.04F * (float) Math.cos(lifetime * 0.1F - 0.5F)) * sign;
        part3.zRot = (-0.06F + 0.06F * (float) Math.cos(lifetime * 0.1F - 1.0F)) * sign;
        part4.zRot = (-0.08F + 0.08F * (float) Math.cos(lifetime * 0.1F - 1.5F)) * sign;
        part5.zRot = (-0.10F + 0.10F * (float) Math.cos(lifetime * 0.1F - 2.0F)) * sign;

        setScaleXZ(part2, 0.99f);
        setScaleXZ(part3, 0.98f);
        setScaleXZ(part4, 0.97f);
        setScaleXZ(part5, 0.96f);
    }

    protected void animateTentacleSquish(
            ModelPart part1, ModelPart part2, ModelPart part3, ModelPart part4,
            ModelPart part5, float motionProgress, boolean mirrored
    ) {
        var sign = mirrored ? -1.0F : 1.0F;
        part1.zRot += 0.25F * SQUISH_EASING.compute(squishProgress(motionProgress, 0.00F)) * sign;
        part2.zRot += 0.30F * SQUISH_EASING.compute(squishProgress(motionProgress, 0.01F)) * sign;
        part3.zRot += 0.35F * SQUISH_EASING.compute(squishProgress(motionProgress, 0.02F)) * sign;
        part4.zRot += 0.40F * SQUISH_EASING.compute(squishProgress(motionProgress, 0.03F)) * sign;
        part5.zRot += 0.45F * SQUISH_EASING.compute(squishProgress(motionProgress, 0.04F)) * sign;
    }

    protected float squishProgress(float time, float offset) {
        var start = ((SQUISH_START + offset) % 1.0F + 1.0F) % 1.0F;
        var end = ((SQUISH_END + offset) % 1.0F + 1.0F) % 1.0F;

        var length = (end >= start) ? (end - start) : (1.0F - start + end);
        var delta = (time - start + 1.0F) % 1.0F;
        var progress = delta / length;

        return (delta <= length) ? progress : 0.0F;
    }

    protected void setScaleXZ(ModelPart part, float scale) {
        part.xScale = scale;
        part.zScale = scale;
    }

    static {
        MOTION_EASING = Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_IN_QUAD, 0.2f);
        SQUISH_EASING = Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_OUT_ELASTIC, 0.25f);
        SQUISH_START = -0.1F;
        SQUISH_END = 0.3F;
    }
}