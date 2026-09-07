package dev.obscuria.aquamirae.common.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;

public class WaterWalkerPathNavigation extends GroundPathNavigation {

    public WaterWalkerPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.WATER) || super.isStableDestination(pos);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new WaterWalkerNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected boolean hasValidPathType(PathType pathType) {
        return pathType == PathType.WATER || super.hasValidPathType(pathType);
    }
}