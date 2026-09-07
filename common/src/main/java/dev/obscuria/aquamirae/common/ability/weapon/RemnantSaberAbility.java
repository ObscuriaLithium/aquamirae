package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.PlayerHelper;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Variable;
import dev.obscuria.aquamirae.common.ability.variable.ConfigVariable;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.integration.DamageContext;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import dev.obscuria.aquamirae.registry.tags.AquamiraeEntityTypeTags;
import dev.obscuria.fragmentum.config.ConfigValue;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class RemnantSaberAbility extends SimpleAbility implements IDamageHandler {

    public static final RemnantSaberAbility SHARED = new RemnantSaberAbility();

    public static final String VAR_BASE_BONUS = "base_bonus";
    public static final String VAR_PER_KILL = "per_kill";
    public static final String VAR_MAX_BONUS = "max_bonus";

    private RemnantSaberAbility() {
        super(Component.translatable("ability.aquamirae.remnant_saber"), Util.make(VariableMap.builder(), builder -> {
            var maxBonus = new ConfigVariable(CommonConfig.REMNANT_SABER_MAX_BONUS);
            builder.define(VAR_BASE_BONUS, new BonusVariable(CommonConfig.REMNANT_SABER_BASE_BONUS, maxBonus), PercentageFormatter.SHARED);
            builder.define(VAR_PER_KILL, new ConfigVariable(CommonConfig.REMNANT_SABER_PER_KILL_BONUS), PercentageFormatter.SHARED);
            builder.define(VAR_MAX_BONUS, maxBonus, PercentageFormatter.SHARED);
        }));
    }

    @Override
    public void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {
        if (self.getType() == AquamiraeEntityTypes.CAPTAIN_CORNELIA.get())
            self.heal(Math.min(30, victim.getMaxHealth() * 0.4f));
        if (victim.getType().is(AquamiraeEntityTypeTags.ICE_MAZE_CREATURES))
            damage.multiply(1f + computeVariable(VAR_BASE_BONUS, self));
    }

    public static void onDamageDealtCallback(LivingEntity self, LivingEntity victim) {
        if (!victim.isDeadOrDying()) return;
        if (!(self instanceof Player player)) return;
        if (!victim.getType().is(AquamiraeEntityTypeTags.ICE_MAZE_CREATURES)) return;
        var bonusPerKill = SHARED.computeVariable(VAR_PER_KILL, self);
        PlayerHelper.addRemnantSaberBonus(player, bonusPerKill);
    }

    private record BonusVariable(ConfigValue<Double> configValue, Variable maxBonus) implements Variable {

        @Override
        public float baseValue() {
            return configValue.get().floatValue();
        }

        @Override
        public float compute(LivingEntity self) {
            if (!(self instanceof Player player)) return baseValue();
            return Math.min(baseValue() + PlayerHelper.getRemnantSaberBonus(player), maxBonus.compute(self));
        }
    }
}
