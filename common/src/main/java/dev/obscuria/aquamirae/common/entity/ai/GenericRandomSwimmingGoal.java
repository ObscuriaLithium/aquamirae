package dev.obscuria.aquamirae.common.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GenericRandomSwimmingGoal extends RandomSwimmingGoal {

    protected final int radius;
    protected final int verticalDistance;

    public GenericRandomSwimmingGoal(PathfinderMob mob, double speed, int interval, int radius, int verticalDistance) {
        super(mob, speed, interval);
        this.radius = radius;
        this.verticalDistance = verticalDistance;
    }

    @Override
    protected @Nullable Vec3 getPosition() {
        return BehaviorUtils.getRandomSwimmablePos(this.mob, radius, verticalDistance);
    }
}
