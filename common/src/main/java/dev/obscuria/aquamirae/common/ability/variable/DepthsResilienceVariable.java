package dev.obscuria.aquamirae.common.ability.variable;

import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.aquamirae.registry.AquamiraeAttributes;
import net.minecraft.world.entity.LivingEntity;

public record DepthsResilienceVariable(float baseValue, float maxBonus) implements Variable {

    @Override
    public float compute(LivingEntity self) {
        return baseValue() + maxBonus * ((float) self.getAttributeValue(AquamiraeAttributes.DEPTHS_RESILIENCE.holder()) - 1.0f);
    }
}