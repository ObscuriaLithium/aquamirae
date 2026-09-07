package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.integration.DamageContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public final class DreadwakeTier1Ability extends SimpleAbility implements IDamageHandler {

    public static final DreadwakeTier1Ability SHARED = new DreadwakeTier1Ability();

    public static final String VAR_BONUS = "bonus";

    private DreadwakeTier1Ability() {
        super(Component.translatable("ability.aquamirae.dreadwake.tier_1"), VariableMap.builder()
                .define(VAR_BONUS, new Constant(0.5f), PercentageFormatter.SHARED));
    }

    @Override
    public void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {
        if (victim.getHealth() >= victim.getMaxHealth() / 2) return;
        damage.multiply(1f + computeVariable(VAR_BONUS, self));
        AbilityHelper.animateCritEffect(self, victim);
    }
}
