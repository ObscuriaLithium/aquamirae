package dev.obscuria.aquamirae.api.common.ability.variable;

import net.minecraft.world.entity.LivingEntity;

public interface Variable {
    
    float baseValue();

    float compute(LivingEntity self);

    default boolean ignoreFraction() {
        return false;
    }

    default ValueState classify(float actualValue) {
        if (ignoreFraction())
            actualValue = Math.round(actualValue);
        return actualValue < baseValue()
                ? ValueState.DECREASED
                : actualValue > baseValue()
                  ? ValueState.INCREASED
                  : ValueState.UNCHANGED;
    }
}
