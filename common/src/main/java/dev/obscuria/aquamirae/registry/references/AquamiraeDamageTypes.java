package dev.obscuria.aquamirae.registry.references;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public interface AquamiraeDamageTypes {

    ResourceKey<DamageType> CRYSTALLIZATION = create("crystallization");
    ResourceKey<DamageType> SHIPBREAKER = create("shipbreaker");

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Aquamirae.id(name));
    }
}
