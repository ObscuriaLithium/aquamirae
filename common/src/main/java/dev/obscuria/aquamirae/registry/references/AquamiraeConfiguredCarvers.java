package dev.obscuria.aquamirae.registry.references;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

public interface AquamiraeConfiguredCarvers {

    ResourceKey<ConfiguredWorldCarver<?>> CANYON = create("canyon");

    private static ResourceKey<ConfiguredWorldCarver<?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, Aquamirae.id(name));
    }
}
