package com.obscuria.aquamirae.common.worldgen.feature.placement;

import com.mojang.serialization.Codec;
import com.obscuria.aquamirae.registry.AquamiraePlacementModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ChunkSampledBiomeFilter extends PlacementFilter {

    public static final ChunkSampledBiomeFilter SHARED = new ChunkSampledBiomeFilter();
    public static final Codec<ChunkSampledBiomeFilter> CODEC = Codec.unit(() -> SHARED);
    private static final String ERROR = "Tried to biome check an unregistered feature";

    private ChunkSampledBiomeFilter() {}

    @Override
    public PlacementModifierType<ChunkSampledBiomeFilter> type() {
        return AquamiraePlacementModifiers.CHUNK_SAMPLED_BIOME.get();
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {

        var placedFeature = context.topFeature().orElseThrow(() -> new IllegalStateException(ERROR));
        var chunkX = SectionPos.blockToSectionCoord(pos.getX());
        var chunkZ = SectionPos.blockToSectionCoord(pos.getZ());

        var minX = chunkX << 4;
        var minZ = chunkZ << 4;
        var maxX = minX + 15;
        var maxZ = minZ + 15;

        if (checkCorner(context, context.getLevel(), placedFeature, minX, minZ)) return true;
        if (checkCorner(context, context.getLevel(), placedFeature, minX, maxZ)) return true;
        if (checkCorner(context, context.getLevel(), placedFeature, maxX, minZ)) return true;
        if (checkCorner(context, context.getLevel(), placedFeature, maxX, maxZ)) return true;

        return false;
    }

    @SuppressWarnings("deprecation")
    private boolean checkCorner(PlacementContext context,
                                LevelReader level,
                                PlacedFeature feature,
                                int x, int z) {

        var biome = level.getBiomeManager().getNoiseBiomeAtPosition(x, 62, z);
        return context.generator()
                .getBiomeGenerationSettings(biome)
                .hasFeature(feature);
    }
}