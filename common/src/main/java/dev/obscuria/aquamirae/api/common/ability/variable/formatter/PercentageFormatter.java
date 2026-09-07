package dev.obscuria.aquamirae.api.common.ability.variable.formatter;

import dev.obscuria.aquamirae.AquamiraeHelper;

public final class PercentageFormatter implements VariableFormatter {

    public static final PercentageFormatter SHARED = new PercentageFormatter();

    @Override
    public String format(float value) {
        return AquamiraeHelper.formatDouble(value * 100.0) + "%";
    }
}
