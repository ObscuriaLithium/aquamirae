package dev.obscuria.aquamirae.api.common.ability;

import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import lombok.Getter;
import net.minecraft.network.chat.Component;

public abstract class SimpleAbility implements Ability {

    @Getter private final Component description;
    @Getter private final VariableMap variableMap;

    protected SimpleAbility(Component description, VariableMap.Builder builder) {
        this.description = description;
        this.variableMap = builder.build();
    }
}