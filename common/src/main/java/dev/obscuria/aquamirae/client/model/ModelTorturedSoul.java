package dev.obscuria.aquamirae.client.model;

import dev.obscuria.aquamirae.client.animation.AnimPipeline;
import dev.obscuria.aquamirae.common.entity.TorturedSoul;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelTorturedSoul<T extends TorturedSoul> extends AnimatedEntityModel<T> {

    public final ModelPart body, heart, head, nose;
    public final ModelPart leftArm, rightArm, leftArmLower, rightArmLower;
    public final ModelPart leftLeg, rightLeg, leftLegLower, rightLegLower;

    public ModelTorturedSoul(ModelPart root) {
        super(root, "main");
        this.body = main.getChild("body");
        this.heart = body.getChild("heart");
        this.head = body.getChild("head");
        this.nose = head.getChild("nose");
        this.leftArm = body.getChild("left_arm");
        this.rightArm = body.getChild("right_arm");
        this.leftLeg = main.getChild("left_leg");
        this.rightLeg = main.getChild("right_leg");
        this.leftArmLower = leftArm.getChild("left_arm_bottom");
        this.rightArmLower = rightArm.getChild("right_arm_bottom");
        this.leftLegLower = leftLeg.getChild("left_leg_bottom");
        this.rightLegLower = rightLeg.getChild("right_leg_bottom");
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var definitionRoot = meshDefinition.getRoot();

        var main = definitionRoot.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 150.0F));
        var body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(-0.02F)).texOffs(0, 39).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 6.0F, -150.0F));
        body.addOrReplaceChild("heart", CubeListBuilder.create().texOffs(44, 21).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));
        var head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(28, 39).addBox(-4.5F, -13.0F, -4.5F, 9.0F, 10.0F, 9.0F, new CubeDeformation(-0.4F)), PartPose.offset(0.0F, -12.0F, 0.0F));
        head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));
        var left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(38, 0).mirror().addBox(0.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(44, 30).mirror().addBox(0.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(4.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        left_arm.addOrReplaceChild("left_arm_bottom", CubeListBuilder.create().texOffs(38, 11).mirror().addBox(-2.0F, 0.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(2.0F, 4.0F, 1.0F));
        var right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(38, 0).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(44, 30).addBox(-4.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(-4.0F, -10.0F, 0.0F));
        right_arm.addOrReplaceChild("right_arm_bottom", CubeListBuilder.create().texOffs(38, 11).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 4.0F, 0.0F));
        var left_leg = main.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 6.0F, -150.0F));
        left_leg.addOrReplaceChild("left_leg_bottom", CubeListBuilder.create().texOffs(0, 29).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(0.0F, 6.0F, 0.0F));
        var right_leg = main.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 6.0F, -150.0F));
        right_leg.addOrReplaceChild("right_leg_bottom", CubeListBuilder.create().texOffs(0, 29).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.heart.xRot = ageInTicks / 13f;
        this.heart.yRot = ageInTicks / 9f;
        this.heart.zRot = ageInTicks / 5f;
        this.head.yRot += (float) Math.toRadians(netHeadYaw) * 0.5f;
        this.body.yRot += (float) Math.toRadians(netHeadYaw) * 0.5f;
    }

    @Override // @formatter:off
    protected AnimPipeline<T> createAnimations() {
        return new AnimPipeline<T>()
                .resetParts(
                        main, body, heart, head, nose,
                        leftArm, rightArm, leftArmLower, rightArmLower,
                        leftLeg, rightLeg, leftLegLower, rightLegLower)
                .ambientIdle(0.1f, 5f)
                    .keyframe(main, k -> k.xRot(-0.3F, -0.5F))
                    .keyframe(body, k -> k.xRot(-3F, -10F))
                    .keyframe(head, k -> k.xRot(3F, 16F))
                    .keyframe(rightArm, k -> k.xRot(12F, 35F, -0.2f).zRot(-3F, 10F, -0.2f))
                    .keyframe(rightArmLower, k -> k.xRot(18F, 69F, -0.4f))
                    .keyframe(leftArm, k -> k.xRot(10F, 15F, -0.2f).zRot(3F, -10F, -0.2f))
                    .keyframe(leftArmLower, k -> k.xRot(15F, 30F, -0.4f))
                    .keyframe(rightLeg, k -> k.rotation(15F, 30F, -0.5F, -4F, 0.5F, 4F))
                    .keyframe(rightLegLower, k -> k.xRot(-30F, -44F))
                    .keyframe(leftLeg, k -> k.rotation(15F, 30F, -0.5F, 4F, 0.5F, -4F))
                    .keyframe(leftLegLower, k -> k.xRot(-30F, -44F))
                .end()
                .ambientWalk(0.4f, 5f)
                    .keyframe(main, k -> k.xRot(0.5F, -0.3F, 2, 0))
                    .keyframe(body, k -> k.xRot(-3F, -3F, 2, -0.1f))
                    .keyframe(head, k -> k.xRot(-3F, -3F, 2, -0.2f))
                    .keyframe(rightArm, k -> k.xRot(24F, -12F, -0.1f).zRot(-3F, 10F, -0.1f))
                    .keyframe(rightArmLower, k -> k.xRot(20F, 34F, -0.2f))
                    .keyframe(leftArm, k -> k.xRot(-24F, -12F, -0.1f).zRot(3F, -10F, -0.1f))
                    .keyframe(leftArmLower, k -> k.xRot(-20F, 34F, -0.2f))
                    .keyframe(rightLeg, k -> k.xRot(-30F, 14F, -0.1f))
                    .keyframe(rightLegLower, k -> k.xRot(-34F, -34F, -0.2f))
                    .keyframe(leftLeg, k -> k.xRot(30F, 14F, -0.1f))
                    .keyframe(leftLegLower, k -> k.xRot(34F, -34F, -0.2f))
                .end()
                .clip(self -> self.attackState, 8, 12, Easing.EASE_OUT_ELASTIC, Easing.EASE_OUT_BACK)
                    .pose(0, 20, Easing.CEIL)
                        .keyframe(body, k -> k.rotation(-27F, 0, 0))
                        .keyframe(leftArm, k -> k.rotation(125F, 0, 0))
                        .keyframe(rightArm, k -> k.rotation(125F, 0, 0))
                        .keyframe(leftArmLower, k -> k.rotation(12F, 0, 0))
                        .keyframe(rightArmLower, k -> k.rotation(12F, 0, 0))
                    .end()
                .end();

    }
}
