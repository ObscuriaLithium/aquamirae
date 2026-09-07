package dev.obscuria.aquamirae.common.ability.armor;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDeathHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.registry.AquamiraeEffects;
import dev.obscuria.aquamirae.registry.references.AquamiraeDamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class AbyssalArmorFullSetWithTiaraAbility extends SimpleAbility implements IDeathHandler {

    public static final AbyssalArmorFullSetWithTiaraAbility SHARED = new AbyssalArmorFullSetWithTiaraAbility();

    public static final String VAR_DURATION = "duration";
    public static final String VAR_DEBUFF = "debuff";

    private AbyssalArmorFullSetWithTiaraAbility() {
        super(Component.translatable("ability.aquamirae.abyssal_armor.full_tiara"), VariableMap.builder()
                .define(VAR_DURATION, new Constant(300f), DecimalFormatter.SHARED)
                .define(VAR_DEBUFF, new Constant(0.8f), PercentageFormatter.SHARED));
    }

    @Override
    public boolean cancelDeath(LivingEntity entity, DamageSource source) {
        if (source.is(AquamiraeDamageTypes.CRYSTALLIZATION)) return false;
        if (entity.hasEffect(AquamiraeEffects.CRYSTALLIZATION.holder())) return false;
        var duration = Math.round(20 * computeVariable(VAR_DURATION, entity));
        entity.level().playSound(null, entity.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL);
        entity.addEffect(new MobEffectInstance(AquamiraeEffects.CRYSTALLIZATION.holder(), duration));
        entity.setHealth(entity.getMaxHealth());
        return true;
    }
}
