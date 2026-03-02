package com.obscuria.aquamirae.common.worldgen.feature;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.common.worldgen.feature.config.MonolithConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class MonolithFeature extends Feature<MonolithConfiguration> {

    public MonolithFeature() {
        super(MonolithConfiguration.CODEC);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean place(FeaturePlaceContext<MonolithConfiguration> context) {

        var config = context.config();
        var level = context.level();
        var origin = level.getChunk(context.origin()).getPos().getWorldPosition();
        var pos = new BlockPos.MutableBlockPos();

        for (var xOffset = 0; xOffset < 16; xOffset++) {
            for (var zOffset = 0; zOffset < 16; zOffset++) {

                var x = origin.getX() + xOffset;
                var z = origin.getZ() + zOffset;
                pos.set(x, level.getSeaLevel(), z);

                if (!level.getBiome(pos).is(Aquamirae.ICE_MAZE)) continue;
                if (!config.spreadMask().test(x, z)) continue;
                if (config.carverMask().test(x, z)) continue;

                pos.move(Direction.UP, config.offset());
                for (var i = 0; i < config.height(); i++) {
                    level.setBlock(pos, config.material().defaultBlockState(), Block.UPDATE_CLIENTS);
                    pos.move(Direction.UP, 1);
                }
            }
        }

        return true;
    }
}
