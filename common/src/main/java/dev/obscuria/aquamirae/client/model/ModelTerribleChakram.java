package dev.obscuria.aquamirae.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.common.entity.projectile.TerribleChakram;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelTerribleChakram<T extends TerribleChakram> extends EntityModel<T> {

	public final ModelPart main;

	public ModelTerribleChakram(ModelPart root) {
		this.main = root.getChild("chakram");
	}

	public static LayerDefinition createBodyLayer() {
		var meshDefinition = new MeshDefinition();
		var partDefinition = meshDefinition.getRoot();

		var chakram = partDefinition.addOrReplaceChild("chakram", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		chakram.addOrReplaceChild("part1", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -2.0F, -8.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 3).addBox(-7.0F, -2.0F, -7.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 6).addBox(-5.0F, -2.0F, -6.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 9).addBox(-4.0F, -2.0F, -5.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 12).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 15).addBox(-2.0F, -2.0F, -3.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 18).addBox(-1.0F, -2.0F, -2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 21).addBox(-1.0F, -2.0F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 24).addBox(-2.0F, -2.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 27).addBox(-2.0F, -2.0F, 1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 30).addBox(-3.0F, -2.0F, 2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 33).addBox(-3.0F, -2.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 36).addBox(-4.0F, -2.0F, 4.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 39).addBox(-5.0F, -2.0F, 5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 42).addBox(-4.0F, -2.0F, 6.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 0.5F, -5.5F));
		chakram.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -2.0F, -8.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 3).addBox(-7.0F, -2.0F, -7.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 6).addBox(-5.0F, -2.0F, -6.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 9).addBox(-4.0F, -2.0F, -5.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 12).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 15).addBox(-2.0F, -2.0F, -3.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 18).addBox(-1.0F, -2.0F, -2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 21).addBox(-1.0F, -2.0F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 24).addBox(-2.0F, -2.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 27).addBox(-2.0F, -2.0F, 1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 30).addBox(-3.0F, -2.0F, 2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 33).addBox(-3.0F, -2.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 36).addBox(-4.0F, -2.0F, 4.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 39).addBox(-5.0F, -2.0F, 5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 42).addBox(-4.0F, -2.0F, 6.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 0.5F, 5.5F, 0.0F, 1.5708F, 0.0F));
		chakram.addOrReplaceChild("part3", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -2.0F, -8.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 3).addBox(-7.0F, -2.0F, -7.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 6).addBox(-5.0F, -2.0F, -6.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 9).addBox(-4.0F, -2.0F, -5.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 12).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 15).addBox(-2.0F, -2.0F, -3.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 18).addBox(-1.0F, -2.0F, -2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 21).addBox(-1.0F, -2.0F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 24).addBox(-2.0F, -2.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 27).addBox(-2.0F, -2.0F, 1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 30).addBox(-3.0F, -2.0F, 2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 33).addBox(-3.0F, -2.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 36).addBox(-4.0F, -2.0F, 4.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 39).addBox(-5.0F, -2.0F, 5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 42).addBox(-4.0F, -2.0F, 6.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.5F, 5.5F, -3.1416F, 0.0F, 3.1416F));
		chakram.addOrReplaceChild("part4", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -2.0F, -8.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 3).addBox(-7.0F, -2.0F, -7.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 6).addBox(-5.0F, -2.0F, -6.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 9).addBox(-4.0F, -2.0F, -5.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 12).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 15).addBox(-2.0F, -2.0F, -3.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 18).addBox(-1.0F, -2.0F, -2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 21).addBox(-1.0F, -2.0F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 24).addBox(-2.0F, -2.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 27).addBox(-2.0F, -2.0F, 1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 30).addBox(-3.0F, -2.0F, 2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 33).addBox(-3.0F, -2.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 36).addBox(-4.0F, -2.0F, 4.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 39).addBox(-5.0F, -2.0F, 5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 42).addBox(-4.0F, -2.0F, 6.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.5F, -5.5F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(
			PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, int color
	) {
		this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public void setupAnim(
			T entity, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch
	) {
		this.main.yRot = ageInTicks * 0.8F;
	}
}
