package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.api.common.entity.MultipartEntity;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.common.entity.ai.ShellbackStrollGoal;
import dev.obscuria.aquamirae.common.entity.ai.navigation.WaterWalkerPathNavigation;
import dev.obscuria.aquamirae.common.entity.inventory.ShellbackInventory;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class Shellback extends TamableAnimal implements MultipartEntity, GeoEntity, PlayerRideable, Saddleable {

    public static final int MAX_SADDLES = 2;

    protected static final EntityDataAccessor<Integer> DATA_ID_SADDLE_COUNT;
    protected static final EntityDataAccessor<Boolean> DATA_ID_CHEST_0;
    protected static final EntityDataAccessor<Boolean> DATA_ID_CHEST_1;
    protected static final EntityDataAccessor<Boolean> DATA_ID_CHEST_2;
    protected static final EntityDataAccessor<Boolean> DATA_ID_CHEST_3;

    @Getter private final List<ShellbackChestPart> subParts;
    @Getter private final ShellbackInventory inventory;

    protected final AnimatableInstanceCache cache;
    protected int openCount = 0;
    protected float mountYRot = 0f;

    public Shellback(EntityType<? extends Shellback> type, Level level) {
        super(type, level);
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.inventory = new ShellbackInventory(this);

        this.setPathfindingMalus(PathType.WATER, 0F);

        var partTypes = ShellbackChestType.values();
        this.subParts = new ArrayList<>(partTypes.length);
        for (var partType : partTypes) {
            subParts.add(new ShellbackChestPart(this, partType));
        }
    }

    public void setSaddleCount(int count) {
        this.entityData.set(DATA_ID_SADDLE_COUNT, Mth.clamp(count, 0, MAX_SADDLES));
    }

    public int getSaddleCount() {
        return this.entityData.get(DATA_ID_SADDLE_COUNT);
    }

    public boolean addSaddle(@Nullable SoundSource source) {
        if (getSaddleCount() >= MAX_SADDLES) return false;
        setSaddleCount(getSaddleCount() + 1);
        if (source != null) {
            this.level().playSound(null, this, SoundEvents.HORSE_SADDLE, source, 1.0F, 1.0F);
        }
        return true;
    }

    public boolean removeSaddle() {
        if (getSaddleCount() <= 0) return false;
        setSaddleCount(getSaddleCount() - 1);
        spawnAtLocation(Items.SADDLE);
        level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 0.8F, 1.0F);
        return true;
    }

    public void dropChestContents(int chestIndex) {
        var chestView = inventory.createChestView(subParts.get(chestIndex), chestIndex);
        for (int i = 0; i < chestView.getContainerSize(); i++) {
            var stack = chestView.getItem(i);
            if (stack.isEmpty()) continue;
            this.spawnAtLocation(stack);
            chestView.setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean isSaddleable() {
        return !isBaby();
    }

    @Override
    public void equipSaddle(ItemStack itemStack, @Nullable SoundSource soundSource) {
        addSaddle(soundSource);
    }

    @Override
    public boolean isSaddled() {
        return getSaddleCount() > 0;
    }

    public void onChestOpen() {
        openCount++;
    }

    public void onChestClose() {
        openCount = Math.max(0, openCount - 1);
    }

    public boolean isInventoryOpen() {
        return openCount > 0;
    }

    public void setChest(int index, boolean value) {
        if (index == 0) this.entityData.set(DATA_ID_CHEST_0, value);
        if (index == 1) this.entityData.set(DATA_ID_CHEST_1, value);
        if (index == 2) this.entityData.set(DATA_ID_CHEST_2, value);
        if (index == 3) this.entityData.set(DATA_ID_CHEST_3, value);
    }

    public boolean hasChest(int index) {
        if (index == 0) return entityData.get(DATA_ID_CHEST_0);
        if (index == 1) return entityData.get(DATA_ID_CHEST_1);
        if (index == 2) return entityData.get(DATA_ID_CHEST_2);
        if (index == 3) return entityData.get(DATA_ID_CHEST_3);
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isBaby()) return super.mobInteract(player, hand);
        var stack = player.getItemInHand(hand);

        if (stack.is(Items.SHEARS) && isSaddled()) {
            if (!level().isClientSide()) {
                removeSaddle();
                if (!player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, player, getSlotForHand(hand));
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide());
        }

        if (stack.is(Items.SADDLE) && getSaddleCount() < MAX_SADDLES) {
            if (!level().isClientSide()) {
                addSaddle(SoundSource.NEUTRAL);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide());
        }

        if (canAddPassenger(player)) {
            doPlayerRide(player);
            return InteractionResult.sidedSuccess(level().isClientSide());
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (passenger instanceof Player player) {
            mountYRot = this.getYRot();
            if (isTame()) return;
            this.tame(player);
            this.setPersistenceRequired();
        }
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof Player player ? player : super.getControllingPassenger();
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity entity) {
        return super.getPassengerRidingPosition(entity).add(0, 0.1f, 0);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return getPassengers().size() <= MAX_SADDLES;
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        if (player.xxa != 0.0F) {
            var delta = this.getDeltaMovement();
            var currentSpeed = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
            var maxSpeed = this.getAttributeValue(Attributes.MOVEMENT_SPEED);
            var speedFactor = (float) Mth.clamp(currentSpeed / maxSpeed, 0.0, 1.0);
            var direction = player.zza < 0.0F ? -1.0F : 1.0F;
            this.mountYRot -= player.xxa * 4.0F * speedFactor * direction;
        }

        this.setRot(mountYRot, getXRot());
        this.yRotO = this.yBodyRot = this.yHeadRot = mountYRot;
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        if (!isSaddled()) return Vec3.ZERO;
        float forward = player.zza;
        if (forward < 0.0F) forward *= 0.5F;
        return new Vec3(0.0F, 0.0F, forward);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction callback) {
        if (!hasPassenger(passenger)) return;
        var firstPassenger = getFirstPassenger() == passenger;
        var walkOffset = bodyWalkOffset(0.33f);
        var riderOffset = rotatedOffset(0f, firstPassenger ? 0.33f : -0.55f);

        var position = this.getPassengerRidingPosition(passenger);
        var point = passenger.getVehicleAttachmentPoint(this);

        callback.accept(passenger,
                position.x - point.x + walkOffset.x + riderOffset.x,
                position.y - point.y,
                position.z - point.z + walkOffset.z + riderOffset.z);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.inventory.save(tag, level().registryAccess());
        tag.putInt("SaddleCount", getSaddleCount());
        tag.putBoolean("HasChest0", hasChest(0));
        tag.putBoolean("HasChest1", hasChest(1));
        tag.putBoolean("HasChest2", hasChest(2));
        tag.putBoolean("HasChest3", hasChest(3));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inventory.load(tag, level().registryAccess());
        this.setSaddleCount(tag.getInt("SaddleCount"));
        this.setChest(0, tag.getBoolean("HasChest0"));
        this.setChest(1, tag.getBoolean("HasChest1"));
        this.setChest(2, tag.getBoolean("HasChest2"));
        this.setChest(3, tag.getBoolean("HasChest3"));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_SADDLE_COUNT, 0);
        builder.define(DATA_ID_CHEST_0, false);
        builder.define(DATA_ID_CHEST_1, false);
        builder.define(DATA_ID_CHEST_2, false);
        builder.define(DATA_ID_CHEST_3, false);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        for (int i = 0; i < 4; i++) {
            if (!hasChest(i)) continue;
            dropChestContents(i);
            spawnAtLocation(Blocks.CHEST);
        }
        for (int i = 0; i < getSaddleCount(); i++) {
            spawnAtLocation(Items.SADDLE);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canStandOnFluid(FluidState state) {
        return state.is(FluidTags.WATER);
    }

    @Override
    public void tick() {
        double prevX = this.getX();
        double prevZ = this.getZ();
        super.tick();
        floatOnWater();
        checkInsideBlocks();
        spawnTrailParticles(prevX, prevZ);
        updatePartPositions();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (tickCount % 20 != 0) return;
        this.heal(1f);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        for (int i = 0; i < subParts.size(); i++) {
            subParts.get(i).setId(i + 1 + packet.getId());
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new ShellbackStrollGoal(this, 1.0));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterWalkerPathNavigation(this, level);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
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
        controllers.add(new AnimationController<>(this, "Default", 10,
                state -> state.setAndContinue(DefaultAnimations.WALK)));
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
        ConfiguredAttribute.applyAttributes(this, CommonConfig.SHELLBACK_ATTRIBUTES);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    protected void doPlayerRide(Player player) {
        if (level().isClientSide) return;
        player.setYRot(this.getYRot());
        player.setXRot(this.getXRot());
        player.startRiding(this);
    }

    protected Vec3 bodyWalkOffset(float distance) {
        float scuttlerWalk = walkAnimation.position(0) * 0.1f;
        float sway = Mth.sin(scuttlerWalk * Mth.PI + 0.2f);
        float yawRad = getYRot() * (Mth.PI / 180F);
        return new Vec3(
                Mth.cos(yawRad) * sway * distance,
                0,
                Mth.sin(yawRad) * sway * distance);
    }

    protected void spawnTrailParticles(double prevX, double prevZ) {
        if (!this.level().isClientSide()) return;

        double dx = this.getX() - prevX;
        double dz = this.getZ() - prevZ;
        if (dx == 0 && dz == 0) return;

        RandomSource rand = this.getRandom();
        int count = 3 + rand.nextInt(3);
        for (int i = 0; i < count; i++) {
            double spawnX = this.getBoundingBox().minX + rand.nextDouble() * this.getBbWidth();
            double spawnZ = this.getBoundingBox().minZ + rand.nextDouble() * this.getBbWidth();
            double spawnY = this.getY();

            BlockPos blockPos = BlockPos.containing(spawnX, spawnY - 0.1, spawnZ);
            BlockState blockState = this.level().getBlockState(blockPos);

            if (blockState.is(Blocks.WATER) || this.isInWater()) {
                this.level().addParticle(
                        ParticleTypes.SPLASH,
                        spawnX, spawnY + 0.25, spawnZ,
                        rand.nextGaussian() * 0.05,
                        0.05 + rand.nextDouble() * 0.1,
                        rand.nextGaussian() * 0.05);
            } else {
                if (blockState.isAir()) continue;
                this.level().addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                        spawnX, spawnY, spawnZ,
                        dx * -3.0 + rand.nextGaussian() * 0.1,
                        0.1 + rand.nextDouble() * 0.2,
                        dz * -3.0 + rand.nextGaussian() * 0.1);
            }
        }
    }

    protected void updatePartPositions() {
        double bodyX = this.getX();
        double bodyY = this.getY() + this.getBbHeight() * 0.4;
        double bodyZ = this.getZ();

        var walkOffset = bodyWalkOffset(0.25f);

        for (ShellbackChestPart part : subParts) {
            var type = part.partType;
            var partOffset = rotatedOffset(
                    ShellbackChestType.SIDE_OFFSET * type.sideSign,
                    ShellbackChestType.FRONT_OFFSET * type.frontSign);

            part.xo = part.getX();
            part.yo = part.getY();
            part.zo = part.getZ();
            part.xOld = part.getX();
            part.yOld = part.getY();
            part.zOld = part.getZ();

            part.setPos(
                    bodyX + walkOffset.x + partOffset.x,
                    bodyY,
                    bodyZ + walkOffset.z + partOffset.z);
        }
    }

    protected Vec3 rotatedOffset(float sideOffset, float frontOffset) {
        float yawRad = this.yBodyRot * (float) (Math.PI / 180.0);
        float cosYaw = Mth.cos(yawRad);
        float sinYaw = Mth.sin(yawRad);
        return new Vec3(
                cosYaw * sideOffset - sinYaw * frontOffset,
                0.0,
                sinYaw * sideOffset + cosYaw * frontOffset);
    }

    private void floatOnWater() {
        if (this.isInWater()) {
            CollisionContext ctx = CollisionContext.of(this);
            if (ctx.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true)
                    && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.WATER)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5F).add(0, 0.05, 0));
            }
        }
    }

    static {
        DATA_ID_SADDLE_COUNT = SynchedEntityData.defineId(Shellback.class, EntityDataSerializers.INT);
        DATA_ID_CHEST_0 = SynchedEntityData.defineId(Shellback.class, EntityDataSerializers.BOOLEAN);
        DATA_ID_CHEST_1 = SynchedEntityData.defineId(Shellback.class, EntityDataSerializers.BOOLEAN);
        DATA_ID_CHEST_2 = SynchedEntityData.defineId(Shellback.class, EntityDataSerializers.BOOLEAN);
        DATA_ID_CHEST_3 = SynchedEntityData.defineId(Shellback.class, EntityDataSerializers.BOOLEAN);
    }
}