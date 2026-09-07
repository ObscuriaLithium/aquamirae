package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.common.ability.variable.ConfigDepthsFuryVariable;
import dev.obscuria.aquamirae.common.ability.variable.ConfigVariable;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.integration.DamageContext;
import dev.obscuria.aquamirae.config.CommonConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public final class TerribleCleaverFullAbility extends SimpleAbility implements IDamageHandler {

    public static final TerribleCleaverFullAbility SHARED = new TerribleCleaverFullAbility();

    public static final String VAR_CHANCE = "chance";
    public static final String VAR_BONUS = "bonus";

    private TerribleCleaverFullAbility() {
        super(Component.translatable("ability.aquamirae.terrible_cleaver.full"), VariableMap.builder()
                .define(VAR_CHANCE, new ConfigVariable(CommonConfig.TERRIBLE_CLEAVER_CRIT_CHANCE), PercentageFormatter.SHARED)
                .define(VAR_BONUS, new ConfigDepthsFuryVariable(
                        CommonConfig.TERRIBLE_CLEAVER_MIN_CRIT_BONUS,
                        CommonConfig.TERRIBLE_CLEAVER_MAX_CRIT_BONUS
                ), PercentageFormatter.SHARED));
    }

    @Override
    public void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {
        if (self.getRandom().nextFloat() >= computeVariable(VAR_CHANCE, self)) return;
        damage.multiply(1f + computeVariable(VAR_BONUS, self));
        AbilityHelper.animateCritEffect(self, victim);
    }
}
