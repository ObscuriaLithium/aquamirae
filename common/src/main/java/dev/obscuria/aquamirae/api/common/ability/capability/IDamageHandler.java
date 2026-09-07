package dev.obscuria.aquamirae.api.common.ability.capability;

import dev.obscuria.aquamirae.integration.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface IDamageHandler {

    default void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {}

    default void modifyIncomingDamage(LivingEntity self, DamageSource source, DamageContext damage) {}

    default void onDamageDealt(LivingEntity self, LivingEntity victim, DamageSource source) {}

    default void onDamageTaken(LivingEntity self, DamageSource source) {}
}