package dev.obscuria.aquamirae.client.model;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.client.animation.AnimPipeline;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public abstract class AnimatedEntityModel<T extends LivingEntity> extends EntityModel<T> {

    protected final ModelPart main;
    protected final Supplier<AnimPipeline<T>> animations;

    protected AnimatedEntityModel(ModelPart root, String rootName) {
        this.main = root.getChild(rootName);
        this.animations = Suppliers.memoize(this::createAnimations);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch
    ) {
        var context = new AnimPipeline.Context(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.animations.get().evaluate(entity, context);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer vertexConsumer,
            int packedLight, int packedOverlay, int modulate
    ) {
        this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay, modulate);
    }

    protected abstract AnimPipeline<T> createAnimations();
}
