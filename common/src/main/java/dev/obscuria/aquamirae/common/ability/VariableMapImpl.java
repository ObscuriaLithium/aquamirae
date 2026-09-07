package dev.obscuria.aquamirae.common.ability;

import com.google.common.collect.ImmutableMap;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.VariableFormatter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class VariableMapImpl implements VariableMap {

    private final ImmutableMap<String, Variable> variables;
    private final ImmutableMap<String, VariableFormatter> formatters;

    public VariableMapImpl(BuilderImpl builder) {
        this.variables = builder.buildVariables();
        this.formatters = builder.buildFormatters();
    }

    @Override
    public float computeVariable(String name, LivingEntity self) {
        @Nullable var var = variables.get(name);
        return var == null ? 0.0f : var.compute(self);
    }

    @Override
    public Component formatDescription(Component description, LivingEntity self, boolean enableColors) {
        var result = description.getString();
        for (var entry : variables.entrySet()) {
            var variable = entry.getValue();
            var formatter = Objects.requireNonNull(formatters.get(entry.getKey()));
            var value = variable.compute(self);
            var formattedValue = formatter.format(value);
            if (enableColors) formattedValue = variable.classify(value).wrap(formattedValue);
            result = result.replace("{" + entry.getKey() + "}", formattedValue);
        }
        return Component.literal(result);
    }

    public static class BuilderImpl implements Builder {

        private final Map<String, Variable> variables = new LinkedHashMap<>();
        private final Map<String, VariableFormatter> formatters = new LinkedHashMap<>();

        @Override
        public Builder define(String name, Variable var, VariableFormatter formatter) {
            this.variables.put(name, var);
            this.formatters.put(name, formatter);
            return this;
        }

        @Override
        public VariableMap build() {
            return new VariableMapImpl(this);
        }

        private ImmutableMap<String, Variable> buildVariables() {
            return ImmutableMap.<String, Variable>builder().putAll(variables).build();
        }

        private ImmutableMap<String, VariableFormatter> buildFormatters() {
            return ImmutableMap.<String, VariableFormatter>builder().putAll(formatters).build();
        }
    }
}
