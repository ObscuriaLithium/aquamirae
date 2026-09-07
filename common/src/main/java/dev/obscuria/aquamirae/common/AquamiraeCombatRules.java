package dev.obscuria.aquamirae.common;

import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.integration.DamageContext;
import dev.obscuria.aquamirae.registry.AquamiraeAttributes;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.aquamirae.registry.AquamiraeEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class AquamiraeCombatRules {

    public static void modifyDamageByDepthsResilience(LivingEntity victim, DamageSource source, DamageContext context) {
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;
        var victimResilience = victim.getAttributeValue(AquamiraeAttributes.DEPTHS_RESILIENCE.holder());
        var critChance = Mth.clampedMap(victimResilience, 1.0, 2.0, 0.8, 0.0);

        if (attacker.getRandom().nextDouble() >= critChance) return;
        var critMultiplier = Mth.clampedMap(victimResilience, 1.0, 2.0, 3.0, 1.0);
        context.multiply((float) critMultiplier);
        AbilityHelper.animateCritEffect(attacker, victim);
    }

    public static float modifyArmorByDepthsResilience(LivingEntity victim, DamageSource source, float amount) {
        var victimResilience = victim.getAttributeValue(AquamiraeAttributes.DEPTHS_RESILIENCE.holder());
        var modifier = Mth.clampedMap(victimResilience, 1.0, 2.0, 0.2, 1.0);
        return amount * (float) modifier;
    }

    public static void modifyDamageByDepthsFury(LivingEntity victim, DamageSource source, DamageContext context) {
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;
        var attackerFury = attacker.getAttributeValue(AquamiraeAttributes.DEPTHS_FURY.holder());
        var modifier = Mth.clampedMap(attackerFury, 1.0, 2.0, 0.2, 1.0);
        context.multiply((float) modifier);
    }

    public static void modifyDamageByRuneDamage(LivingEntity victim, DamageSource source, DamageContext context) {
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;
        if (!attacker.getMainHandItem().has(AquamiraeDataComponents.RUNE_OF_THE_STORM_DAMAGE)) return;
        if (!isInColdBiome(attacker)) return;
        context.multiply(1.4f);
    }

    public static void modifyDamageByRuneProtection(LivingEntity victim, DamageSource source, DamageContext context) {
        if (!isInColdBiome(victim)) return;
        context.multiply(1f - getTotalRuneProtection(victim));
    }

    public static void applyDevouringCurse(LivingEntity victim, DamageSource source, DamageContext context) {
        if (source.is(DamageTypes.STARVE)) return;
        if (!(victim instanceof Player player)) return;
        if (!player.hasEffect(AquamiraeEffects.DEVOURING_CURSE.holder())) return;
        player.getFoodData().setFoodLevel(Math.max(0, player.getFoodData().getFoodLevel() - 2));
        context.multiply(0f);
    }

    private static float getTotalRuneProtection(LivingEntity victim) {
        return getRuneProtection(victim, EquipmentSlot.HEAD)
                + getRuneProtection(victim, EquipmentSlot.CHEST)
                + getRuneProtection(victim, EquipmentSlot.LEGS)
                + getRuneProtection(victim, EquipmentSlot.FEET);
    }

    private static float getRuneProtection(LivingEntity victim, EquipmentSlot slot) {
        return victim.getItemBySlot(slot).has(AquamiraeDataComponents.RUNE_OF_THE_STORM_PROTECTION) ? 0.1f : 0f;
    }

    private static boolean isInColdBiome(LivingEntity entity) {
        return entity.level().getBiome(entity.blockPosition()).value().getBaseTemperature() <= 0f;
    }
}