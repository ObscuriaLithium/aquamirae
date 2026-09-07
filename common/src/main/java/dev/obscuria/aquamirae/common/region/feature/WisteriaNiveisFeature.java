package dev.obscuria.aquamirae.common.region.feature;

import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WisteriaNiveisFeature extends Feature<NoneFeatureConfiguration> {

    public WisteriaNiveisFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        boolean anyPlaced = false;
        var count = context.random().nextInt(4, 12);
        var pos = new BlockPos.MutableBlockPos();
        for (var i = 0; i <= count; i++) {
            var x = (int) (context.origin().getX() + context.random().triangle(0, 5));
            var z = (int) (context.origin().getZ() + context.random().triangle(0, 5));
            pos.set(x, context.chunkGenerator().getSeaLevel(), z);
            if (AquamiraeBlocks.WISTERIA_NIVEIS.get().canBePlacedOn(context.level(), pos.below())) {
                context.level().setBlock(pos, AquamiraeBlocks.WISTERIA_NIVEIS.instantiate()
                        .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER), 3);
                context.level().setBlock(pos.above(), AquamiraeBlocks.WISTERIA_NIVEIS.instantiate()
                        .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
                anyPlaced = true;
            }
        }
        return anyPlaced;
    }
}
