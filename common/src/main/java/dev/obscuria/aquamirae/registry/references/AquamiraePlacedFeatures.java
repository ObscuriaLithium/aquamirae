package dev.obscuria.aquamirae.registry.references;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface AquamiraePlacedFeatures {

    ResourceKey<PlacedFeature> SURFACE_ICE_WALLS = create("surface_ice_walls");
    ResourceKey<PlacedFeature> SURFACE_CRACKED_ICE = create("surface_cracked_ice");
    ResourceKey<PlacedFeature> SURFACE_CAMP = create("surface_camp");
    ResourceKey<PlacedFeature> SURFACE_WISTERIA_NIVEIS = create("surface_wisteria_niveis");
    ResourceKey<PlacedFeature> OXYHELIUM = create("oxyhelium");

    private static ResourceKey<PlacedFeature> create(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Aquamirae.id(name));
    }
}
