package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.common.entity.ai.DummyMeleeAttackGoal;
import dev.obscuria.aquamirae.common.entity.animation.AnimController;
import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import dev.obscuria.aquamirae.common.entity.animation.IAnimatable;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.common.entity.ai.AnglerfishBiteGoal;
import dev.obscuria.aquamirae.common.entity.ai.GenericRandomSwimmingGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Anglerfish extends Monster implements IAnimatable {

    public final AnimState attackState;
    public final AnimController controller;
    public float clientGroundMod = 0;
    public float clientGroundModO = 0;

    public Anglerfish(EntityType<Anglerfish> type, Level world) {
        super(type, world);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 20, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
        this.attackState = new AnimState("attack", 40);
        this.controller = new AnimController(this, attackState);
        this.xpReward = 12;
        this.setPathfindingMalus(PathType.WATER, 0);
    }

    @Override
    public AnimController getAnimationController() {
        return controller;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!super.doHurtTarget(entity)) return false;
        AquamiraeHelper.doPoison(this, entity, 200, 0);
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.controller.tick();
        if (attackState.isPlaying() && getTarget() != null) {
            lookAt(getTarget(), 45f, 45f);
            this.lookControl.setLookAt(getTarget());
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.clientGroundModO = clientGroundMod;

        if (isInWater()) {
            setDeltaMovement(getDeltaMovement().add(0, -0.001F, 0));
            this.clientGroundMod += (0f - clientGroundMod) * 0.2f;
        } else {
            this.clientGroundMod += (1f - clientGroundMod) * 0.2f;
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (isControlledByLocalInstance() && isInWater()) {
            moveRelative(0.1F, travelVector);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9));
            if (getTarget() == null) {
                setDeltaMovement(getDeltaMovement().add(0, -0.005, 0));
            }
        } else {
            super.travel(travelVector);
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
        ConfiguredAttribute.applyAttributes(this, CommonConfig.ANGLERFISH_ATTRIBUTES);
        if (CommonConfig.ENABLE_MAZE_MOTHER_SPAWNING.get()
                && level instanceof ServerLevel serverLevel
                && spawnType == MobSpawnType.NATURAL
                && random.nextDouble() < CommonConfig.MAZE_MOTHER_SPAWN_CHANCE.get()) {
            var mazeMother = new MazeMother(AquamiraeEntityTypes.MAZE_MOTHER.get(), serverLevel);
            mazeMother.moveTo(position());
            mazeMother.finalizeSpawn(level, difficulty, spawnType, null);
            serverLevel.addFreshEntity(mazeMother);
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AnglerfishBiteGoal(this));
        this.goalSelector.addGoal(2, new DummyMeleeAttackGoal(this, 1.6f, false));
        this.goalSelector.addGoal(3, new GenericRandomSwimmingGoal(this, 1, 120, 32, 7));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true, false));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        playSound(SoundEvents.GUARDIAN_FLOP, 0.15f, 1);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AquamiraeSounds.ENTITY_GENERIC_DEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AquamiraeSounds.ENTITY_GENERIC_DEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AquamiraeSounds.ENTITY_GENERIC_DEEP_DEATH;
    }

    @SuppressWarnings("deprecation")
    public static boolean checkSpawnRules(
            EntityType<Anglerfish> type, ServerLevelAccessor levelAccessor,
            MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return levelAccessor.getSeaLevel() - pos.getY() < 32
                && levelAccessor.getFluidState(pos).is(FluidTags.WATER)
                && levelAccessor.getDifficulty() != Difficulty.PEACEFUL;
    }
}