package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.common.ability.variable.ConfigVariable;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PotencyFormatter;
import dev.obscuria.aquamirae.config.CommonConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class TerribleBladeFullAbility extends SimpleAbility implements IDamageHandler {

    public static final TerribleBladeFullAbility SHARED = new TerribleBladeFullAbility();

    public static final String VAR_AMPLIFIER = "amplifier";
    public static final String VAR_DURATION = "duration";

    private TerribleBladeFullAbility() {
        super(Component.translatable("ability.aquamirae.terrible_blade.full"), VariableMap.builder()
                .define(VAR_AMPLIFIER, new ConfigVariable(CommonConfig.TERRIBLE_BLADE_REGENERATION_AMPLIFIER), PotencyFormatter.SHARED)
                .define(VAR_DURATION, new ConfigVariable(CommonConfig.TERRIBLE_BLADE_REGENERATION_DURATION), DecimalFormatter.SHARED));
    }

    @Override
    public void onDamageDealt(LivingEntity self, LivingEntity victim, DamageSource source) {
        if (!victim.isDeadOrDying()) return;
        if (!victim.hasEffect(MobEffects.POISON)) return;
        self.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
                Math.round(20 * computeVariable(VAR_DURATION, self)),
                Math.round(computeVariable(VAR_AMPLIFIER, self))));
    }
}
