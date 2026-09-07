package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.region.feature.*;
import dev.obscuria.fragmentum.content.registry.Deferred;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.Supplier;

public final class AquamiraeFeatures {

    public static final Deferred<Feature<?>, IceWallsFeature> ICE_WALLS = register("ice_walls", IceWallsFeature::new);
    public static final Deferred<Feature<?>, CrackedIceFeature> CRACKED_ICE = register("cracked_ice", CrackedIceFeature::new);
    public static final Deferred<Feature<?>, WisteriaNiveisFeature> WISTERIA_NIVEIS = register("wisteria_niveis", WisteriaNiveisFeature::new);
    public static final Deferred<Feature<?>, OxyheliumFeature> OXYHELIUM = register("oxyhelium", OxyheliumFeature::new);
    public static final Deferred<Feature<?>, CampFeature> CAMP = register("camp", CampFeature::new);

    private static <T extends Feature<?>> Deferred<Feature<?>, T> register(String name, Supplier<T> supplier) {
        return AquamiraeRegistries.REGISTRAR.register(Registries.FEATURE, Aquamirae.id(name), supplier);
    }

    static void init() {}
}
