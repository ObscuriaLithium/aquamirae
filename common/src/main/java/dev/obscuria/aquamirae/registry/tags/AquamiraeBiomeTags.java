package dev.obscuria.aquamirae.registry.tags;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface AquamiraeBiomeTags {

    TagKey<Biome> ICE_MAZE = create("ice_maze");
    TagKey<Biome> HAS_BLIZZARD = create("has_blizzard");
    TagKey<Biome> SPAWNS_SHIPBREAKER = create("spawns_shipbreaker");
    TagKey<Biome> WITHOUT_WATER_FREEZING = create("without_water_freezing");

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, Aquamirae.id(name));
    }
}