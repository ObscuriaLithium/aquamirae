package dev.obscuria.aquamirae.common.region.feature;

import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import dev.obscuria.aquamirae.common.block.StashBlock;
import dev.obscuria.aquamirae.common.region.feature.config.CampConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CampFeature extends Feature<CampConfig> {

    public CampFeature() {
        super(CampConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<CampConfig> context) {
        var level = context.level();
        var random = context.random();
        var pos = context.origin().mutable().setY(context.chunkGenerator().getSeaLevel() - 1).move(Direction.UP, context.config().height());

        if (!level.getBlockState(pos).is(Blocks.SNOW_BLOCK)) return false;
        pos.move(Direction.UP);
        var campfireState = Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, true);
        level.setBlock(pos, campfireState, CampfireBlock.UPDATE_CLIENTS);

        var cx = pos.getX();
        var cy = pos.getY() - 1;
        var cz = pos.getZ();
        var xAxis = random.nextBoolean();

        if (xAxis) {
            placeBench(level, pos, cx + 2, cy, cz, Direction.SOUTH);
            placeBench(level, pos, cx - 2, cy, cz, Direction.SOUTH);
        } else {
            placeBench(level, pos, cx, cy, cz + 2, Direction.EAST);
            placeBench(level, pos, cx, cy, cz - 2, Direction.EAST);
        }

        placeBarrels(context.config(), level, random, pos, cx, cy, cz);

        return true;
    }

    protected void placeBench(
            LevelAccessor level,
            BlockPos.MutableBlockPos pos,
            int baseX, int baseY, int baseZ,
            Direction direction
    ) {
        var slab = Blocks.SPRUCE_SLAB.defaultBlockState();

        for (var i = -1; i <= 1; i++) {
            var x = baseX + direction.getStepX() * i;
            var z = baseZ + direction.getStepZ() * i;

            pos.set(x, baseY, z);
            if (!level.getBlockState(pos).is(Blocks.SNOW_BLOCK)) continue;
            pos.move(Direction.UP);
            if (!level.getBlockState(pos).isAir()) continue;
            level.setBlock(pos, slab, CampfireBlock.UPDATE_CLIENTS);
        }
    }

    protected void placeBarrels(
            CampConfig config,
            LevelAccessor level,
            RandomSource random,
            BlockPos.MutableBlockPos pos,
            int cx, int cy, int cz
    ) {
        var barrelCount = 1 + random.nextInt(config.barrelCount());

        for (var i = 0; i < barrelCount; i++) {
            var dx = random.nextInt(7) - 3;
            var dz = random.nextInt(7) - 3;
            if (Math.abs(dx) < 2 && Math.abs(dz) < 2) continue;
            var x = cx + dx;
            var z = cz + dz;

            pos.set(x, cy, z);
            if (!level.getBlockState(pos).is(Blocks.SNOW_BLOCK)) continue;
            pos.move(Direction.UP);
            if (!level.getBlockState(pos).isAir()) continue;
            var barrelFacing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            level.setBlock(pos,
                    Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, barrelFacing),
                    CampfireBlock.UPDATE_CLIENTS);

            if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity container) {
                container.setLootTable(config.barrelLootTable(), random.nextLong());
            }

            if (random.nextBoolean()) continue;
            pos.move(Direction.UP);
            var pouchFacing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            level.setBlock(pos,
                    AquamiraeBlocks.PIRATE_POUCH.instantiate().setValue(StashBlock.FACING, pouchFacing),
                    CampfireBlock.UPDATE_CLIENTS);
        }
    }
}