package dev.obscuria.aquamirae.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.renderer.spline.BodyDecoration;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ModelShipbreakerHead extends BodyDecoration {

	private static final ResourceLocation TEXTURE = Aquamirae.id("textures/entity/shipbreaker_head.png");
	private static final ResourceLocation OVERLAY_TEXTURE = Aquamirae.id("textures/entity/shipbreaker_head_overlay.png");

	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart jaw1;
	private final ModelPart bone3;
	private final ModelPart jaw2;
	private final ModelPart bone4;
	private final ModelPart bone7;
	private final ModelPart jaw3;
	private final ModelPart bone5;
	private final ModelPart bone9;
	private final ModelPart jaw4;
	private final ModelPart bone6;
	private final ModelPart bone8;

	public ModelShipbreakerHead(ModelPart root) {
        super(root, RenderType::entityCutoutNoCull);
        this.bone = root.getChild("bone");
		this.bone2 = this.bone.getChild("bone2");
		this.jaw1 = this.bone.getChild("jaw1");
		this.bone3 = this.jaw1.getChild("bone3");
		this.jaw2 = this.bone.getChild("jaw2");
		this.bone4 = this.jaw2.getChild("bone4");
		this.bone7 = this.bone4.getChild("bone7");
		this.jaw3 = this.bone.getChild("jaw3");
		this.bone5 = this.jaw3.getChild("bone5");
		this.bone9 = this.bone5.getChild("bone9");
		this.jaw4 = this.bone.getChild("jaw4");
		this.bone6 = this.jaw4.getChild("bone6");
		this.bone8 = this.bone6.getChild("bone8");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -11.0F, -1.0F, 22.0F, 22.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(0, 33).addBox(-7.0F, -7.0F, 10.0F, 14.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition jaw1 = bone.addOrReplaceChild("jaw1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone3 = jaw1.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(66, 0).addBox(-7.0F, -5.0F, 0.0F, 12.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(66, 18).addBox(-6.0F, -4.0F, -7.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(66, 18).addBox(-6.0F, -4.0F, -6.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(-1.0F))
				.texOffs(0, 53).addBox(-6.0F, -4.0F, 6.0F, 10.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -2.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition jaw2 = bone.addOrReplaceChild("jaw2", CubeListBuilder.create(), PartPose.offsetAndRotation(8.55F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition bone4 = jaw2.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 0.0F, -2.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bone7 = bone4.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(66, 0).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 53).addBox(-5.0F, -5.0F, 6.0F, 10.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(66, 18).addBox(-5.0F, -5.0F, -7.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(66, 18).addBox(-5.0F, -5.0F, -6.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(5.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition jaw3 = bone.addOrReplaceChild("jaw3", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.4F, 8.05F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone5 = jaw3.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -4.0F, -2.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bone9 = bone5.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(66, 0).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 53).addBox(-5.0F, -5.0F, 6.0F, 10.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(66, 18).addBox(-5.0F, -5.0F, -7.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(66, 18).addBox(-5.0F, -5.0F, -6.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(-5.0F, 7.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition jaw4 = bone.addOrReplaceChild("jaw4", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.65F, 0.0F, 1.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition bone6 = jaw4.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offsetAndRotation(-11.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bone8 = bone6.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(66, 0).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 53).addBox(-5.0F, -5.0F, 6.0F, 10.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(66, 18).addBox(-5.0F, -5.0F, -7.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(66, 18).addBox(-5.0F, -5.0F, -6.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(5.0F, 5.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void render(Context context, PoseStack pose, MultiBufferSource buffer, int packedOverlay, int packedLight, float partialTicks) {
		float rotation = (float) Mth.map(
				context.ring().getOrientation().m21(), -1f, 1f,
				Math.toRadians(-80f), Math.toRadians(-30f));
		jaw1.xRot = -rotation;
		jaw2.yRot = -rotation;
		jaw3.xRot = rotation;
		jaw4.yRot = rotation;

		pose.pushPose();
		pose.translate(0f, -18f, 0);
		super.render(context, pose, buffer, packedOverlay, packedLight, partialTicks);
		pose.popPose();
	}

	@Override
	public ResourceLocation getTexture() {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getOverlayTexture() {
		return OVERLAY_TEXTURE;
	}

	@Override
	protected float resolveScale(Context context) {
		return 13f;
	}
}