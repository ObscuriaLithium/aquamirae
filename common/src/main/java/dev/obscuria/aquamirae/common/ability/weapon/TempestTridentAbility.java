package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.integration.DamageContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public final class TempestTridentAbility extends SimpleAbility implements IDamageHandler {

    public static final TempestTridentAbility SHARED = new TempestTridentAbility();

    public static final String VAR_DURATION = "duration";
    public static final String VAR_SHOCK_DURATION = "shock_duration";

    private TempestTridentAbility() {
        super(Component.translatable("ability.aquamirae.tempest_trident"), VariableMap.builder()
                .define(VAR_DURATION, new Constant(20), DecimalFormatter.SHARED)
                .define(VAR_SHOCK_DURATION, new Constant(1), DecimalFormatter.SHARED));
    }

    @Override
    public void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {
        if (!self.isUnderWater()) return;
        damage.multiply(1f + computeVariable(VAR_DURATION, self));
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
