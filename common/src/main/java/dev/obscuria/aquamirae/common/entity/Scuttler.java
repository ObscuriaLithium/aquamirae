package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.registry.tags.AquamiraeBlockTags;
import dev.obscuria.aquamirae.common.entity.ai.ScuttlerAvoidEntityGoal;
import dev.obscuria.aquamirae.common.entity.ai.ScuttlerSearchForItemsGoal;
import dev.obscuria.aquamirae.common.entity.ai.ScuttlerStrollGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class Scuttler extends Animal implements GeoEntity {

    protected static final EntityDataAccessor<Boolean> DATA_ID_BEETLED;
    protected static final RawAnimation ANIM_BEETLED;

    public float localInSnow;
    public float localInSnowO;
    public float localInWater;
    public float localInWaterO;

    protected final AnimatableInstanceCache cache;
    protected int ticksSinceEaten;

    public Scuttler(EntityType<? extends Scuttler> type, Level level) {
        super(type, level);
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.setPathfindingMalus(PathType.WATER, 0F);
        this.setCanPickUpLoot(true);
    }

    public ItemStack getStolenItem() {
        return getItemBySlot(EquipmentSlot.MAINHAND);
    }

    public void setStolenItem(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }

    public boolean isBeetled() {
        return this.entityData.get(DATA_ID_BEETLED);
    }

    public void setBeetled(boolean beetled) {
        this.entityData.set(DATA_ID_BEETLED, beetled);
    }

    public boolean canMove() {
        return !isBeetled();
    }

    public double getBaseScale() {
        return getAttributeBaseValue(Attributes.SCALE);
    }

    public void setBaseScale(double value) {
        Objects.requireNonNull(getAttribute(Attributes.SCALE)).setBaseValue(value);
    }

    public void addBaseScale(double value) {
        this.setBaseScale(getBaseScale() + value);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ScuttlerAvoidEntityGoal<>(this, Player.class, 6.0F, 1.4, 1.4));
        this.goalSelector.addGoal(2, new ScuttlerSearchForItemsGoal(this));
        this.goalSelector.addGoal(3, new ScuttlerStrollGoal(this, 1.0));
    }

    @Override
    public void tick() {
        super.tick();

        this.localInSnowO = localInSnow;
        this.localInWaterO = localInWater;
        this.localInSnow = Mth.clamp(localInSnow + (getInBlockState().is(Blocks.SNOW) ? 0.25f : -0.25f), 0f, 1f);
        this.localInWater = Mth.clamp(localInWater + (getInBlockState().is(Blocks.WATER) ? 0.25f : -0.25f), 0f, 1f);

        this.floatOnWater();
        this.checkInsideBlocks();
        if (level().isClientSide()) return;
        if (!isBeetled() || tickCount % 18 != 0) return;
        float pitch = 0.95F + 0.1F * random.nextFloat();
        this.playSound(AquamiraeSounds.ENTITY_SCUTTLER_UPSIDED, 0.35F, pitch);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(level() instanceof ServerLevel level)) return;
        if (!isAlive() || !isEffectiveAi()) return;

        var stack = getStolenItem();
        if (canEat(stack)) {
            ++this.ticksSinceEaten;
            if (ticksSinceEaten > 100) {
                var result = stack.finishUsingItem(level(), this);
                if (!result.isEmpty()) setStolenItem(result);
                this.sendParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER, 16);
                this.addBaseScale(0.25);
                this.ticksSinceEaten = 0;
            } else if (ticksSinceEaten > 60 && (ticksSinceEaten - 2) % 4 == 0) {
                this.playSound(getEatingSound(stack), 1f, 1f);
                this.level().broadcastEntityEvent(this, (byte) 45);
                level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack),
                        getX(), getEyeY(), getZ(), 6,
                        0.15, 0.15, 0.15, 0.02);
            }
        }

        if (getBaseScale() > 3.5 && tickCount % 10 == 0) {
            if (canEvolve()) evolveToShellback();
            this.sendParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER, 4);
        }
    }

    @Override
    public boolean canStandOnFluid(FluidState state) {
        return state.is(FluidTags.WATER);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!super.hurt(source, amount)) return false;
        if (source.getEntity() == null) return true;
        this.maybeDropStolenItem();
        this.addDeltaMovement(new Vec3(0.0, 0.1, 0.0));
        this.setBeetled(!isBeetled());
        this.hurtMarked = true;
        return true;
    }

    @Override
    public ItemStack equipItemIfPossible(ItemStack stack) {
        if (isBeetled() || !getStolenItem().isEmpty()) return ItemStack.EMPTY;
        this.setStolenItem(stack);
        return stack;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsBeetled", isBeetled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBeetled(tag.getBoolean("IsBeetled"));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AquamiraeItems.JELLYFISH_JELLY.asItem());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_BEETLED, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new ScuttlerPathNavigation(this, level);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!canMove()) return;
        this.playSound(AquamiraeSounds.ENTITY_SCUTTLER_STEP, 0.4F, 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AquamiraeSounds.ENTITY_SCUTTLER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AquamiraeSounds.ENTITY_SCUTTLER_DEATH;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Default", 10, state -> {
            if (state.getAnimatable().isBeetled())
                return state.setAndContinue(ANIM_BEETLED);
            return state.setAndContinue(DefaultAnimations.WALK);
        }).setOverrideEasingType(EasingType.EASE_IN_OUT_BACK));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level, DifficultyInstance difficulty,
            MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        ConfiguredAttribute.applyAttributes(this, CommonConfig.SCUTTLER_ATTRIBUTES);
        var variantModifier = Mth.clampedMap(getBaseScale(), 0.5f, 1.5f, -1f, 1f);
        if (getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            var modifier = new AttributeModifier(Aquamirae.id("bonus"), variantModifier * -0.25f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).addPermanentModifier(modifier);
        }
        if (getAttribute(Attributes.MAX_HEALTH) != null) {
            var modifier = new AttributeModifier(Aquamirae.id("bonus"), variantModifier * 0.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(modifier);
        }
        setHealth(getMaxHealth());
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void updateWalkAnimation(float partialTick) {
        super.updateWalkAnimation(partialTick * (float) Math.pow(getBaseScale(), -0.5f));
    }

    private boolean canEvolve() {
        var dimensions = AquamiraeEntityTypes.SHELLBACK.get().getDimensions();
        return level().noCollision(dimensions.makeBoundingBox(getX(), getY(), getZ()));
    }

    private void evolveToShellback() {
        if (!(level() instanceof ServerLevel level)) return;
        var entity = new Shellback(AquamiraeEntityTypes.SHELLBACK.get(), level());
        entity.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
        entity.setYBodyRot(yBodyRot);
        entity.setYHeadRot(getYHeadRot());
        this.level().addFreshEntity(entity);
        var difficulty = level.getCurrentDifficultyAt(blockPosition());
        entity.finalizeSpawn(level, difficulty, MobSpawnType.BREEDING, null);
        this.playSound(AquamiraeSounds.ENTITY_SCUTTLER_HURT, 1f, 1f);
        this.maybeDropStolenItem();
        this.discard();
    }

    private void sendParticlesAroundSelf(ParticleOptions options, int count) {
        if (!(level() instanceof ServerLevel level)) return;
        var offset = 0.25 * getBaseScale();
        level.sendParticles(options,
                getX(), getY() + offset / 2, getZ(), count,
                offset, offset, offset, 0.02);
    }

    private boolean canEat(ItemStack stack) {
        return getBaseScale() < 3.5 && stack.is(AquamiraeItems.JELLYFISH_JELLY.asItem());
    }

    private void maybeDropStolenItem() {
        if (getStolenItem().isEmpty()) return;
        this.spawnAtLocation(getStolenItem());
        this.setStolenItem(ItemStack.EMPTY);
    }

    private void floatOnWater() {
        if (this.isInWater()) {
            var context = CollisionContext.of(this);
            if (context.isAbove(LiquidBlock.STABLE_SHAPE, blockPosition(), true)
                    && !level().isWaterAt(blockPosition().above())) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(getDeltaMovement().scale(0.5F).add(0, 0.05, 0));
            }
        }
    }

    public static boolean checkSpawnRules(
            EntityType<Scuttler> type, ServerLevelAccessor level,
            MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return level.getBlockState(pos.below()).is(AquamiraeBlockTags.SCUTTLER_SPAWNABLE_ON);
    }

    static {
        DATA_ID_BEETLED = SynchedEntityData.defineId(Scuttler.class, EntityDataSerializers.BOOLEAN);
        ANIM_BEETLED = RawAnimation.begin().thenLoop("misc.beetled");
    }

    static class ScuttlerPathNavigation extends GroundPathNavigation {

        ScuttlerPathNavigation(Scuttler scuttler, Level level) {
            super(scuttler, level);
        }

        protected PathFinder createPathFinder(int maxVisitedNodes) {
            this.nodeEvaluator = new ScuttlerNodeEvaluator();
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
        }

        protected boolean hasValidPathType(PathType pathType) {
            return pathType == PathType.WATER || super.hasValidPathType(pathType);
        }

        public boolean isStableDestination(BlockPos pos) {
            return level.getBlockState(pos).is(Blocks.WATER) || super.isStableDestination(pos);
        }
    }

    static class ScuttlerNodeEvaluator extends WalkNodeEvaluator {

        @Override
        protected boolean isAmphibious() {
            return true;
        }
    }
}