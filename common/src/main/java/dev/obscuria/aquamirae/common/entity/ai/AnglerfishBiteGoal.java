package dev.obscuria.aquamirae.common.entity.ai;

import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.common.entity.Anglerfish;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;

public class AnglerfishBiteGoal extends Goal {

    protected static final int SOUND_TICK = 6;
    protected static final int DASH_TICK = 19;
    protected static final int HIT_TICK = 21;

    protected final Anglerfish anglerfish;
    protected int attackCooldown = 0;

    public AnglerfishBiteGoal(Anglerfish anglerfish) {
        this.anglerfish = anglerfish;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        if (attackCooldown-- > 0) return false;
        if (anglerfish.attackState.isPlaying()) return false;
        if (anglerfish.getTarget() == null) return false;
        var maxDistance = anglerfish.onGround() ? 12 : 6;
        return distanceToTarget() <= maxDistance
                && anglerfish.hasLineOfSight(anglerfish.getTarget());
    }

    @Override
    public boolean canContinueToUse() {
        return anglerfish.attackState.isPlaying();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.anglerfish.attackState.play(anglerfish);
    }

    @Override
    public void tick() {
        int attackTick = anglerfish.attackState.getTick();
        if (attackTick == HIT_TICK) attackTarget();
        if (attackTick == DASH_TICK) dashTowardsTarget();
        if (attackTick < DASH_TICK) slowDown();
        if (attackTick == SOUND_TICK) anglerfish.playSound(AquamiraeSounds.ENTITY_GENERIC_DEEP_BITE, 2f, 1f);
    }

    @Override
    public void stop() {
        this.attackCooldown = Mth.randomBetweenInclusive(anglerfish.getRandom(), 2, 20);
    }

    protected void slowDown() {
        this.anglerfish.setDeltaMovement(anglerfish.getDeltaMovement().scale(0.6F));
    }

    protected void dashTowardsTarget() {
        if (anglerfish.getTarget() == null) return;
        var vectorToTarget = anglerfish.position().vectorTo(anglerfish.getTarget().getEyePosition());
        double length = vectorToTarget.length();
        if (length > 5.0) vectorToTarget = vectorToTarget.scale(5.0 / length);
        var deltaMovement = anglerfish.getDeltaMovement().add(vectorToTarget.scale(0.3f));
        this.anglerfish.setDeltaMovement(deltaMovement);
    }

    protected void attackTarget() {
        if (anglerfish.getTarget() == null) return;
        if (distanceToTarget() > 2.5) return;
        this.anglerfish.doHurtTarget(anglerfish.getTarget());
    }

    protected double distanceToTarget() {
        if (anglerfish.getTarget() == null) return Double.MAX_VALUE;
        return anglerfish.getTarget().position().distanceTo(anglerfish.position());
    }
}
