package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.api.common.ability.AbilityConditions;
import dev.obscuria.aquamirae.api.common.ability.AbilityCounters;
import dev.obscuria.aquamirae.api.common.ability.AbilitySet;
import dev.obscuria.aquamirae.api.common.ability.AbilityStyles;
import dev.obscuria.aquamirae.api.common.ability.resolver.ComponentAbilitySetResolver;
import dev.obscuria.aquamirae.api.common.ability.resolver.CompositeAbilitySetResolver;
import dev.obscuria.aquamirae.api.common.ability.resolver.EquipmentAbilitySetResolver;
import dev.obscuria.aquamirae.common.ability.armor.*;
import dev.obscuria.aquamirae.common.ability.weapon.*;
import dev.obscuria.fragmentum.content.registry.Deferred;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.function.Consumer;

public final class AquamiraeAbilitySets {

    public static final Deferred<AbilitySet, AbilitySet> TERRIBLE_ARMOR;
    public static final Deferred<AbilitySet, AbilitySet> ABYSSAL_ARMOR;
    public static final Deferred<AbilitySet, AbilitySet> SALVAGER_ARMOR;

    public static final Deferred<AbilitySet, AbilitySet> REMNANT_SABER;
    public static final Deferred<AbilitySet, AbilitySet> TERRIBLE_BLADE;
    public static final Deferred<AbilitySet, AbilitySet> TERRIBLE_FANG;
    public static final Deferred<AbilitySet, AbilitySet> TERRIBLE_CLEAVER;

    public static final Deferred<AbilitySet, AbilitySet> SHATTERBLADE;
    public static final Deferred<AbilitySet, AbilitySet> SHATTERSPIKE;

    public static final Deferred<AbilitySet, AbilitySet> TIDEPIERCER;
    public static final Deferred<AbilitySet, AbilitySet> JAWBREAKER;
    public static final Deferred<AbilitySet, AbilitySet> DAGGER_OF_GREED;
    public static final Deferred<AbilitySet, AbilitySet> TEMPEST_TRIDENT;
    public static final Deferred<AbilitySet, AbilitySet> DREADWAKE;

    static {
        var terribleSetName = Component.translatable("ability_set.aquamirae.terrible");
        var abyssalSetName = Component.translatable("ability_set.aquamirae.abyssal");
        var salvagerSetName = Component.translatable("ability_set.aquamirae.salvager");
        var specialSetName = Component.translatable("ability_set.aquamirae.special");

        TERRIBLE_ARMOR = register("terrible_armor",
                builder -> builder
                        .displayName(terribleSetName)
                        .resolver(EquipmentAbilitySetResolver.anyArmorSlot())
                        .addTier(2, TerribleArmorHalfSetAbility.SHARED)
                        .addTier(4, TerribleArmorFullSetAbility.SHARED));
        ABYSSAL_ARMOR = register("abyssal_armor",
                builder -> builder
                        .displayName(abyssalSetName)
                        .style(AbilityStyles.PURPLE_GEM)
                        .resolver(EquipmentAbilitySetResolver.anyArmorSlot())
                        .addTier(2, AbyssalArmorHalfSetAbility.SHARED)
                        .addVariantTier(4, AbyssalArmorFullSetWithHeaumeAbility.SHARED, EquipmentSlot.HEAD, () -> AquamiraeItems.ABYSSAL_HEAUME)
                        .addVariantTier(4, AbyssalArmorFullSetWithTiaraAbility.SHARED, EquipmentSlot.HEAD, () -> AquamiraeItems.ABYSSAL_TIARA));
        SALVAGER_ARMOR = register("salvager_armor",
                builder -> builder
                        .displayName(salvagerSetName)
                        .resolver(EquipmentAbilitySetResolver.anyArmorSlot())
                        .addTier(4, SalvagerArmorFullSetAbility.SHARED));

        REMNANT_SABER = register("remnant_saber",
                builder -> builder
                        .displayName(terribleSetName)
                        .resolver(CompositeAbilitySetResolver.related(EquipmentAbilitySetResolver.mainhand(), TERRIBLE_ARMOR::holder))
                        .addTier(1, RemnantSaberAbility.SHARED, AbilityConditions.mainhand()));
        TERRIBLE_BLADE = register("terrible_blade",
                builder -> builder
                        .displayName(terribleSetName)
                        .resolver(CompositeAbilitySetResolver.related(EquipmentAbilitySetResolver.mainhand(), TERRIBLE_ARMOR::holder))
                        .addTier(1, TerribleBladeBaseAbility.SHARED, AbilityConditions.mainhand())
                        .addTier(5, TerribleBladeFullAbility.SHARED, AbilityConditions.mainhand()));
        TERRIBLE_FANG = register("terrible_fang",
                builder -> builder
                        .displayName(terribleSetName)
                        .resolver(CompositeAbilitySetResolver.related(EquipmentAbilitySetResolver.mainhand(), TERRIBLE_ARMOR::holder))
                        .addTier(1, TerribleFangBaseAbility.SHARED, AbilityConditions.mainhand())
                        .addTier(5, TerribleFangFullAbility.SHARED, AbilityConditions.mainhand()));
        TERRIBLE_CLEAVER = register("terrible_cleaver",
                builder -> builder
                        .displayName(terribleSetName)
                        .resolver(CompositeAbilitySetResolver.related(EquipmentAbilitySetResolver.mainhand(), TERRIBLE_ARMOR::holder))
                        .addTier(1, TerribleCleaverBaseAbility.SHARED, AbilityConditions.mainhand())
                        .addTier(5, TerribleCleaverFullAbility.SHARED, AbilityConditions.mainhand()));

        SHATTERBLADE = register("shatterblade",
                builder -> builder
                        .displayName(abyssalSetName)
                        .style(AbilityStyles.PURPLE_GEM)
                        .resolver(CompositeAbilitySetResolver.related(EquipmentAbilitySetResolver.mainhand(), ABYSSAL_ARMOR::holder))
                        .addTier(1, ShatterbladeBaseAbility.SHARED, AbilityConditions.mainhand())
                        .addTier(5, ShatterbladeFullAbility.SHARED, AbilityConditions.mainhand()));
        SHATTERSPIKE = register("shatterspike",
                builder -> builder
                        .displayName(abyssalSetName)
                        .style(AbilityStyles.PURPLE_GEM)
                        .resolver(CompositeAbilitySetResolver.related(EquipmentAbilitySetResolver.mainhand(), ABYSSAL_ARMOR::holder))
                        .addTier(1, ShatterspikeBaseAbility.SHARED, AbilityConditions.mainhand())
                        .addTier(5, ShatterspikeFullAbility.SHARED, AbilityConditions.mainhand()));

        TIDEPIERCER = register("tidepiercer",
                builder -> builder
                        .displayName(salvagerSetName)
                        .style(AbilityStyles.RED_GEM)
                        .resolver(EquipmentAbilitySetResolver.mainhand())
                        .addTier(1, TidepiercerAbility.SHARED));
        JAWBREAKER = register("jawbreaker",
                builder -> builder
                        .displayName(specialSetName)
                        .style(AbilityStyles.RED_GEM)
                        .resolver(EquipmentAbilitySetResolver.mainhand())
                        .addTier(1, JawbreakerAbility.SHARED));
        DAGGER_OF_GREED = register("dagger_of_greed",
                builder -> builder
                        .displayName(specialSetName)
                        .style(AbilityStyles.GREEN_GEM)
                        .resolver(EquipmentAbilitySetResolver.mainhand())
                        .addTier(1, DaggerOfGreedAbility.SHARED));
        TEMPEST_TRIDENT = register("tempest_trident",
                builder -> builder
                        .displayName(specialSetName)
                        .resolver(EquipmentAbilitySetResolver.mainhand())
                        .addTier(1, TempestTridentAbility.SHARED));
        DREADWAKE = register("dreadwake",
                builder -> builder
                        .displayName(specialSetName)
                        .style(AbilityStyles.PURPLE_GEM)
                        .counter(AbilityCounters.CHARGES)
                        .resolver(ComponentAbilitySetResolver.mainhand(AquamiraeDataComponents.ABYSSAL_CHARGES))
                        .addTier(0, DreadwakeBaseAbility.SHARED)
                        .addTier(250, DreadwakeTier1Ability.SHARED)
                        .addTier(500, DreadwakeTier2Ability.SHARED)
                        .addTier(1000, DreadwakeTier3Ability.SHARED));
    }

    private static Deferred<AbilitySet, AbilitySet> register(String name, Consumer<AbilitySet.Builder> consumer) {
        return AquamiraeRegistries.REGISTRAR.register(AquamiraeRegistries.Key.ABILITY_SET, Aquamirae.id(name), () -> {
            var builder = AbilitySet.builder();
            consumer.accept(builder);
            return builder.build();
        });
    }

    static void init() {}
}