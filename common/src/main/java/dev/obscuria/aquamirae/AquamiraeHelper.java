package dev.obscuria.aquamirae;

import dev.obscuria.aquamirae.registry.tags.AquamiraeBiomeTags;
import dev.obscuria.aquamirae.registry.tags.AquamiraeEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.Month;

public final class AquamiraeHelper {

    public static boolean isInIceMaze(LivingEntity entity) {
        return isIceMazeAt(entity.level(), entity.blockPosition());
    }

    public static boolean isIceMazeAt(LevelReader level, BlockPos pos) {
        return level.getBiome(pos).is(AquamiraeBiomeTags.ICE_MAZE);
    }

    public static boolean isAffectedByDepthsFury(@Nullable Entity entity) {
        return entity != null && entity.getType().is(AquamiraeEntityTypeTags.AFFECTED_BY_DEPTHS_FURY);
    }

    public static boolean isAffectedByDepthsResilience(@Nullable Entity entity) {
        return entity != null && entity.getType().is(AquamiraeEntityTypeTags.AFFECTED_BY_DEPTHS_RESILIENCE);
    }

    public static boolean doPoison(LivingEntity attacker, Entity entity, int duration, int amplifier) {
        if (!(entity instanceof LivingEntity living)) return false;
        if (living instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        living.addEffect(new MobEffectInstance(MobEffects.POISON, duration, amplifier));
        living.setLastHurtByMob(attacker);
        return true;
    }

    public static boolean isWinterEventActive() {
        var month = LocalDate.now().getMonth();
        return month == Month.DECEMBER || month == Month.JANUARY;
    }

    public static String formatDouble(double value) {
        return "%.3f".formatted(value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }
}