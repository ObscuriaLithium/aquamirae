package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.registry.AquamiraeParticleTypes;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class GoldenMoth extends Animal implements GeoEntity {

    protected final AnimatableInstanceCache cache;

    public GoldenMoth(EntityType<GoldenMoth> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.xpReward = 10;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Default", 10,
                state -> state.getAnimatable().onGround()
                        ? state.setAndContinue(DefaultAnimations.IDLE)
                        : state.setAndContinue(DefaultAnimations.FLY))
                .setOverrideEasingType(EasingType.EASE_IN_OUT_CUBIC));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return super.shouldRenderAtSqrDistance(distance * 0.5f);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (level() instanceof ServerLevel level) {
            var options = Objects.requireNonNull(AquamiraeParticleTypes.SHINE.simple());
            level.sendParticles(options,
                    getX(), getY(), getZ(), 6,
                    0.05, 0.05, 0.05, 0.8);
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (stack.is(Items.GLASS_BOTTLE)) {
            stack.shrink(1);
            if (!level().isClientSide()) {
                this.level().playSound(null, this.blockPosition(), AquamiraeSounds.ENTITY_GOLDEN_MOTH_CATCH, SoundSource.AMBIENT, 1, 1);
                this.spawnAtLocation(AquamiraeItems.GOLDEN_MOTH_JAR);
                this.discard();
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public void baseTick() {
        if (isInWaterOrBubble()) {
            addDeltaMovement(new Vec3(0, 0.05, 0));
        }
        super.baseTick();
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && tickCount % 4 == 0) {
            var motion = getDeltaMovement().scale(3);
            var particleType = Objects.requireNonNull(AquamiraeParticleTypes.SHINE.simple());
            level().addParticle(particleType,
                    getX() + random.triangle(0.0, 0.25),
                    getY() + random.triangle(0.0, 0.25),
                    getZ() + random.triangle(0.0, 0.25),
                    motion.x, motion.y, motion.z);
        }
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
        ConfiguredAttribute.applyAttributes(this, CommonConfig.GOLDEN_MOTH_ATTRIBUTES);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1, 20) {
            @Override
            protected Vec3 getPosition() {
                var random = GoldenMoth.this.getRandom();
                var dirX = GoldenMoth.this.getX() + ((random.nextFloat() * 2 - 1) * 16);
                var dirY = GoldenMoth.this.getY() + ((random.nextFloat() * 2 - 1) * 16);
                var dirZ = GoldenMoth.this.getZ() + ((random.nextFloat() * 2 - 1) * 16);
                return new Vec3(dirX, dirY, dirZ);
            }
        });
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    protected SoundEvent getAmbientSound() {
        return AquamiraeSounds.ENTITY_GOLDEN_MOTH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.AMETHYST_CLUSTER_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.LARGE_AMETHYST_BUD_BREAK;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.FLYING_SPEED);
    }
}
