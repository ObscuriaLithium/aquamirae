package dev.obscuria.aquamirae.common.ability.variable;

import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.aquamirae.registry.AquamiraeAttributes;
import net.minecraft.world.entity.LivingEntity;

public record DepthsFuryVariable(float baseValue, float maxBonus, boolean ignoreFraction) implements Variable {

    public DepthsFuryVariable(float baseValue, float maxBonus) {
        this(baseValue, maxBonus, false);
    }

    @Override
    public float compute(LivingEntity self) {
        return baseValue() + maxBonus * ((float) self.getAttributeValue(AquamiraeAttributes.DEPTHS_FURY.holder()) - 1.0f);
    }
}
