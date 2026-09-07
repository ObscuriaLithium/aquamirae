package dev.obscuria.aquamirae.common.entity.ai.navigation;

import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class WaterWalkerNodeEvaluator extends WalkNodeEvaluator {

    @Override
    protected boolean isAmphibious() {
        return true;
    }
}