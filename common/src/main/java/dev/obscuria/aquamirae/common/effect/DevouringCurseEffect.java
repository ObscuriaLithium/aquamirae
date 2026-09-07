package dev.obscuria.aquamirae.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DevouringCurseEffect extends MobEffect {

    public DevouringCurseEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player)) return false;
        if (player.getFoodData().getFoodLevel() > 0) return false;
        player.hurt(entity.damageSources().starve(), 2);
        return true;
    }
}
