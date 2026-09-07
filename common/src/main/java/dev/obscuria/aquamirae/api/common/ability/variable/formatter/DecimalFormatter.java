package dev.obscuria.aquamirae.api.common.ability.variable.formatter;

import dev.obscuria.aquamirae.AquamiraeHelper;

public final class DecimalFormatter implements VariableFormatter {

    public static final DecimalFormatter SHARED = new DecimalFormatter();

    @Override
    public String format(float value) {
        return AquamiraeHelper.formatDouble(value);
    }
}
