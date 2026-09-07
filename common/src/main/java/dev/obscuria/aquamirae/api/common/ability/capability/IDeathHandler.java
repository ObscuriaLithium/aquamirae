package dev.obscuria.aquamirae.api.common.ability.capability;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface IDeathHandler {

    default boolean cancelDeath(LivingEntity entity, DamageSource source) {
        return false;
    }
}