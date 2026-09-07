package dev.obscuria.aquamirae.api.common.ability.variable.formatter;

import net.minecraft.network.chat.Component;

public final class PotencyFormatter implements VariableFormatter {

    public static final PotencyFormatter SHARED = new PotencyFormatter();

    @Override
    public String format(float value) {
        return Component.translatable("potion.potency." + Math.round(value)).getString();
    }
}
