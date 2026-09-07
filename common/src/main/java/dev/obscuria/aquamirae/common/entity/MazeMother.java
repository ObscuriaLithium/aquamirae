package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.registry.tags.AquamiraeBlockTags;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class MazeMother extends Monster {

    public double verticalMotionO;
    public double verticalMotion;

    public MazeMother(EntityType<MazeMother> type, Level world) {
        super(type, world);
        xpReward = 30;
        this.setPathfindingMalus(PathType.WATER, 0);
        this.moveControl = new MoveControl(this) {
            @Override
            public void tick() {
                if (MazeMother.this.isInWater())
                    MazeMother.this.setDeltaMovement(MazeMother.this.getDeltaMovement().add(0, 0.005, 0));
                if (this.operation == Operation.MOVE_TO && !MazeMother.this.getNavigation().isDone()) {
                    double dx = this.wantedX - MazeMother.this.getX();
                    double dy = this.wantedY - MazeMother.this.getY();
                    double dz = this.wantedZ - MazeMother.this.getZ();
                    double horizDistSqr = dx * dx + dz * dz;

                    if (horizDistSqr > 0.05) {
                        float f = (float) (Mth.atan2(dz, dx) * (180 / Math.PI)) - 90;
                        MazeMother.this.setYRot(this.rotlerp(MazeMother.this.getYRot(), f, 10));
                        MazeMother.this.yBodyRot = MazeMother.this.getYRot();
                        MazeMother.this.yHeadRot = MazeMother.this.getYRot();
                    }

                    float f1 = (float) (this.speedModifier * MazeMother.this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    if (MazeMother.this.isInWater()) {
                        MazeMother.this.setSpeed((float) MazeMother.this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
                        f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
                        MazeMother.this.setXRot(this.rotlerp(MazeMother.this.getXRot(), f2, 5));
                        float f3 = Mth.cos(MazeMother.this.getXRot() * (float) (Math.PI / 180.0));
                        MazeMother.this.setZza(f3 * f1);
                        MazeMother.this.setYya((float) (f1 * dy));
                    } else {
                        MazeMother.this.setSpeed(f1 * 0.05F);
                    }
                } else {
                    MazeMother.this.setSpeed(0);
                    MazeMother.this.setYya(0);
                    MazeMother.this.setZza(0);
                }
            }
        };
    }

    public float getVerticalMotion(float partialTick) {
        return (float) Mth.lerp(partialTick, verticalMotionO, verticalMotion);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(10F);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new LargeWaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, false, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false, false));
        this.goalSelector.addGoal(6, new RandomSwimmingGoal(this, 1, 40));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ELDER_GUARDIAN_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ELDER_GUARDIAN_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ELDER_GUARDIAN_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.DROWN)) return false;
        return super.hurt(source, amount);
    }

    @Override
    protected void updateNoActionTime() {
        final Vec3 center = this.position();
        List<Player> players = level().getEntitiesOfClass(Player.class, new AABB(center, center).inflate(128), e -> true).stream()
                .sorted(Comparator.comparingDouble(ent -> ent.distanceToSqr(center))).toList();
        if (!players.isEmpty()) this.setNoActionTime(0);
        super.updateNoActionTime();
    }

    @Override
    public void baseTick() {
        if (this.isInWater()) this.setDeltaMovement(this.getDeltaMovement().add(0, -0.001, 0));
        if (!this.level().isClientSide) {
            if (tickCount % 10 == 0) {
                this.breakIce(-1);
                this.breakIce(4);
                this.breakIce(5);
            }
        }
        super.baseTick();
    }

    @Override
    public void tick() {
        super.tick();
        this.verticalMotionO = this.verticalMotion;
        this.verticalMotion = getDeltaMovement().y;
    }

    private void breakIce(int offset) {
        for (int ix = -6; ix <= 6; ix++)
            for (int iz = -6; iz <= 6; iz++) {
                final BlockPos pos = new BlockPos(this.getBlockX() + ix, this.getBlockY() + offset, this.getBlockZ() + iz);
                if (this.level().getBlockState(pos.above()).isAir() && this.level().getBlockState(pos).is(AquamiraeBlockTags.MAZE_MOTHER_BREAKABLES)) {
                    if (this.random.nextBoolean())
                        if (this.level().getBlockState(pos).is(Blocks.ICE) || this.level().getBlockState(pos).is(Blocks.FROSTED_ICE)) {
                            this.level().destroyBlock(pos, true);
                            this.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                        } else this.level().destroyBlock(pos, true);
                }
            }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level, DifficultyInstance difficulty,
            MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        ConfiguredAttribute.applyAttributes(this, CommonConfig.MAZE_MOTHER_ATTRIBUTES);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public static class LargeWaterBoundPathNavigation extends WaterBoundPathNavigation {

        public LargeWaterBoundPathNavigation(Mob mob, Level level) {
            super(mob, level);
        }

        @Override
        protected void followThePath() {
            Vec3 mobPos = getTempMobPos();
            var nextNode = path.getNextNode();
            var nextNodePos = path.getNextNodePos();

            float reachDistance = mob.getBbWidth();
            this.maxDistanceToWaypoint = reachDistance;

            double dx = Math.abs(mob.getX() - (nextNodePos.getX() + 0.5));
            double dz = Math.abs(mob.getZ() - (nextNodePos.getZ() + 0.5));
            double dy = Math.abs(mob.getY() - nextNodePos.getY());

            boolean reached = dx < maxDistanceToWaypoint
                    && dz < maxDistanceToWaypoint
                    && dy < reachDistance;

            if (reached || (canCutCorner(nextNode.type) && shouldTargetNextNodeInDirection(mobPos))) {
                path.advance();
            }

            doStuckDetection(mobPos);
        }

        @Override
        public boolean canCutCorner(PathType pathType) {
            return pathType != PathType.DANGER_FIRE && pathType != PathType.DANGER_OTHER;
        }

        private boolean shouldTargetNextNodeInDirection(Vec3 vec) {
            if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
                return false;
            } else {
                Vec3 vec3 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
                if (!vec.closerThan(vec3, 2.0F)) {
                    return false;
                } else if (this.canMoveDirectly(vec, this.path.getNextEntityPos(this.mob))) {
                    return true;
                } else {
                    Vec3 vec31 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                    Vec3 vec32 = vec3.subtract(vec);
                    Vec3 vec33 = vec31.subtract(vec);
                    double d0 = vec32.lengthSqr();
                    double d1 = vec33.lengthSqr();
                    boolean flag = d1 < d0;
                    boolean flag1 = d0 < (double)0.5F;
                    if (!flag && !flag1) {
                        return false;
                    } else {
                        Vec3 vec34 = vec32.normalize();
                        Vec3 vec35 = vec33.normalize();
                        return vec35.dot(vec34) < (double)0.0F;
                    }
                }
            }
        }
    }
}
