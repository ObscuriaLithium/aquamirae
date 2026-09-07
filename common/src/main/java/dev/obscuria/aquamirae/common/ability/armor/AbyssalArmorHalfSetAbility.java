package dev.obscuria.aquamirae.common.ability.armor;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IEffectHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.common.ability.variable.DepthsFuryVariable;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PotencyFormatter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public final class AbyssalArmorHalfSetAbility extends SimpleAbility implements IEffectHandler {

    public static final AbyssalArmorHalfSetAbility SHARED = new AbyssalArmorHalfSetAbility();

    public static final String VAR_DURATION = "duration";
    public static final String VAR_MAX_STACKS = "max_stacks";

    private AbyssalArmorHalfSetAbility() {
        super(Component.translatable("ability.aquamirae.abyssal_armor.half"), VariableMap.builder()
                .define(VAR_DURATION, new DepthsFuryVariable(30f, 30f), DecimalFormatter.SHARED)
                .define(VAR_MAX_STACKS, new Constant(2), PotencyFormatter.SHARED));
    }

    @Override
    public void onEffectAdded(LivingEntity entity, MobEffectInstance instance) {
        if (instance.getEffect().value().getCategory() != MobEffectCategory.HARMFUL) return;
        var duration = Math.round(20 * computeVariable(VAR_DURATION, entity));
        var amplifier = selectAmplifier(entity);
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, amplifier));
    }

    private int selectAmplifier(LivingEntity entity) {
        @Nullable var current = entity.getEffect(MobEffects.DAMAGE_BOOST);
        var maxStacks = Math.round(computeVariable(VAR_MAX_STACKS, entity));
        return Math.min(maxStacks, current == null ? 0 : current.getAmplifier() + 1);
    }
}
