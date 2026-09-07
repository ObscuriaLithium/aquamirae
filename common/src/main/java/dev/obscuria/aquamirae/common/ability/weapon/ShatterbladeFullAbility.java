package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.integration.DamageContext;
import dev.obscuria.aquamirae.registry.AquamiraeEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public final class ShatterbladeFullAbility extends SimpleAbility implements IDamageHandler {

    public static final ShatterbladeFullAbility SHARED = new ShatterbladeFullAbility();

    public static final String VAR_BONUS = "bonus";

    private ShatterbladeFullAbility() {
        super(Component.translatable("ability.aquamirae.shatterblade.full"), VariableMap.builder()
                .define(VAR_BONUS, new Constant(4f), DecimalFormatter.SHARED));
    }

    @Override
    public void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {
        var baseAbility = ShatterbladeBaseAbility.SHARED;
        var varMaxStacks = ShatterbladeBaseAbility.VAR_MAX_STACKS;
        var maxStacks = Math.round(baseAbility.computeVariable(varMaxStacks, self));
        if (getCurrentStacks(victim) < maxStacks) return;
        damage.add(computeVariable(VAR_BONUS, self));
        victim.playSound(SoundEvents.AMETHYST_BLOCK_BREAK);
    }

    private int getCurrentStacks(LivingEntity victim) {
        @Nullable var effect = victim.getEffect(AquamiraeEffects.HEALTH_SHATTER.holder());
        return effect == null ? 0 : effect.getAmplifier() + 1;
    }
}
