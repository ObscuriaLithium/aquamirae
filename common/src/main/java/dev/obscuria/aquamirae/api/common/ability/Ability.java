package dev.obscuria.aquamirae.api.common.ability;

import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public interface Ability {

    Component getDescription();

    VariableMap getVariableMap();

    default float computeVariable(String name, LivingEntity self) {
        return getVariableMap().computeVariable(name, self);
    }

    default Component getFormattedDescription(LivingEntity self, boolean enableColors) {
        return getVariableMap().formatDescription(getDescription(), self, enableColors);
    }
}