package dev.obscuria.aquamirae.common.ability.variable;

import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.aquamirae.registry.AquamiraeAttributes;
import dev.obscuria.fragmentum.config.ConfigValue;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public record ConfigDepthsFuryVariable(
        ConfigValue<Double> minValue,
        ConfigValue<Double> maxValue
) implements Variable {

    @Override
    public float baseValue() {
        return minValue.get().floatValue();
    }

    @Override
    public float compute(LivingEntity self) {
        var depthsFury = (float) self.getAttributeValue(AquamiraeAttributes.DEPTHS_FURY.holder()) - 1.0f;
        return Mth.lerp(depthsFury, minValue.get().floatValue(), maxValue.get().floatValue());
    }
}
