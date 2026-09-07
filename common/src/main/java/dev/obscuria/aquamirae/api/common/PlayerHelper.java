package dev.obscuria.aquamirae.api.common;

import dev.obscuria.aquamirae.integration.extension.ExtensionPlayer;
import net.minecraft.world.entity.player.Player;

public final class PlayerHelper {

    public static float getRemnantSaberBonus(Player player) {
        return ((ExtensionPlayer) player).aquamirae$getRemnantSaberBonus();
    }

    public static void setRemnantSaberBonus(Player player, float bonus) {
        ((ExtensionPlayer) player).aquamirae$setRemnantSaberBonus(bonus);
    }

    public static void addRemnantSaberBonus(Player player, float bonus) {
        setRemnantSaberBonus(player, getRemnantSaberBonus(player) + bonus);
    }
}
