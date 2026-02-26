package com.obscuria.aquamirae.common.worldgen.feature;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.common.worldgen.feature.config.CrackedIceConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class CrackedIceFeature extends Feature<CrackedIceConfiguration> {

    public CrackedIceFeature() {
        super(CrackedIceConfiguration.CODEC);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean place(FeaturePlaceContext<CrackedIceConfiguration> context) {

        var config = context.config();
        var level = context.level();
        var origin = level.getChunk(context.origin()).getPos().getWorldPosition();
        var pos = new BlockPos.MutableBlockPos();

        for (var xOffset = 0; xOffset < 16; xOffset++) {
            for (var zOffset = 0; zOffset < 16; zOffset++) {

                var x = origin.getX() + xOffset;
                var z = origin.getZ() + zOffset;
                pos.set(x, level.getSeaLevel() - 1, z);

                if (!level.getBiome(pos).is(Aquamirae.ICE_MAZE)) continue;
                if (!level.isWaterAt(pos)) continue;
                if (config.outerMask().test(x, z)
                        || (config.innerMask().test(x, z)
                        && !config.carverMask().test(x, z))) {
                    level.setBlock(pos, Blocks.ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }

        return true;
    }
}
