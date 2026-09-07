package dev.obscuria.aquamirae.common.region.feature;

import dev.obscuria.aquamirae.common.block.OxyheliumBlock;
import dev.obscuria.aquamirae.common.region.feature.config.OxyheliumConfig;
import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

@SuppressWarnings("deprecation")
public class OxyheliumFeature extends Feature<OxyheliumConfig> {

    public OxyheliumFeature() {
        super(OxyheliumConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<OxyheliumConfig> context) {

        var level = context.level();
        var origin = context.origin();
        var config = context.config();
        var random = context.random();

        var seaLevel = level.getSeaLevel();
        var maxLength = config.maxLength().sample(random);
        var maxDepth = config.maxDepth().sample(random);
        var count = config.count().sample(random);
        var spreadRadius = config.spreadRadius().sample(random);
        var lengthPenalty = config.lengthPenaltyPerBlock().sample(random);
        var minY = seaLevel - maxDepth;

        var stemState = AquamiraeBlocks.OXYHELIUM.instantiate()
                .setValue(OxyheliumBlock.WATERLOGGED, true);
        var budState = AquamiraeBlocks.OXYHELIUM.instantiate()
                .setValue(OxyheliumBlock.TYPE, OxyheliumBlock.Type.BUD)
                .setValue(OxyheliumBlock.WATERLOGGED, true);

        var pos = new BlockPos.MutableBlockPos();
        var anyPlaced = false;

        for (var i = 0; i < count; i++) {
            var offsetX = random.nextInt(spreadRadius * 2 + 1) - spreadRadius;
            var offsetZ = random.nextInt(spreadRadius * 2 + 1) - spreadRadius;
            var distance = Math.max(Math.abs(offsetX), Math.abs(offsetZ));
            var localMaxlength = maxLength - Math.round(distance * lengthPenalty);
            if (localMaxlength <= 0) continue;

            var length = 0;
            for (var offset = 0; offset < maxDepth; offset++) {
                pos.set(origin).move(offsetX, 0, offsetZ).setY(seaLevel - 1 - offset);
                if (!level.isStateAtPosition(pos.move(Direction.UP), config::canGrowOn)) continue;
                if (!level.isWaterAt(pos.move(Direction.DOWN))) continue;

                var isBud = offset == maxDepth - 1 || length == localMaxlength - 1;
                level.setBlock(pos, isBud ? budState : stemState, OxyheliumBlock.UPDATE_CLIENTS);
                anyPlaced = true;
                length += 1;

                if (length >= localMaxlength) break;
            }
        }

        return anyPlaced;
    }
}