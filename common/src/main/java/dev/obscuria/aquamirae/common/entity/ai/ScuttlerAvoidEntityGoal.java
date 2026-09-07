package dev.obscuria.aquamirae.common.entity.ai;

import dev.obscuria.aquamirae.common.entity.Scuttler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class ScuttlerAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

    protected final Scuttler scuttler;

    public ScuttlerAvoidEntityGoal(
            Scuttler scuttler,
            Class<T> classToAvoid,
            float maxDistance,
            double walkSpeedMod,
            double sprintSpeedMod
    ) {
        super(scuttler, classToAvoid, maxDistance, walkSpeedMod, sprintSpeedMod);
        this.scuttler = scuttler;
    }

    @Override
    public boolean canUse() {
        return scuttler.canMove() && super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (scuttler.canMove()) return;
        scuttler.getNavigation().stop();
        this.stop();
    }
}