
package com.obscuria.aquamirae.registry;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.common.worldgen.feature.*;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface AquamiraeFeatures {

    DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, Aquamirae.MODID);

    RegistryObject<WallsFeature> WALLS = REGISTRY.register("walls", WallsFeature::new);
    RegistryObject<CrackedIceFeature> CRACKED_ICE = REGISTRY.register("cracked_ice", CrackedIceFeature::new);
    RegistryObject<MonolithFeature> MONOLITH = REGISTRY.register("monolith", MonolithFeature::new);
    RegistryObject<CampFeature> CAMP = REGISTRY.register("camp", CampFeature::new);
    RegistryObject<WisteriaNiveisFeature> WISTERIA_NIVEIS = REGISTRY.register("wisteria_niveis", WisteriaNiveisFeature::new);
    RegistryObject<OxygeliumFeature> OXYGELIUM = REGISTRY.register("oxygelium", () -> new OxygeliumFeature(NoneFeatureConfiguration.CODEC));
}
