package dev.obscuria.aquamirae.integration.hook.server;

import dev.obscuria.aquamirae.api.common.PlayerHelper;
import dev.obscuria.aquamirae.integration.hook.HookLivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public final class HookServerPlayer {

    public static void restoreFrom(ServerPlayer self, ServerPlayer from, boolean keepEverything) {
        PlayerHelper.setRemnantSaberBonus(self, PlayerHelper.getRemnantSaberBonus(from));
    }

    public static boolean cancelDeath(ServerPlayer self, DamageSource source) {
        return HookLivingEntity.cancelDeath(self, source);
    }
}
