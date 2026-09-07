package dev.obscuria.aquamirae.common.ability.variable;

import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.fragmentum.config.ConfigValue;
import net.minecraft.world.entity.LivingEntity;

public record ConfigVariable(ConfigValue<Double> configValue) implements Variable {

    @Override
    public float baseValue() {
        return configValue.get().floatValue();
    }

    @Override
    public float compute(LivingEntity self) {
        return baseValue();
    }
}
