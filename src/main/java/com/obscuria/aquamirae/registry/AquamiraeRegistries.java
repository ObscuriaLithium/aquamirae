package com.obscuria.aquamirae.registry;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.common.worldgen.noise.PackedNoise;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DataPackRegistryEvent;

public final class AquamiraeRegistries {

    public interface Keys {

        ResourceKey<Registry<PackedNoise>> NOISE = ResourceKey.createRegistryKey(Aquamirae.key("noise"));
    }

    public static void init(IEventBus eventBus) {
        AquamiraePlacementModifiers.REGISTRY.register(eventBus);
        AquamiraeRecipes.REGISTRY.register(eventBus);
        AquamiraeCreativeTab.REGISTRY.register(eventBus);
        AquamiraeFeatures.REGISTRY.register(eventBus);
        AquamiraeSounds.REGISTRY.register(eventBus);
        AquamiraeBlocks.REGISTRY.register(eventBus);
        AquamiraeEntities.REGISTRY.register(eventBus);
        AquamiraeItems.REGISTRY.register(eventBus);
        AquamiraeMobEffects.REGISTRY.register(eventBus);
        AquamiraePotions.REGISTRY.register(eventBus);
        AquamiraeParticleTypes.REGISTRY.register(eventBus);

        eventBus.addListener(AquamiraeRegistries::createDataRegistries);
    }

    private static void createDataRegistries(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Keys.NOISE, PackedNoise.DIRECT_CODEC);
    }
}
