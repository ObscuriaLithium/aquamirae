package dev.obscuria.aquamirae.api.common.ability.variable;

import net.minecraft.world.entity.LivingEntity;

public record Constant(float baseValue, boolean ignoreFraction) implements Variable {

    public Constant(float baseValue) {
        this(baseValue, false);
    }

    @Override
    public float compute(LivingEntity self) {
        return baseValue();
    }
}