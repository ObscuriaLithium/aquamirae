package dev.obscuria.aquamirae.api.common.ability.variable;

import dev.obscuria.aquamirae.common.ability.VariableMapImpl;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.VariableFormatter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public interface VariableMap {

    static Builder builder() {
        return new VariableMapImpl.BuilderImpl();
    }

    float computeVariable(String name, LivingEntity self);

    Component formatDescription(Component description, LivingEntity self, boolean enableColors);

    interface Builder {

        Builder define(String name, Variable var, VariableFormatter formatter);

        VariableMap build();
    }
}
