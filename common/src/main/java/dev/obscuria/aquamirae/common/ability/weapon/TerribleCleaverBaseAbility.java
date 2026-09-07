package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.common.ability.variable.ConfigDepthsFuryVariable;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.integration.DamageContext;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.registry.tags.AquamiraeEntityTypeTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public final class TerribleCleaverBaseAbility extends SimpleAbility implements IDamageHandler {

    public static final TerribleCleaverBaseAbility SHARED = new TerribleCleaverBaseAbility();

    public static final String VAR_BONUS = "bonus";

    private TerribleCleaverBaseAbility() {
        super(Component.translatable("ability.aquamirae.terrible_cleaver.base"), VariableMap.builder()
                .define(VAR_BONUS, new ConfigDepthsFuryVariable(
                        CommonConfig.TERRIBLE_CLEAVER_MIN_BONUS_DAMAGE,
                        CommonConfig.TERRIBLE_CLEAVER_MAX_BONUS_DAMAGE
                ), PercentageFormatter.SHARED));
    }

    @Override
    public void modifyOutgoingDamage(LivingEntity self, LivingEntity victim, DamageContext damage) {
        if (!victim.getType().is(AquamiraeEntityTypeTags.ICE_MAZE_CREATURES)) return;
        if (self.getHealth() >= self.getMaxHealth()) return;
        damage.multiply(1f + computeVariable(VAR_BONUS, self));
    }
}