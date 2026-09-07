package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.registry.AquamiraeEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public final class ShatterbladeBaseAbility extends SimpleAbility implements IDamageHandler {

    public static final ShatterbladeBaseAbility SHARED = new ShatterbladeBaseAbility();

    public static final String VAR_REDUCTION = "reduction";
    public static final String VAR_DURATION = "duration";
    public static final String VAR_MAX_STACKS = "max_stacks";

    private ShatterbladeBaseAbility() {
        super(Component.translatable("ability.aquamirae.shatterblade.base"), VariableMap.builder()
                .define(VAR_REDUCTION, new Constant(0.05f), PercentageFormatter.SHARED)
                .define(VAR_DURATION, new Constant(12f), DecimalFormatter.SHARED)
                .define(VAR_MAX_STACKS, new Constant(10f), DecimalFormatter.SHARED));
    }

    @Override
    public void onDamageDealt(LivingEntity self, LivingEntity victim, DamageSource source) {
        var duration = Math.round(20 * computeVariable(VAR_DURATION, self));
        var amplifier = selectAmplifier(self, victim);
        victim.addEffect(new MobEffectInstance(AquamiraeEffects.HEALTH_SHATTER.holder(), duration, amplifier));
    }

    private int selectAmplifier(LivingEntity self, LivingEntity victim) {
        @Nullable var current = victim.getEffect(AquamiraeEffects.HEALTH_SHATTER.holder());
        var maxStacks = Math.round(computeVariable(VAR_MAX_STACKS, self)) - 1;
        return Math.min(maxStacks, current == null ? 0 : current.getAmplifier() + 1);
    }
}
