package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import dev.obscuria.aquamirae.common.entity.projectile.AbyssalShard;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public final class DreadwakeTier2Ability extends SimpleAbility implements IDamageHandler {

    public static final DreadwakeTier2Ability SHARED = new DreadwakeTier2Ability();

    public static final String VAR_THRESHOLD = "threshold";
    public static final String VAR_SHARDS = "shards";

    private DreadwakeTier2Ability() {
        super(Component.translatable("ability.aquamirae.dreadwake.tier_2"), VariableMap.builder()
                .define(VAR_THRESHOLD, new Constant(0.2f), PercentageFormatter.SHARED)
                .define(VAR_SHARDS, new Constant(3f), DecimalFormatter.SHARED));
    }

    @Override
    public void onDamageDealt(LivingEntity self, LivingEntity victim, DamageSource source) {
        if (source.getDirectEntity() == null || !victim.isDeadOrDying()) return;
        for (var i = 0; i < 3; i++) {
            var motion = source.getDirectEntity().position().vectorTo(victim.getEyePosition())
                    .normalize().scale(0.25f)
                    .xRot((float) self.getRandom().triangle(0.0, 0.75))
                    .yRot((float) self.getRandom().triangle(0.0, 0.75))
                    .scale((float) self.getRandom().triangle(0.9, 0.4));
            var shard = AbyssalShard.create(self.level(), self, victim.getEyePosition(), motion);
            shard.setShardScale(1f);
        }
    }
}
