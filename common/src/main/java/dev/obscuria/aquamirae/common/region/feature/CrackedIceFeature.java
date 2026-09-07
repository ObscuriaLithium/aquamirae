package dev.obscuria.aquamirae.common.region.feature;

import dev.obscuria.aquamirae.common.region.feature.config.CrackedIceConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CrackedIceFeature extends Feature<CrackedIceConfig> {

    public CrackedIceFeature() {
        super(CrackedIceConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<CrackedIceConfig> context) {
        var config = context.config();
        var level = context.level();
        var origin = level.getChunk(context.origin()).getPos().getWorldPosition();
        var pos = new BlockPos.MutableBlockPos();

        for (var xOffset = 0; xOffset < 16; xOffset++) {
            for (var zOffset = 0; zOffset < 16; zOffset++) {

                var x = origin.getX() + xOffset;
                var z = origin.getZ() + zOffset;
                pos.set(x, context.chunkGenerator().getSeaLevel() - 1, z);

                if (!level.getBiome(pos).is(config.biomeMask())) continue;
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
