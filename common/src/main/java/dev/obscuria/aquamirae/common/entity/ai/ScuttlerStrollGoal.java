package dev.obscuria.aquamirae.common.entity.ai;

import dev.obscuria.aquamirae.common.entity.Scuttler;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class ScuttlerStrollGoal extends WaterAvoidingRandomStrollGoal {

    protected final Scuttler scuttler;

    public ScuttlerStrollGoal(Scuttler scuttler, double speed) {
        super(scuttler, speed, 0.2F);
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