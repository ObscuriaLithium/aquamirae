package dev.obscuria.aquamirae.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Constants;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.client.animation.AnimPipeline;
import dev.obscuria.aquamirae.common.entity.Anglerfish;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;

public class ModelAnglerfish<T extends Anglerfish> extends AnimatedEntityModel<T> {

    public final ModelPart head, headUpper, headLower, body;
    public final ModelPart tail1, tail2, tail3, tail4, tail5, tail6;
    public final ModelPart leftFin, rightFin;
    public final ModelPart lamp1, lamp2, lamp3, lamp4, lamp5;

    public ModelAnglerfish(ModelPart root) {
        super(root, "main");
        this.head = main.getChild("head");
        this.body = main.getChild("body");
        this.headUpper = head.getChild("headTop");
        this.headLower = head.getChild("headBottom");
        this.leftFin = body.getChild("leftFin");
        this.rightFin = body.getChild("rightFin");
        this.tail1 = body.getChild("tail1");
        this.tail2 = tail1.getChild("tail2");
        this.tail3 = tail2.getChild("tail3");
        this.tail4 = tail3.getChild("tail4");
        this.tail5 = tail4.getChild("tail5");
        this.tail6 = tail5.getChild("tail6");
        this.lamp1 = headUpper.getChild("lamp1");
        this.lamp2 = lamp1.getChild("lamp2");
        this.lamp3 = lamp2.getChild("lamp3");
        this.lamp4 = lamp3.getChild("lamp4");
        this.lamp5 = lamp4.getChild("lamp5");
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        var main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        var head = main.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, 0.6109F, 0.0F, 0.0F));
        var headTop = head.addOrReplaceChild("headTop", CubeListBuilder.create().texOffs(50, 45).addBox(-7.0F, -16.0F, 0.0F, 14.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(59, 10).addBox(1.0F, -17.0F, 2.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(40, 0).addBox(-6.0F, -17.0F, 2.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 55).addBox(-7.0F, -16.0F, -8.0F, 14.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -5.0F));
        var lamp1 = headTop.addOrReplaceChild("lamp1", CubeListBuilder.create().texOffs(107, 0).addBox(-0.5F, -5.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 9.0F, -0.2182F, 0.0F, 0.0F));
        var lamp2 = lamp1.addOrReplaceChild("lamp2", CubeListBuilder.create().texOffs(107, 5).addBox(-0.5F, -5.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
        var lamp3 = lamp2.addOrReplaceChild("lamp3", CubeListBuilder.create().texOffs(107, 10).addBox(-0.5F, -5.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.5672F, 0.0F, 0.0F));
        var lamp4 = lamp3.addOrReplaceChild("lamp4", CubeListBuilder.create().texOffs(107, 15).addBox(-0.5F, -5.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.9163F, 0.0F, 0.0F));
        lamp4.addOrReplaceChild("lamp5", CubeListBuilder.create().texOffs(104, 20).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.5672F, 0.0F, 0.0F));
        head.addOrReplaceChild("headBottom", CubeListBuilder.create().texOffs(44, 16).addBox(-7.0F, -2.0F, -14.0F, 14.0F, 3.0F, 16.0F, new CubeDeformation(0.11F)).texOffs(0, 32).addBox(-7.0F, -9.0F, -14.0F, 14.0F, 7.0F, 16.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 2.0F, -5.0F));
        var body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -8.0F, -11.0F, 12.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(44, 55).addBox(0.0F, -18.0F, -11.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(44, 19).addBox(0.0F, 8.0F, -11.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        var tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(68, 73).addBox(-4.0F, -7.0F, 0.0F, 8.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(76, 27).addBox(0.0F, -17.0F, 0.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 24).addBox(0.0F, 7.0F, 0.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));
        var tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(12, 79).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(52, 0).addBox(0.0F, -15.0F, 0.0F, 0.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(40, 0).addBox(0.0F, 6.0F, 0.0F, 0.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));
        var tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 73).addBox(0.0F, -12.0F, 0.0F, 0.0F, 24.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.0F));
        var tail4 = tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(56, 75).addBox(0.0F, -8.0F, 0.0F, 0.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.0F));
        var tail5 = tail4.addOrReplaceChild("tail5", CubeListBuilder.create().texOffs(44, 75).addBox(0.0F, -8.0F, 0.0F, 0.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.0F));
        tail5.addOrReplaceChild("tail6", CubeListBuilder.create().texOffs(32, 73).addBox(0.0F, -8.0F, 0.0F, 0.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.0F));
        body.addOrReplaceChild("leftFin", CubeListBuilder.create().texOffs(0, 103).addBox(0.0F, -2.0F, -0.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 103).addBox(0.5F, -6.0F, -0.5F, 0.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, -4.5F, 0.0F, 0.5672F, 0.0F));
        body.addOrReplaceChild("rightFin", CubeListBuilder.create().texOffs(0, 103).addBox(-1.0F, -2.0F, -0.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 103).addBox(-0.5F, -6.0F, -0.5F, 0.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 0.0F, -4.5F, 0.0F, -0.6981F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer vertexConsumer,
            int packedLight, int packedOverlay, int modulate
    ) {
        poseStack.pushPose();
        poseStack.scale(1.5F, 1.5F, 1.5F);
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, modulate);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float partialTicks = AquamiraeClient.partialTicks();
        float groundMod = Mth.lerp(partialTicks, entity.clientGroundModO, entity.clientGroundMod);
        float groundModInv = 1F - groundMod;

        this.main.y = 10 * groundMod;
        this.main.zRot += (float) Math.toRadians(90f * groundMod);
        this.main.xRot += headPitch / Constants.RAD_TO_DEG * groundModInv;
        this.main.yRot += netHeadYaw / Constants.RAD_TO_DEG * groundModInv;
        this.main.xRot += netHeadYaw / Constants.RAD_TO_DEG * groundMod;
    }

    @Override // @formatter:off
    protected AnimPipeline<T> createAnimations() {
        return new AnimPipeline<T>()
                .resetParts(main, head, headUpper, headLower, body, tail1, tail2, tail3, tail4, tail5, tail6, leftFin, rightFin, lamp1, lamp2, lamp3, lamp4, lamp5)
                .ambient(AnimPipeline.ContextVar.AGE_IN_TICKS, 0.1f, AnimPipeline.Mod.idle(5), AnimPipeline.Mode.DEFINE)
                    .keyframe(main, k -> k.xRot(1F, -15F))
                    .keyframe(head, k -> k.xRot(-1F, -35F))
                    .keyframe(body, k -> k.yRot(5F, 0))
                    .keyframe(tail1, k -> k.yRot(7F, 0, -0.05f))
                    .keyframe(tail2, k -> k.yRot(9F, 0, -0.1f))
                    .keyframe(tail3, k -> k.yRot(11F, 0, -0.15f))
                    .keyframe(tail4, k -> k.yRot(13F, 0, -0.2f))
                    .keyframe(tail5, k -> k.yRot(15F, 0, -0.25f))
                    .keyframe(tail6, k -> k.yRot(15F, 0, -0.3f))
                    .keyframe(leftFin, k -> k.yRot(15F, -40F, -0.1f).zRot(0, -30F, -0.1f))
                    .keyframe(rightFin, k -> k.yRot(-15F, 40F, -0.1f).zRot(0, 30F, -0.1f))
                    .keyframe(lamp1, k -> k.xRot(2F, -5F))
                    .keyframe(lamp2, k -> k.xRot(4F, -10F, -0.05f))
                    .keyframe(lamp3, k -> k.xRot(6F, -30F, -0.1f))
                    .keyframe(lamp4, k -> k.xRot(8F, -50F, -0.15f))
                    .keyframe(lamp5, k -> k.xRot(10F, -30F, -0.2f))
                .end()
                .ambient(AnimPipeline.ContextVar.AGE_IN_TICKS, 0.7f, AnimPipeline.Mod.walk(5), AnimPipeline.Mode.ADD)
                    .keyframe(main, k -> k.xRot(1F, -15F))
                    .keyframe(head, k -> k.xRot(-1F, -35F).yRot(-10F, 0))
                    .keyframe(body, k -> k.yRot(5F, 0))
                    .keyframe(tail1, k -> k.yRot(10F, 0, -0.05f))
                    .keyframe(tail2, k -> k.yRot(12.5F, 0, -0.1f))
                    .keyframe(tail3, k -> k.yRot(15F, 0, -0.15f))
                    .keyframe(tail4, k -> k.yRot(20F, 0, -0.2f))
                    .keyframe(tail5, k -> k.yRot(25F, 0, -0.25f))
                    .keyframe(tail6, k -> k.yRot(30F, 0, -0.3f))
                    .keyframe(leftFin, k -> k.yRot(15F, -40F, -0.1f).zRot(0, -30F, -0.1f))
                    .keyframe(rightFin, k -> k.yRot(-15F, 40F, -0.1f).zRot(0, 30F, -0.1f))
                    .keyframe(lamp1, k -> k.xRot(2F, -5F))
                    .keyframe(lamp2, k -> k.xRot(4F, -10F, -0.05f))
                    .keyframe(lamp3, k -> k.xRot(6F, -30F, -0.1f))
                    .keyframe(lamp4, k -> k.xRot(8F, -50F, -0.15f))
                    .keyframe(lamp5, k -> k.xRot(10F, -30F, -0.2f))
                .end()
                .clip(it -> it.attackState, 14, 10, Easing.EASE_OUT_CUBIC, Easing.EASE_OUT_CUBIC)
                    .pose(0, 20, Easing.CEIL, AnimPipeline.ContextVar.AGE_IN_TICKS, 0.7f)
                        .keyframe(headUpper, k -> k.xRot(1F, 15F, 4, 0))
                        .keyframe(headLower, k -> k.xRot(-1F, -15F, 4, 0))
                        .keyframe(body, k -> k.yRot(10F, 0))
                        .keyframe(tail1, k -> k.yRot(15F, 0, -0.05f))
                        .keyframe(tail2, k -> k.yRot(20F, 0, -0.1f))
                        .keyframe(tail3, k -> k.yRot(25F, 0, -0.15f))
                        .keyframe(tail4, k -> k.yRot(30F, 0, -0.2f))
                        .keyframe(tail5, k -> k.yRot(35F, 0, -0.25f))
                        .keyframe(tail6, k -> k.yRot(40F, 0, -0.3f))
                    .end()
                    .pose(20, 40, Easing.EASE_OUT_CUBIC.scale(0.15f), AnimPipeline.ContextVar.AGE_IN_TICKS, 0.7f)
                        .keyframe(headUpper, k -> k.xRot(-25F))
                        .keyframe(headLower, k -> k.xRot(25F))
                    .end()
                .end();
    }
}
