package dev.obscuria.aquamirae.registry.references;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public interface AquamiraeBiomes {

    ResourceKey<Biome> ICE_MAZE = create("ice_maze");

    private static ResourceKey<Biome> create(String name) {
        return ResourceKey.create(Registries.BIOME, Aquamirae.id(name));
    }
}
