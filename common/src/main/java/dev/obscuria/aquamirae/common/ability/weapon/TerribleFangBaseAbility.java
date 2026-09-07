package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.common.ability.variable.ConfigVariable;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.registry.AquamiraeAbilitySets;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.integration.DamageContext;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public final class TerribleFangBaseAbility extends SimpleAbility implements IDamageHandler {

    public static final TerribleFangBaseAbility SHARED = new TerribleFangBaseAbility();

    public static final String VAR_BONUS = "bonus";
    public static final String VAR_PER_HEART = "per_heart";
    public static final String VAR_MAX_BONUS = "max_bonus";

    private TerribleFangBaseAbility() {
        super(Component.translatable("ability.aquamirae.terrible_fang.base"), Util.make(VariableMap.builder(), builder -> {
            var perHeart = new ConfigVariable(CommonConfig.TERRIBLE_FANG_PER_HEART_BONUS);
            var maxBonus = new ConfigVariable(CommonConfig.TERRIBLE_FANG_MAX_BONUS);
            builder.define(VAR_BONUS, new BonusVariable(perHeart, maxBonus), PercentageFormatter.SHARED);
            builder.define(VAR_PER_HEART, perHeart, PercentageFormatter.SHARED);
            builder.define(VAR_MAX_BONUS, maxBonus, PercentageFormatter.SHARED);
        }));
    }

    @Override
    public void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {
        damage.multiply(1f + computeVariable(VAR_BONUS, self) * computeMultiplier(self, victim));
    }

    private float computeMultiplier(LivingEntity self, LivingEntity victim) {
        if (victim.getHealth() < victim.getMaxHealth()) return 1f;
        if (!AbilityHelper.isActive(
                AquamiraeAbilitySets.TERRIBLE_FANG.holder(),
                TerribleFangFullAbility.SHARED, self)) return 1f;
        return 2f;
    }

    private record BonusVariable(Variable perHeart, Variable maxBonus) implements Variable {

        @Override
        public float baseValue() {
            return 0f;
        }

        @Override
        public float compute(LivingEntity self) {
            var emptyHearts = (int) ((self.getMaxHealth() - self.getHealth()) / 2f);
            return Math.min(emptyHearts * perHeart.compute(self), maxBonus.compute(self));
        }
    }
}
