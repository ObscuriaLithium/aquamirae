package dev.obscuria.aquamirae.api.common.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public interface AbilityCounter {

    Component format(int pieces, Style style);
}
