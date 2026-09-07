package dev.obscuria.aquamirae.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class DummyMeleeAttackGoal extends MeleeAttackGoal {

    public DummyMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    protected boolean canPerformAttack(LivingEntity entity) {
        return false;
    }
}
