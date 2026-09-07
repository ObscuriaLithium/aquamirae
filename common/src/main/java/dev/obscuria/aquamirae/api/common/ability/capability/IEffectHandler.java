package dev.obscuria.aquamirae.api.common.ability.capability;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public interface IEffectHandler {

    default void onEffectAdded(LivingEntity entity, MobEffectInstance instance) {}

    default void onEffectRemoved(LivingEntity entity, MobEffectInstance instance) {}
}