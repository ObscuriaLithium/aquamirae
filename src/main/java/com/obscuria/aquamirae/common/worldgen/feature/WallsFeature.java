package com.obscuria.aquamirae.common.worldgen.feature;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.common.worldgen.feature.config.WallsConfiguration;
import com.obscuria.aquamirae.common.worldgen.noise.PackedNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class WallsFeature extends Feature<WallsConfiguration> {

    public WallsFeature() {
        super(WallsConfiguration.CODEC);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean place(FeaturePlaceContext<WallsConfiguration> context) {

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
                var paletteNoise = config.wallLayer2().noise().value();
                var layer1Height = (int) (config.wallLayer1Height() * config.wallLayer1().sample(x, z));
                var layer2Height = (int) (config.wallLayer2Height() * config.wallLayer2().sample(x, z));
                var isGround =  config.groundMask().test(x, z);
                fragment(level, pos, paletteNoise, Math.max(layer1Height, layer2Height), isGround);
            }
        }

        return true;
    }

    private static void fragment(WorldGenLevel level, BlockPos.MutableBlockPos pos, PackedNoise noise,
                                 int height, boolean isGround) {
        if (height <= 0) {
            if (!isGround) return;
            level.setBlock(pos, Blocks.SNOW_BLOCK.defaultBlockState(), Block.UPDATE_CLIENTS);
        } else {
            var offset = height / 2;
            pos.move(Direction.DOWN, offset);
            for (var i = -offset; i < height; i++) {

                final Block block;
                if (i + 1 == height) {
                    block = Blocks.SNOW_BLOCK;
                } else if (noise.sample((64 + pos.getX()) * 4, pos.getY() * 4, (64 + pos.getZ()) * 4) > 0.6f) {
                    block = Blocks.BLUE_ICE;
                } else {
                    block = Blocks.PACKED_ICE;
                }
                level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_CLIENTS);

                pos.move(Direction.UP);
            }
        }
    }
}
