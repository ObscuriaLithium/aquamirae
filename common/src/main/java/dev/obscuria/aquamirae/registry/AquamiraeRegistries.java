package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.api.common.ability.AbilitySet;
import dev.obscuria.aquamirae.common.event.PathDefinition;
import dev.obscuria.aquamirae.common.region.noise.PackedNoise;
import dev.obscuria.fragmentum.content.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.content.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class AquamiraeRegistries {

    static final Registrar REGISTRAR = FragmentumRegistry.registrar(Aquamirae.MOD_ID);

    public static final Registry<AbilitySet> ABILITY_SET = REGISTRAR.createVanillaRegistry(Key.ABILITY_SET);

    public static final class Key {

        public static final ResourceKey<Registry<AbilitySet>> ABILITY_SET = create("ability_set");
        public static final ResourceKey<Registry<PackedNoise>> NOISE = create("noise");
        public static final ResourceKey<Registry<PathDefinition>> SHIPBREAKER_PATH = create("path");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(Aquamirae.id(name));
        }
    }

    public static void init() {

        AquamiraeSounds.init();
        AquamiraeAttributes.init();
        AquamiraeEffects.init();
        AquamiraeDataComponents.init();
        AquamiraeEntityTypes.init();
        AquamiraeBlocks.init();
        AquamiraeArmorMaterials.init();
        AquamiraeAbilitySets.init();
        AquamiraeItems.init();
        AquamiraeCreativeTabs.init();
        AquamiraeParticleTypes.init();
        AquamiraeFeatures.init();
        AquamiraePlacementModifiers.init();
        AquamiraeStructureProcessors.init();
        AquamiraeStructurePlacements.init();
        AquamiraeCriteriaTriggers.init();
        AquamiraeRecipeSerializers.init();
        AquamiraeLootPoolEntries.init();

        REGISTRAR.createDataRegistry(Key.NOISE, () -> PackedNoise.DIRECT_CODEC);
        REGISTRAR.createDataRegistry(Key.SHIPBREAKER_PATH, () -> PathDefinition.DIRECT_CODEC);
    }
}
