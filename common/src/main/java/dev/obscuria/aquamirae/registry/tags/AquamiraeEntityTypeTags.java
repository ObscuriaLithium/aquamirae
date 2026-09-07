package dev.obscuria.aquamirae.registry.tags;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public interface AquamiraeEntityTypeTags {

    TagKey<EntityType<?>> ICE_MAZE_CREATURES = create("ice_maze_creatures");
    TagKey<EntityType<?>> AFFECTED_BY_DEPTHS_FURY = create("affected_by_depths_fury");
    TagKey<EntityType<?>> AFFECTED_BY_DEPTHS_RESILIENCE = create("affected_by_depths_resilience");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Aquamirae.id(name));
    }
}
