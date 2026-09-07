package dev.obscuria.aquamirae.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.client.animation.AnimPipeline;
import dev.obscuria.aquamirae.common.entity.MazeMother;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelMazeMother<T extends MazeMother> extends AnimatedEntityModel<T> {

    public final ModelPart root2, bodyTop, bodyBottom;
    public final ModelPart tail1, tail2, tail3, tail4, tail5;
    public final ModelPart jaw1, jaw2, jaw3, jaw4;
    public final ModelPart wing1LeftTop, wing1LeftBottom;
    public final ModelPart wing1RightTop, wing1RightBottom;
    public final ModelPart wing2LeftTop, wing2LeftBottom;
    public final ModelPart wing2RightTop, wing2RightBottom;

    public ModelMazeMother(ModelPart root) {
        super(root, "main");
        this.root2 = main.getChild("main2");
        this.bodyTop = root2.getChild("body_top");
        this.bodyBottom = root2.getChild("body_bottom");
        this.jaw1 = bodyTop.getChild("jaw1");
        this.jaw2 = bodyTop.getChild("jaw2");
        this.jaw3 = bodyTop.getChild("jaw3");
        this.jaw4 = bodyTop.getChild("jaw4");
        this.tail1 = bodyBottom.getChild("tail1");
        this.tail2 = tail1.getChild("tail2");
        this.tail3 = tail2.getChild("tail3");
        this.tail4 = tail3.getChild("tail4");
        this.tail5 = tail4.getChild("tail5");
        this.wing1LeftTop = bodyTop.getChild("left_wing1_top");
        this.wing1RightTop = bodyTop.getChild("right_wing1_top");
        this.wing2LeftTop = wing1LeftTop.getChild("left_wing2_top");
        this.wing2RightTop = wing1RightTop.getChild("right_wing2_top");
        this.wing1LeftBottom = bodyBottom.getChild("left_wing1_bottom");
        this.wing1RightBottom = bodyBottom.getChild("right_wing1_bottom");
        this.wing2LeftBottom = wing1LeftBottom.getChild("left_wing2_bottom");
        this.wing2RightBottom = wing1RightBottom.getChild("right_wing2_bottom");
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        var main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -126.0F, 0.0F));
        var main2 = main.addOrReplaceChild("main2", CubeListBuilder.create(), PartPose.offset(0.0F, 147.0F, 0.0F));
        var body_top = main2.addOrReplaceChild("body_top", CubeListBuilder.create().texOffs(0, 45).addBox(-8.0F, -1.5F, -16.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        body_top.addOrReplaceChild("jaw1", CubeListBuilder.create().texOffs(37, 26).addBox(-3.0F, 0.0F, -10.5F, 6.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 0.0F, -14.5F));
        body_top.addOrReplaceChild("jaw2", CubeListBuilder.create().texOffs(49, 26).addBox(-3.0F, 0.0F, -10.5F, 6.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 0.0F, -14.5F));
        body_top.addOrReplaceChild("jaw3", CubeListBuilder.create().texOffs(65, 26).addBox(-1.5F, 0.0F, -5.5F, 3.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 0.0F, -15.5F));
        body_top.addOrReplaceChild("jaw4", CubeListBuilder.create().texOffs(73, 26).addBox(-1.5F, 0.0F, -5.5F, 3.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 0.0F, -15.5F));
        var crystal1_top = body_top.addOrReplaceChild("crystal1_top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -16.0F));
        crystal1_top.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 58).addBox(-2.0F, -10.5F, -16.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 16.0F, 0.0F, 0.0F, -0.7854F));
        var crystal2_top = body_top.addOrReplaceChild("crystal2_top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -16.0F));
        crystal2_top.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(64, 45).addBox(-1.0F, -9.5F, -16.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 16.0F, 0.0F, 0.0F, -0.3491F));
        var crystal3_top = body_top.addOrReplaceChild("crystal3_top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -16.0F));
        crystal3_top.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(64, 65).addBox(1.0F, -9.5F, -16.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 16.0F, 0.0F, 0.0F, 0.3491F));
        var crystal4_top = body_top.addOrReplaceChild("crystal4_top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -16.0F));
        crystal4_top.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 48).addBox(2.0F, -10.5F, -16.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 16.0F, 0.0F, 0.0F, 0.7854F));
        var left_wing1_top = body_top.addOrReplaceChild("left_wing1_top", CubeListBuilder.create().texOffs(48, 45).addBox(-9.0F, -1.0F, -14.0F, 9.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, 0.0F));
        left_wing1_top.addOrReplaceChild("left_wing2_top", CubeListBuilder.create().texOffs(92, 0).addBox(-5.0F, -1.0F, -11.0F, 5.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 0.0F, 0.0F));
        var right_wing1_top = body_top.addOrReplaceChild("right_wing1_top", CubeListBuilder.create().texOffs(60, 0).addBox(0.0F, -1.0F, -14.0F, 9.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 0.0F));
        right_wing1_top.addOrReplaceChild("right_wing2_top", CubeListBuilder.create().texOffs(47, 91).addBox(0.0F, -1.0F, -11.0F, 5.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 0.0F, 0.0F));
        var body_bottom = main2.addOrReplaceChild("body_bottom", CubeListBuilder.create().texOffs(0, 26).addBox(-8.0F, 0.5F, 0.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(72, 26).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
        var crystal1_bottom = body_bottom.addOrReplaceChild("crystal1_bottom", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));
        crystal1_bottom.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -10.5F, 0.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
        var crystal2_bottom = body_bottom.addOrReplaceChild("crystal2_bottom", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));
        crystal2_bottom.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(60, 0).addBox(-1.0F, -9.5F, 0.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.3491F));
        var crystal3_bottom = body_bottom.addOrReplaceChild("crystal3_bottom", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));
        crystal3_bottom.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(64, 55).addBox(1.0F, -9.5F, 0.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.3491F));
        var crystal4_bottom = body_bottom.addOrReplaceChild("crystal4_bottom", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));
        crystal4_bottom.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 48).addBox(2.0F, -10.5F, 0.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        var left_wing1_bottom = body_bottom.addOrReplaceChild("left_wing1_bottom", CubeListBuilder.create().texOffs(80, 45).addBox(-9.0F, -1.0F, 0.0F, 9.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 2.0F, 0.0F));
        left_wing1_bottom.addOrReplaceChild("left_wing2_bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 0.0F, 0.0F));
        var right_wing1_bottom = body_bottom.addOrReplaceChild("right_wing1_bottom", CubeListBuilder.create().texOffs(20, 84).addBox(0.0F, -1.0F, 0.0F, 9.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 2.0F, 0.0F));
        right_wing1_bottom.addOrReplaceChild("right_wing2_bottom", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 0.0F, 0.0F));
        var tail1 = body_bottom.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(96, 57).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 16.0F));
        var tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(98, 69).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));
        var tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(0, 87).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(22, 22).addBox(-8.5F, 0.0F, 5.0F, 17.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));
        var tail4 = tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(2, 98).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(19, 15).addBox(-8.5F, 0.0F, 0.0F, 17.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));
        tail4.addOrReplaceChild("tail5", CubeListBuilder.create().texOffs(11, 0).addBox(-8.5F, 0.0F, 0.0F, 17.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(0, 14).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer vertexConsumer,
            int packedLight, int packedOverlay, int modulate
    ) {
        poseStack.pushPose();
        poseStack.scale(5f, 5f, 5f);
        poseStack.translate(0F, -1F, 0F);
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, modulate);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        var verticalMotion = entity.getVerticalMotion(AquamiraeClient.partialTicks());
        this.tail1.xRot -= verticalMotion * 0.2f;
        this.tail2.xRot -= verticalMotion * 0.4f;
        this.tail3.xRot -= verticalMotion * 0.6f;
        this.tail4.xRot -= verticalMotion * 0.8f;
        this.tail5.xRot -= verticalMotion * 1.0f;
        this.wing1RightBottom.zRot = this.wing1RightTop.zRot;
        this.wing1LeftBottom.zRot = this.wing1LeftTop.zRot;
        this.wing2RightBottom.zRot = this.wing2RightTop.zRot;
        this.wing2LeftBottom.zRot = this.wing2LeftTop.zRot;
//        HekateLib.math.i(jaw1, 0, 0, 10F, 10F, 0, 0, 0.4f, 0F, ageInTicks, HekateLib.math.cycle(ageInTicks, 0.02F, -1F));
//        HekateLib.math.i(jaw2, 0, 0, -10F, -10F, 0, 0, 0.4f, 0F, ageInTicks, HekateLib.math.cycle(ageInTicks, 0.02F, -1F));
//        HekateLib.math.i(jaw3, 0, 0, 15F, 5F, 0, 0, 0.6f, 0F, ageInTicks, HekateLib.math.cycle(ageInTicks, 0.02F, 0F));
//        HekateLib.math.i(jaw4, 0, 0, -15F, -5F, 0, 0, 0.6f, 0F, ageInTicks, HekateLib.math.cycle(ageInTicks, 0.02F, 0F));
    }

    @Override // @formatter:off
	protected AnimPipeline<T> createAnimations() {
		return new AnimPipeline<T>()
				.resetParts(
						main, root2, bodyTop, bodyBottom,
						tail1, tail2, tail3, tail4, tail5,
						wing1LeftTop, wing1LeftBottom,
						wing1RightTop, wing1RightBottom,
						wing2LeftTop, wing2LeftBottom,
						wing2RightTop, wing2RightBottom,
						jaw1, jaw2, jaw3, jaw4)
				.ambient(AnimPipeline.ContextVar.AGE_IN_TICKS, 0.1f, AnimPipeline.Mod.ALWAYS, AnimPipeline.Mode.DEFINE)
                    .keyframe(bodyTop, k -> k.xRot(-10F, 0))
                    .keyframe(bodyBottom, k -> k.xRot(-10F, 0, -0.1f))
                    .keyframe(tail1, k -> k.xRot(-10F, 0, -0.2f))
                    .keyframe(tail2, k -> k.xRot(-10F, 0, -0.3f))
                    .keyframe(tail3, k -> k.xRot(-10F, 0, -0.4f))
                    .keyframe(tail4, k -> k.xRot(-10F, 0, -0.5f))
                    .keyframe(tail5, k -> k.xRot(-10F, 0, -0.6f))
                    .keyframe(wing1RightTop, k -> k.zRot(15F, 0, -0.1f))
                    .keyframe(wing1LeftTop, k -> k.zRot(-15F, 0, -0.1f))
                    .keyframe(wing2RightTop, k -> k.zRot(15F, 0, -0.2f))
                    .keyframe(wing2LeftTop, k -> k.zRot(-15F, 0, -0.2f))
                .end();
	}
}
