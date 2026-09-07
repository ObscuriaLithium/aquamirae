package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PotencyFormatter;
import net.minecraft.network.chat.Component;

public final class DreadwakeTier3Ability extends SimpleAbility {

    public static final DreadwakeTier3Ability SHARED = new DreadwakeTier3Ability();

    public static final String VAR_ABSORPTION = "absorption";
    public static final String VAR_REGENERATION = "regeneration";
    public static final String VAR_DURATION = "duration";
    public static final String VAR_SHARDS = "shards";

    private DreadwakeTier3Ability() {
        super(Component.translatable("ability.aquamirae.dreadwake.tier_3"), VariableMap.builder()
                .define(VAR_ABSORPTION, new Constant(1), PotencyFormatter.SHARED)
                .define(VAR_REGENERATION, new Constant(1), PotencyFormatter.SHARED)
                .define(VAR_DURATION, new Constant(8), DecimalFormatter.SHARED)
                .define(VAR_SHARDS, new Constant(3), DecimalFormatter.SHARED));
    }
}
