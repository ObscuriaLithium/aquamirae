package dev.obscuria.aquamirae.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.client.animation.AnimPipeline;
import dev.obscuria.aquamirae.common.entity.CaptainCorneliaBoss;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class ModelCaptainCornelia<T extends CaptainCorneliaBoss> extends AnimatedEntityModel<T> {

    public final ModelPart bodyTop, bodyTop2, bodyBottom, head, rightBooby, leftBooby;
    public final ModelPart rightArm, leftArm, rightLeg, leftLeg, rightArmBottom, leftArmBottom;
    public final ModelPart rightLegBottom, leftLegBottom, item;
    public final ModelPart ten1, ten1_1, ten1_2, ten1_3, ten1_4;
    public final ModelPart ten2, ten2_1, ten2_2, ten2_3, ten2_4;
    public final ModelPart ten3, ten3_1, ten3_2, ten3_3, ten3_4;

    public ModelCaptainCornelia(ModelPart root) {
        super(root, "main");
        this.bodyTop = main.getChild("body_top_Z").getChild("body_top");
        this.bodyTop2 = bodyTop.getChild("body_top2");
        this.bodyBottom = main.getChild("body_bottom_Z").getChild("body_bottom");
        this.head = bodyTop2.getChild("head");
        this.rightBooby = bodyTop2.getChild("right_booby");
        this.leftBooby = bodyTop2.getChild("left_booby");
        this.rightArm = bodyTop2.getChild("right_arm");
        this.leftArm = bodyTop2.getChild("left_arm");
        this.rightArmBottom = rightArm.getChild("right_arm_bottom");
        this.leftArmBottom = leftArm.getChild("left_arm_bottom");
        this.rightLeg = bodyBottom.getChild("right_leg");
        this.leftLeg = bodyBottom.getChild("left_leg");
        this.rightLegBottom = rightLeg.getChild("right_leg_bottom");
        this.leftLegBottom = leftLeg.getChild("left_leg_bottom");
        this.item = rightArmBottom.getChild("bone");
        this.ten1 = bodyTop2.getChild("ten1");
        this.ten1_1 = ten1.getChild("ten1_1");
        this.ten1_2 = ten1_1.getChild("ten1_2");
        this.ten1_3 = ten1_2.getChild("ten1_3");
        this.ten1_4 = ten1_3.getChild("ten1_4");
        this.ten2 = bodyTop2.getChild("ten2");
        this.ten2_1 = ten2.getChild("ten2_1");
        this.ten2_2 = ten2_1.getChild("ten2_2");
        this.ten2_3 = ten2_2.getChild("ten2_3");
        this.ten2_4 = ten2_3.getChild("ten2_4");
        this.ten3 = bodyTop2.getChild("ten3");
        this.ten3_1 = ten3.getChild("ten3_1");
        this.ten3_2 = ten3_1.getChild("ten3_2");
        this.ten3_3 = ten3_2.getChild("ten3_3");
        this.ten3_4 = ten3_3.getChild("ten3_4");
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();
        var main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 150.0F));
        var body_top_Z = main.addOrReplaceChild("body_top_Z", CubeListBuilder.create(), PartPose.offset(0.0F, -25.5F, -150.0F));
        var body_top = body_top_Z.addOrReplaceChild("body_top", CubeListBuilder.create().texOffs(32, 41).addBox(-2.5F, -6.5F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 30.0F, 0.0F));
        var body_top2 = body_top.addOrReplaceChild("body_top2", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));
        var head = body_top2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)).texOffs(24, 8).addBox(-4.0F, -9.75F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(37, 54).addBox(-1.5F, -10.25F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(72, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 1.0F, 0.0873F, 0.0F, 0.0F));
        head.addOrReplaceChild("head1", CubeListBuilder.create().texOffs(28, 53).addBox(-1.5F, 0.75F, 5.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.5F, 7.0F, -1.5708F, 0.0F, 0.0F));
        head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(22, 22).addBox(-4.0F, -5.75F, 1.5F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.5F, 0.0F, -1.5708F, 0.0F, 0.0F));
        head.addOrReplaceChild("head3", CubeListBuilder.create().texOffs(12, 49).addBox(-3.5F, -3.5F, -1.05F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 57).addBox(3.0F, -3.0F, -0.3F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 33).addBox(-4.0F, -3.0F, -0.3F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(52, 31).addBox(-4.0F, 3.0F, -0.3F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 48).addBox(-4.0F, -4.0F, -0.3F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.25F, -4.0F, 0.05F, 0.0F, 1.5708F, 0.0F));
        head.addOrReplaceChild("head4", CubeListBuilder.create().texOffs(46, 17).addBox(-3.5F, -3.5F, -1.05F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 0).addBox(3.0F, -3.0F, -0.3F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 0).addBox(-4.0F, -3.0F, -0.3F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 27).addBox(-4.0F, 3.0F, -0.3F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 25).addBox(-4.0F, -4.0F, -0.3F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.25F, -4.0F, 0.05F, 0.0F, -1.5708F, 0.0F));
        head.addOrReplaceChild("head5", CubeListBuilder.create().texOffs(24, 0).addBox(-3.5F, -31.5F, -6.25F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 0).addBox(3.0F, -31.0F, -5.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.0F, -31.0F, -5.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 19).addBox(-4.0F, -25.0F, -5.5F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 17).addBox(-4.0F, -32.0F, -5.5F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        body_top2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 31).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, -4.5F, 0.0F));
        PartDefinition left_booby = body_top2.addOrReplaceChild("left_booby", CubeListBuilder.create(), PartPose.offset(2.0F, -0.75F, 0.25F));
        left_booby.addOrReplaceChild("left_boobyF", CubeListBuilder.create().texOffs(50, 41).addBox(-2.0F, -2.5F, 0.25F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -4.0F, -0.5236F, -0.2269F, 0.0175F));
        PartDefinition right_booby = body_top2.addOrReplaceChild("right_booby", CubeListBuilder.create(), PartPose.offset(-2.0F, -0.75F, 0.25F));
        right_booby.addOrReplaceChild("right_boobyF", CubeListBuilder.create().texOffs(47, 50).addBox(-2.0F, -2.5F, 0.25F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -4.0F, -0.5236F, 0.2269F, -0.0175F));
        PartDefinition left_arm = body_top2.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 0).addBox(0.25F, -1.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(77, 16).addBox(0.25F, -1.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.2F)).texOffs(89, 16).addBox(0.25F, -5.0F, -0.5F, 7.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(77, 16).addBox(0.25F, -1.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(4.0F, -3.5F, 1.0F));
        left_arm.addOrReplaceChild("left_arm_bottom", CubeListBuilder.create().texOffs(0, 64).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(1.75F, 5.0F, -0.5F));
        PartDefinition right_arm = body_top2.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.25F, -3.5F, 0.0F));
        PartDefinition right_arm_bottom = right_arm.addOrReplaceChild("right_arm_bottom", CubeListBuilder.create().texOffs(0, 64).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(-1.5F, 5.0F, 0.5F));
        right_arm_bottom.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(84, 106).addBox(-0.5F, -0.5F, -10.5F, 1.0F, 1.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.5F, 0.0F));
        PartDefinition ten1 = body_top2.addOrReplaceChild("ten1", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5F, 3.5F));
        PartDefinition ten1_1 = ten1.addOrReplaceChild("ten1_1", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition ten1_2 = ten1_1.addOrReplaceChild("ten1_2", CubeListBuilder.create().texOffs(60, 6).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition ten1_3 = ten1_2.addOrReplaceChild("ten1_3", CubeListBuilder.create().texOffs(60, 6).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        ten1_3.addOrReplaceChild("ten1_4", CubeListBuilder.create().texOffs(60, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition ten2 = body_top2.addOrReplaceChild("ten2", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, -7.5F, 3.0F));
        PartDefinition ten2_1 = ten2.addOrReplaceChild("ten2_1", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition ten2_2 = ten2_1.addOrReplaceChild("ten2_2", CubeListBuilder.create().texOffs(60, 6).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition ten2_3 = ten2_2.addOrReplaceChild("ten2_3", CubeListBuilder.create().texOffs(60, 6).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        ten2_3.addOrReplaceChild("ten2_4", CubeListBuilder.create().texOffs(60, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition ten3 = body_top2.addOrReplaceChild("ten3", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.75F, -7.5F, 3.0F));
        PartDefinition ten3_1 = ten3.addOrReplaceChild("ten3_1", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition ten3_2 = ten3_1.addOrReplaceChild("ten3_2", CubeListBuilder.create().texOffs(60, 6).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition ten3_3 = ten3_2.addOrReplaceChild("ten3_3", CubeListBuilder.create().texOffs(60, 6).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        ten3_3.addOrReplaceChild("ten3_4", CubeListBuilder.create().texOffs(60, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition body_bottom_Z = main.addOrReplaceChild("body_bottom_Z", CubeListBuilder.create(), PartPose.offset(0.0F, 34.5F, -150.0F));
        PartDefinition body_bottom = body_bottom_Z.addOrReplaceChild("body_bottom", CubeListBuilder.create().texOffs(0, 16).addBox(-4.5F, -0.5F, -2.75F, 9.0F, 8.0F, 6.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -30.0F, 0.0F));
        PartDefinition right_leg = body_bottom.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 31).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 3.5F, 0.0F));
        right_leg.addOrReplaceChild("right_leg_bottom", CubeListBuilder.create().texOffs(12, 64).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 5.5F, 0.0F));
        PartDefinition left_leg = body_bottom.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 3.5F, 0.0F));
        left_leg.addOrReplaceChild("left_leg_bottom", CubeListBuilder.create().texOffs(12, 64).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 5.5F, 0.0F));
        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    public void translateToHand(HumanoidArm arm, PoseStack pose) {
        this.main.translateAndRotate(pose);
        this.main.getChild("body_top_Z").translateAndRotate(pose);
        this.bodyTop.translateAndRotate(pose);
        this.bodyTop2.translateAndRotate(pose);
        if (arm == HumanoidArm.LEFT) {
            this.leftArm.translateAndRotate(pose);
            this.leftArmBottom.translateAndRotate(pose);
        } else {
            this.rightArm.translateAndRotate(pose);
            this.rightArmBottom.translateAndRotate(pose);
            this.item.translateAndRotate(pose);
        }
    }

    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        var headYaw = (float) Math.toRadians(netHeadYaw) * 0.333f;
        this.head.yRot += headYaw;
        this.bodyTop.yRot += headYaw;
        this.bodyTop2.yRot += headYaw;
    }

    @Override // @formatter:off
    protected AnimPipeline<T> createAnimations() {
        return new AnimPipeline<T>()
                .resetParts(
                        main, bodyTop, bodyTop2, bodyBottom, head, rightBooby, leftBooby,
                        rightArm, leftArm, rightLeg, leftLeg, rightArmBottom, leftArmBottom,
                        rightLegBottom, leftLegBottom, item,
                        ten1, ten1_1, ten1_2, ten1_3, ten1_4,
                        ten2, ten2_1, ten2_2, ten2_3, ten2_4,
                        ten3, ten3_1, ten3_2, ten3_3, ten3_4)
                .ambientIdle(0.1f, 3.5f)
                    .keyframe(main, k -> k.xRot(-0.6F, 1.2F))
                    .keyframe(bodyBottom, k -> k.xRot(15F, 20F, -0.9f))
                    .keyframe(bodyTop2, k -> k.xRot(6F, -6F, 0.9f))
                    .keyframe(head, k -> k.xRot(4F, -6F, 0.8f))
                    .keyframe(rightArm, k -> k.rotation(12F, -9F, 9F, -27F, 6F, 20F, -0.95F))
                    .keyframe(rightArmBottom, k -> k.xRot(-16F, 20F, -0.9f))
                    .keyframe(item, k -> k.xRot(-5F, -35F, 0.85f))
                    .keyframe(leftArm, k -> k.rotation(12F, -9F, -9F, 27F, -9F, -27F, -0.95F))
                    .keyframe(leftArmBottom, k -> k.xRot(-24F, 30F, -0.9f))
                    .keyframe(rightLeg, k -> k.rotation(12F, 24F, 0.6F, -3F, 0.6F, -3F, -0.8F))
                    .keyframe(rightLegBottom, k -> k.xRot(-12F, -48F, -0.6f))
                    .keyframe(leftLeg, k -> k.rotation(6F, -12F, 0.6F, 3F, 0.6F, 3F, -0.8f))
                    .keyframe(leftLegBottom, k -> k.xRot(-24F, -27F, -0.7f))
                    .keyframe(ten1, k -> k.xRot(16F, -20F, 0.35f))
                    .keyframe(ten1_1, k -> k.xRot(16F, -12F, 0.3f))
                    .keyframe(ten1_2, k -> k.xRot(16F, -6F, 0.25f))
                    .keyframe(ten1_3, k -> k.xRot(16F, 0F, 0.2f))
                    .keyframe(ten1_4, k -> k.xRot(16F, 0F, 0.15f))
                    .keyframe(ten2, k -> k.xRot(16F, -20F, 0.25f).yRot(-30))
                    .keyframe(ten2_1, k -> k.xRot(16F, -12F, 0.2f))
                    .keyframe(ten2_2, k -> k.xRot(16F, -6F, 0.15f))
                    .keyframe(ten2_3, k -> k.xRot(16F, 0F, 0.1f))
                    .keyframe(ten2_4, k -> k.xRot(16F, 0F, 0.05f))
                    .keyframe(ten3, k -> k.xRot(16F, -20F, 0.45f).yRot(30))
                    .keyframe(ten3_1, k -> k.xRot(16F, -12F, 0.4f))
                    .keyframe(ten3_2, k -> k.xRot(16F, -6F, 0.35f))
                    .keyframe(ten3_3, k -> k.xRot(16F, 0F, 0.3f))
                    .keyframe(ten3_4, k -> k.xRot(16F, 0F, 0.25f))
                .end()
                .ambientWalk(0.3f, 3.5f)
                    .keyframe(main, k -> k.xRot(-1.4F, 1.8F))
                    .keyframe(bodyBottom, k -> k.xRot(6F, -36F))
                    .keyframe(bodyTop, k -> k.xRot(6F, -24F))
                    .keyframe(bodyTop2, k -> k.xRot(6F, 12F))
                    .keyframe(rightArm, k -> k.rotation(12F, -9F, 9F, -27F, -9F, 27F, 0.05F))
                    .keyframe(rightArmBottom, k -> k.xRot(24F, 30F))
                    .keyframe(leftArm, k -> k.rotation(12F, -9F, -9F, 27F, 9F, -27F, 0.05F))
                    .keyframe(leftArmBottom, k -> k.xRot(24F, 30F))
                    .keyframe(rightLeg, k -> k.rotation(24F, 12F, 0.6F, -6F, 0.6F, -6F, 0.5F))
                    .keyframe(rightLegBottom, k -> k.xRot(24F, -74F, 0.7f))
                    .keyframe(leftLeg, k -> k.rotation(24F, 0F, -0.6F, 6F, -0.6F, 6F, 0.5F))
                    .keyframe(leftLegBottom, k -> k.xRot(24F, -30F, 0.8f))
                    .keyframe(ten1, k -> k.xRot(-14F, -29F, 0.25f))
                    .keyframe(ten1_1, k -> k.xRot(-14F, -18F, 0.2f))
                    .keyframe(ten1_2, k -> k.xRot(-14F, -12F, 0.15f))
                    .keyframe(ten1_3, k -> k.xRot(-14F, -6F, 0.1f))
                    .keyframe(ten1_4, k -> k.xRot(-14F, -6F, 0.05f))
                    .keyframe(ten2, k -> k.xRot(-14F, -29F, 0.25f).yRot(-30))
                    .keyframe(ten2_1, k -> k.xRot(-14F, -18F, 0.2f))
                    .keyframe(ten2_2, k -> k.xRot(-14F, -12F, 0.15f))
                    .keyframe(ten2_3, k -> k.xRot(-14F, -6F, 0.1f))
                    .keyframe(ten2_4, k -> k.xRot(-14F, -6F, 0.05f))
                    .keyframe(ten3, k -> k.xRot(-14F, -29F, 0.25f).yRot(30))
                    .keyframe(ten3_1, k -> k.xRot(-14F, -18F, 0.2f))
                    .keyframe(ten3_2, k -> k.xRot(-14F, -12F, 0.15f))
                    .keyframe(ten3_3, k -> k.xRot(-14F, -6F, 0.1f))
                    .keyframe(ten3_4, k -> k.xRot(-14F, -6F, 0.05f))
                .end()
                .clip(self -> self.weaponSwitchState, 10, 10, Easing.EASE_OUT_BACK.scale(0.95f), Easing.EASE_OUT_CUBIC)
                    .pose(0, 20, Easing.CEIL, AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(bodyTop, k -> k.rotation(-20F, 22F, 0F))
                        .keyframe(bodyTop2, k -> k.rotation(-12F, 20F, 0F))
                        .keyframe(head, k -> k.rotation(-12F, 0F, 0F))
                        .keyframe(leftArm, k -> k.rotation(-20F, 0F, -17F))
                        .keyframe(leftArmBottom, k -> k.rotation(55F, 0F, 0F))
                        .keyframe(rightArm, k -> k.rotation(90F, 45F, 90F))
                        .keyframe(rightArmBottom, k -> k.rotation(40F, 0F, 0F))
                        .keyframe(item, k -> k.rotation(-45F, 0F, 0F))
                    .end()
                .end()
                .clip(self -> self.pullAttackState, 10, 10, Easing.EASE_OUT_SINE, Easing.EASE_OUT_CUBIC)
                    .pose(0, 10, Easing.CEIL, AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.rotation(2F, 0F, 0F))
                        .keyframe(bodyTop, k -> k.rotation(0F, -17.5F, 0F))
                        .keyframe(bodyTop2, k -> k.rotation(0F, -32.5F, 0F))
                        .keyframe(head, k -> k.rotation(-10F, 46F, -3.5F))
                        .keyframe(leftArm, k -> k.rotation(30F, 35F, -32F))
                        .keyframe(leftArmBottom, k -> k.rotation(25F, 0F, 0F))
                        .keyframe(rightArm, k -> k.rotation(5.5F, 28F, 19.5F))
                        .keyframe(rightArmBottom, k -> k.rotation(25F, 0F, 0F))
                        .keyframe(item, k -> k.rotation(-50F, 0F, 0F))
                        .keyframe(bodyBottom, k -> k.rotation(27.5F, 0F, 0F))
                        .keyframe(rightLeg, k -> k.rotation(25F, 2.6F, -4.2F))
                        .keyframe(rightLegBottom, k -> k.rotation(-60F, 0F, 0F))
                        .keyframe(leftLeg, k -> k.rotation(-2.5F, 0F, 2.5F))
                        .keyframe(leftLegBottom, k -> k.rotation(-17.5F, 0F, 0F))
                    .end()
                    .pose(10, 30, Easing.EASE_OUT_EXPO.scale(0.5f), AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.rotation(1.5F, 0F, 0F))
                        .keyframe(bodyTop, k -> k.rotation(0F, -7.5F, 0F))
                        .keyframe(bodyTop2, k -> k.rotation(10F, -15F, 0F))
                        .keyframe(head, k -> k.rotation(-15F, 27F, 0F))
                        .keyframe(leftArm, k -> k.rotation(-19F, 12.5F, -31F))
                        .keyframe(leftArmBottom, k -> k.rotation(105F, 0F, 0F))
                        .keyframe(rightArm, k -> k.rotation(-10F, 1F, 20F))
                        .keyframe(rightArmBottom, k -> k.rotation(52F, 0F, 0F))
                        .keyframe(item, k -> k.rotation(-50F, 0F, 0F))
                        .keyframe(bodyBottom, k -> k.rotation(-22.5F, 0F, 0F))
                        .keyframe(rightLeg, k -> k.rotation(25F, 2.6F, -4.2F))
                        .keyframe(rightLegBottom, k -> k.rotation(-70F, 0F, 0F))
                        .keyframe(leftLeg, k -> k.rotation(-2.5F, 0F, 2.5F))
                        .keyframe(leftLegBottom, k -> k.rotation(-17.5F, 0F, 0F))
                    .end()
                .end()
                .clip(self -> self.swingAttackState, 12, 20, Easing.EASE_OUT_SINE, Easing.EASE_OUT_CUBIC)
                    .pose(0, 12, Easing.CEIL, AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.rotation(2.5F, 0F, 0F))
                        .keyframe(bodyTop, k -> k.rotation(-14F, 42F, -2F))
                        .keyframe(bodyTop2, k -> k.rotation(0F, 25F, 0F))
                        .keyframe(head, k -> k.rotation(3F, -56F, -8F))
                        .keyframe(leftArm, k -> k.rotation(-21F, 12F, -18.5F))
                        .keyframe(leftArmBottom, k -> k.rotation(70F, 0F, 0F))
                        .keyframe(rightArm, k -> k.rotation(57F, 25F, 74F))
                        .keyframe(rightArmBottom, k -> k.rotation(75F, 0F, 0F))
                        .keyframe(item, k -> k.rotation(-35F, 0F, 0F))
                        .keyframe(bodyBottom, k -> k.rotation(25F, 0F, 0F))
                        .keyframe(rightLeg, k -> k.rotation(57F, 2.6F, -4.2F))
                        .keyframe(rightLegBottom, k -> k.rotation(-90F, 0F, 0F))
                        .keyframe(leftLeg, k -> k.rotation(-2.5F, 0F, 2.5F))
                        .keyframe(leftLegBottom, k -> k.rotation(-17.5F, 0F, 0F))
                    .end()
                    .pose(12, 40, Easing.EASE_OUT_EXPO.scale(0.2f), AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.rotation(1.5F, 0F, 0F))
                        .keyframe(bodyTop, k -> k.rotation(-10F, -4F, 8F))
                        .keyframe(bodyTop2, k -> k.rotation(0F, -20F, 0F))
                        .keyframe(head, k -> k.rotation(2F, 24F, -4.5F))
                        .keyframe(leftArm, k -> k.rotation(-50F, 30F, -35F))
                        .keyframe(leftArmBottom, k -> k.rotation(100F, 0F, 0F))
                        .keyframe(rightArm, k -> k.rotation(35F, 12F, 47F))
                        .keyframe(rightArmBottom, k -> k.rotation(10F, 0F, 0F))
                        .keyframe(item, k -> k.rotation(-82F, 0F, 0F))
                        .keyframe(bodyBottom, k -> k.rotation(-32F, 2F, -4F))
                        .keyframe(rightLeg, k -> k.rotation(57F, 2.6F, -4.2F))
                        .keyframe(rightLegBottom, k -> k.rotation(-90F, 0F, 0F))
                        .keyframe(leftLeg, k -> k.rotation(-2.5F, 0F, 2.5F))
                        .keyframe(leftLegBottom, k -> k.rotation(-17.5F, 0F, 0F))
                    .end()
                .end()
                .clip(self -> self.thurstAttackState, 12, 20, Easing.EASE_OUT_SINE, Easing.EASE_OUT_CUBIC)
                    .pose(0, 12, Easing.CEIL, AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.rotation(2.5F, 0F, 0F))
                        .keyframe(bodyTop, k -> k.rotation(0F, -17.5F, 0F))
                        .keyframe(bodyTop2, k -> k.rotation(0F, -27.5F, 0F))
                        .keyframe(head, k -> k.rotation(-22F, 42F, -9F))
                        .keyframe(leftArm, k -> k.rotation(33.5F, 12F, -18.5F))
                        .keyframe(leftArmBottom, k -> k.rotation(67F, 0F, 0F))
                        .keyframe(rightArm, k -> k.rotation(-48F, -10.5F, 57F))
                        .keyframe(rightArmBottom, k -> k.rotation(75F, 0F, 0F))
                        .keyframe(item, k -> k.rotation(-45F, 0F, 0F))
                        .keyframe(bodyBottom, k -> k.rotation(25F, 0F, 0F))
                        .keyframe(rightLeg, k -> k.rotation(32.4F, 2.6F, -4.2F))
                        .keyframe(rightLegBottom, k -> k.rotation(-90F, 0F, 0F))
                        .keyframe(leftLeg, k -> k.rotation(0F, 0F, 2.5F))
                        .keyframe(leftLegBottom, k -> k.rotation(-38F, 0F, 0F))
                    .end()
                    .pose(12, 40, Easing.EASE_OUT_EXPO.scale(0.2f), AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.rotation(1.5F, 0F, 0F))
                        .keyframe(bodyTop, k -> k.rotation(-12.5F, 35F, 0F))
                        .keyframe(bodyTop2, k -> k.rotation(0F, 7.5F, 0F))
                        .keyframe(head, k -> k.rotation(1F, -38F, 0F))
                        .keyframe(leftArm, k -> k.rotation(-21.5F, 12F, -18.5F))
                        .keyframe(leftArmBottom, k -> k.rotation(70F, 0F, 0F))
                        .keyframe(rightArm, k -> k.rotation(24F, -10.5F, 57F))
                        .keyframe(rightArmBottom, k -> k.rotation(12.5F, 0F, 0F))
                        .keyframe(item, k -> k.rotation(-60F, 0F, 0F))
                        .keyframe(bodyBottom, k -> k.rotation(-28F, 10F, -2F))
                        .keyframe(rightLeg, k -> k.rotation(57F, 2.6F, -4.2F))
                        .keyframe(rightLegBottom, k -> k.rotation(-90F, 0F, 0F))
                        .keyframe(leftLeg, k -> k.rotation(-2.5F, 0F, 2.5F))
                        .keyframe(leftLegBottom, k -> k.rotation(-17.5F, 0F, 0F))
                    .end()
                .end()
                .clip(self -> self.deathState, 10, 0, Easing.EASE_OUT_BACK, Easing.CEIL)
                    .pose(0, 10, Easing.CEIL, AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.rotation(5F, 0, 0))
                        .keyframe(bodyTop, k -> k.rotation(10, 0, 0))
                        .keyframe(head, k -> k.rotation(10, 0, 0))
                        .keyframe(leftArm, k -> k.rotation(-35, 0, -30))
                        .keyframe(leftArmBottom, k -> k.rotation(45, 0, 0))
                        .keyframe(rightArm, k -> k.rotation(-35, 0, 30))
                        .keyframe(rightArmBottom, k -> k.rotation(45, 0, 0))
                        .keyframe(bodyBottom, k -> k.rotation(-5, 0, 0))
                        .keyframe(rightLeg, k -> k.rotation(0, 0, -3))
                        .keyframe(rightLegBottom, k -> k.rotation(-23, 0, 0))
                        .keyframe(leftLeg, k -> k.rotation(-8, 0, 3))
                    .end()
                    .pose(10, 60, Easing.EASE_OUT_BOUNCE.scale(0.5f), AnimPipeline.ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.rotation(-5F, 0F, 0F))
                        .keyframe(bodyTop, k -> k.rotation(-82.5F, 0F, 0F))
                        .keyframe(bodyTop2, k -> k.rotation(12.5F, 0F, 0F))
                        .keyframe(head, k -> k.rotation(-27F, 34F, -16F))
                        .keyframe(leftArm, k -> k.rotation(47F, 55F, 5F))
                        .keyframe(leftArmBottom, k -> k.rotation(82F, 0F, 0F))
                        .keyframe(rightArm, k -> k.rotation(37F, -56F, 22F))
                        .keyframe(rightArmBottom, k -> k.rotation(62F, 0F, 0F))
                        .keyframe(item, k -> k.rotation(-40F, 0F, 0F))
                        .keyframe(bodyBottom, k -> k.rotation(-85F, -5F, 0F))
                        .keyframe(rightLeg, k -> k.rotation(0F, 0F, -5F))
                        .keyframe(rightLegBottom, k -> k.rotation(0F, 0F, 0F))
                        .keyframe(leftLeg, k -> k.rotation(0F, 0F, 2.5F))
                        .keyframe(leftLegBottom, k -> k.rotation(0F, 0F, 0F))
                    .end()
                .end();
    }
}