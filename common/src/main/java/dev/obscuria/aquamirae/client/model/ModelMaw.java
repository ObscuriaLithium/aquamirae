package dev.obscuria.aquamirae.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.client.animation.AnimPipeline;
import dev.obscuria.aquamirae.client.animation.AnimPipeline.ContextVar;
import dev.obscuria.aquamirae.common.entity.Maw;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelMaw<T extends Maw> extends AnimatedEntityModel<T> {

    public final ModelPart head, headUpper, headLower;
    public final ModelPart body, body2, body3;
    public final ModelPart rightFin, leftFin;

    public ModelMaw(ModelPart root) {
        super(root, "main");
        this.head = main.getChild("head");
        this.headUpper = head.getChild("head_upper");
        this.headLower = head.getChild("head_lower");
        this.body = main.getChild("body");
        this.body2 = body.getChild("body2");
        this.body3 = body2.getChild("body3");
        this.rightFin = body.getChild("right_fin");
        this.leftFin = body.getChild("left_fin");
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        var main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 150.0F));
        var head = main.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -147.0F));
        var headUpper = head.addOrReplaceChild("head_upper", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        headUpper.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(64, 66).addBox(-10.0F, 0.0F, -24.0F, 20.0F, 6.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(0, 60).addBox(-10.0F, -6.0F, -24.0F, 20.0F, 6.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4363F, 0.0F, 0.0F));
        headUpper.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(66, 19).addBox(0.01F, -14.4226F, -20.0937F, 0.0F, 8.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -3.75F, -0.4363F, 0.0F, 0.0F));
        var headLower = head.addOrReplaceChild("head_lower", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        headLower.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-10.5F, -6.0F, -24.0F, 21.0F, 6.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(0, 30).addBox(-10.5F, 0.0F, -24.0F, 21.0F, 6.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
        var body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(76, 16).addBox(-9.0F, -6.0F, 2.0F, 18.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(0, 98).addBox(0.0F, -14.0F, 2.0F, 0.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -150.0F));
        var body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 16.0F));
        body2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -11.0F, -1.0F, 0.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 90).addBox(-7.0F, -3.0F, -3.0F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
        var body3 = body2.addOrReplaceChild("body3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 12.0F));
        body3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(56, 76).addBox(0.0F, -2.0F, 5.0F, 0.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(82, 96).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        var leftFin = body.addOrReplaceChild("left_fin", CubeListBuilder.create(), PartPose.offset(9.0F, 3.0F, 10.0F));
        leftFin.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(82, 52).addBox(0.0F, -1.0F, -4.0F, 16.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));
        var rightFin = body.addOrReplaceChild("right_fin", CubeListBuilder.create(), PartPose.offset(-9.0F, 3.0F, 10.0F));
        rightFin.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(66, 0).addBox(-16.0F, -1.0F, -4.0F, 16.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        return LayerDefinition.create(meshDefinition, 256, 256);
    }

    public void translateToMouth(PoseStack pose) {
        this.main.translateAndRotate(pose);
        this.head.translateAndRotate(pose);
        this.headLower.translateAndRotate(pose);
    }

    @Override // @formatter:off
    protected AnimPipeline<T> createAnimations() {
        return new AnimPipeline<T>()
                .resetParts(main, head, headUpper, headLower, body, body2, body3, rightFin, leftFin)
                .ambientIdle(0.06f, 10f)
                    .keyframe(headUpper, k -> k.rotation(-5, 0, 0, 0, 0, 0, 2, 0))
                    .keyframe(body2, k -> k.xRot(-8, -8).yRot(-10, 0, 2, 0).zRot(4, 0))
                    .keyframe(body3, k -> k.xRot(-8, -8, -0.1f).yRot(-10, 0, 2, -0.1f).zRot(4, 0, -0.1f))
                .end()
                .ambientWalk(0.6f, 10f)
                    .keyframe(main, k -> k.zRot(-4, 0))
                    .keyframe(headUpper, k -> k.xRot(-4, 12))
                    .keyframe(body, k -> k.yRot(-12, 0))
                    .keyframe(body2, k -> k.yRot(-12, 0, -0.1f))
                    .keyframe(body3, k -> k.yRot(-12, 0, -0.2f))
                    .keyframe(rightFin, k -> k.yRot(40, 0).zRot(-12, 10, -0.3f))
                    .keyframe(leftFin, k -> k.yRot(40, 0).zRot(-12, -10, -0.3f))
                .end()
                .clip(maw -> maw.attackState, 1, 13, Easing.EASE_IN_CUBIC, Easing.EASE_OUT_CUBIC)
                    .pose(0, 2, Easing.EASE_OUT_CUBIC, ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(head, k -> k.xRot(0, -10))
                        .keyframe(headUpper, k -> k.xRot(0, 50))
                    .end()
                    .pose(2, 20, Easing.EASE_IN_QUART.scale(0.15f), ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(head, k -> k.xRot(0, 10))
                        .keyframe(headUpper, k -> k.xRot(0, -22))
                    .end()
                .end()
                .clip(maw -> maw.deathState, 6, 0, Easing.EASE_OUT_CIRCLE, Easing.CEIL)
                    .pose(0, 6, Easing.CEIL, ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.xRot(8))
                        .keyframe(head, k -> k.xRot(-50))
                        .keyframe(body2, k -> k.xRot(20))
                        .keyframe(body3, k -> k.xRot(40))
                        .keyframe(headUpper, k -> k.xRot(50))
                    .end()
                    .pose(6, 40, Easing.EASE_OUT_BOUNCE.scale(0.6f), ContextVar.AGE_IN_TICKS, 1f)
                        .keyframe(main, k -> k.xRot(0))
                        .keyframe(head, k -> k.xRot(10))
                        .keyframe(body2, k -> k.xRot(0))
                        .keyframe(body3, k -> k.xRot(0))
                        .keyframe(headUpper, k -> k.xRot(-22))
                    .end()
                .end();
    }
}
