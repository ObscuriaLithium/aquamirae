package dev.obscuria.aquamirae.api.common.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.Locale;

public enum AbilityCounters implements AbilityCounter {

    PIECES,
    CHARGES;

    private final String one;
    private final String many;

    AbilityCounters() {
        var key = name().toLowerCase(Locale.ROOT);
        this.one = "ability.aquamirae.counter." + key + ".one";
        this.many = "ability.aquamirae.counter." + key + ".many";
    }

    @Override
    public Component format(int pieces, Style style) {
        return Component.translatable(pieces == 1 ? one : many, pieces).withStyle(style);
    }
}
