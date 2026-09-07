package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.integration.DamageContext;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public final class TidepiercerAbility extends SimpleAbility implements IDamageHandler {

    public static final TidepiercerAbility SHARED = new TidepiercerAbility();

    public static final String VAR_BONUS = "bonus";
    public static final String VAR_PER_BLOCK = "per_block";
    public static final String VAR_MAX_BONUS = "max_bonus";

    private TidepiercerAbility() {
        super(Component.translatable("ability.aquamirae.tidepiercer"), Util.make(VariableMap.builder(), builder -> {
            var perBlock = new Constant(0.02f);
            var maxBonus = new Constant(2.0f);
            builder.define(VAR_BONUS, new BonusVariable(perBlock, maxBonus), PercentageFormatter.SHARED);
            builder.define(VAR_PER_BLOCK, perBlock, PercentageFormatter.SHARED);
            builder.define(VAR_MAX_BONUS, maxBonus, PercentageFormatter.SHARED);
        }));
    }

    @Override
    public void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {
        if (!self.isUnderWater()) return;
        damage.multiply(1f + computeVariable(VAR_BONUS, self));
    }

    private record BonusVariable(Variable perBlock, Variable maxBonus) implements Variable {

        @Override
        public float baseValue() {
            return 0;
        }

        @Override
        public float compute(LivingEntity self) {
            int depth = Math.max(0, self.level().getSeaLevel() - self.blockPosition().getY());
            return Math.min(depth * perBlock.compute(self), maxBonus.compute(self));
        }
    }
}
