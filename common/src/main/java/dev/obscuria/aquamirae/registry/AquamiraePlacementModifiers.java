package dev.obscuria.aquamirae.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.region.feature.placement.ChunkSampledBiomeFilter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public final class AquamiraePlacementModifiers {

    public static final PlacementModifierType<ChunkSampledBiomeFilter> CHUNK_SAMPLED_BIOME = register("chunk_sampled_biome", ChunkSampledBiomeFilter.CODEC);

    private static <T extends PlacementFilter> PlacementModifierType<T> register(String name, MapCodec<T> codec) {
        PlacementModifierType<T> type = () -> codec;
        AquamiraeRegistries.REGISTRAR.register(Registries.PLACEMENT_MODIFIER_TYPE, Aquamirae.id(name), () -> type);
        return type;
    }

    static void init() {}
}
