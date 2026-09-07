package dev.obscuria.aquamirae.registry.tags;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public interface AquamiraeDamageTypeTags {

    TagKey<DamageType> IS_SHIPBREAKER = create("is_shipbreaker");

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Aquamirae.id(name));
    }
}
