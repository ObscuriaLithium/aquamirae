package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.api.common.entity.INoHurtWalkAnim;
import dev.obscuria.aquamirae.common.entity.ai.DummyMeleeAttackGoal;
import dev.obscuria.aquamirae.common.legacy.EntityUtils;
import dev.obscuria.aquamirae.common.entity.animation.AnimController;
import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import dev.obscuria.aquamirae.common.entity.animation.IAnimatable;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import dev.obscuria.aquamirae.network.ClientboundTestPayload;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.registry.AquamiraeParticleTypes;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.server.event.ShipbreakerEvent;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class CaptainCorneliaBoss extends Monster implements IAnimatable, INoHurtWalkAnim {

    private static final EntityDataAccessor<Integer> DATA_ID_PHASE;
    private static final String TAG_NAME_PHASE = "Phase";
    private static final List<WeaponOption> WEAPON_OPTIONS;

    public final ServerBossEvent bossInfo;
    public final AnimState pullAttackState;
    public final AnimState swingAttackState;
    public final AnimState thurstAttackState;
    public final AnimState weaponSwitchState;
    public final AnimState deathState;
    public final AnimController controller;

    private static final int WEAPON_SWITCH_INTERVAL = 400;
    private int weaponSwitchTick;
    private int particle1;
    private int particle2;

    public CaptainCorneliaBoss(EntityType<CaptainCorneliaBoss> type, Level world) {
        super(type, world);
        this.bossInfo = new ServerBossEvent(getDisplayName(),
                ServerBossEvent.BossBarColor.BLUE,
                ServerBossEvent.BossBarOverlay.PROGRESS);
        this.pullAttackState = new AnimState("pull_attack", 30);
        this.swingAttackState = new AnimState("swing_attack", 40);
        this.thurstAttackState = new AnimState("thurst_attack", 40);
        this.weaponSwitchState = new AnimState("weapon_switch", 20);
        this.deathState = new AnimState("death", 60);
        this.controller = new AnimController(this,
                pullAttackState, swingAttackState,
                thurstAttackState, weaponSwitchState,
                deathState);
        this.xpReward = 100;
    }

    public int getPhase() {
        return entityData.get(DATA_ID_PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(DATA_ID_PHASE, phase);
    }

    public boolean isInAction() {
        return pullAttackState.isPlaying()
                || swingAttackState.isPlaying()
                || thurstAttackState.isPlaying()
                || weaponSwitchState.isPlaying();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new DummyMeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, false, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_PHASE, 0);
    }

    @Override
    public AnimController getAnimationController() {
        return controller;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_NAME_PHASE, this.getPhase());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(TAG_NAME_PHASE, Tag.TAG_INT))
            setPhase(tag.getInt(TAG_NAME_PHASE));
    }

    @Override
    public void tick() {
        super.tick();
        this.controller.tick();
    }

    // TODO: Move AI logic to the Goals
    @Override
    public void baseTick() {

        if (pullAttackState.justFinished() && getTarget() != null) swingAttackState.play(this);
        if (swingAttackState.justFinished() && getTarget() != null) thurstAttackState.play(this);
        if (weaponSwitchState.getTick() == 10 && !level().isClientSide()) equip(chooseOptimalWeaponLoadout());

        this.swingAttackState.playSound(this, 1, AquamiraeSounds.ENTITY_CAPTAIN_CORNELIA_ATTACK_1, SoundSource.HOSTILE, 2f, 1f);
        this.thurstAttackState.playSound(this, 1, AquamiraeSounds.ENTITY_CAPTAIN_CORNELIA_ATTACK_2, SoundSource.HOSTILE, 2f, 1f);
        this.weaponSwitchState.playSound(this, 8, SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.HOSTILE, 1f, 1f);
        this.weaponSwitchTick++;

        if (this.isDeadOrDying()) this.removeAllEffects();
        if (this.getHealth() < this.getMaxHealth() * 0.25 && this.getEntityData().get(DATA_ID_PHASE) < 1 && !this.hasEffect(MobEffects.LEVITATION))
            this.rage();
        if (this.getHealth() <= 20 && !this.hasEffect(MobEffects.WEAKNESS)) this.heal(0.1F);

        if (this.getTarget() != null) {
            final LivingEntity target = this.getTarget();
            final double distance = this.distanceToSqr(target);
            modifyYRot(EntityUtils.getYAngleBetween(this, target), this.onGround() ? 0.15f : 0.3f);
            this.lookControl.setLookAt(target);

            if (!this.isInAction() && !this.level().isClientSide) {
                if (this.weaponSwitchTick > WEAPON_SWITCH_INTERVAL
                        && random.nextFloat() < (this.weaponSwitchTick - WEAPON_SWITCH_INTERVAL) / 5000f) {
                    this.weaponSwitchState.play(this, 20);
                    this.weaponSwitchTick = 0;
                } else if (distance < 3 || this.hasEffect(MobEffects.LEVITATION) || (distance < 25 && random.nextInt(100) == 1)) {
                    if (random.nextBoolean()) this.swingAttackState.play(this, 40);
                    else this.thurstAttackState.play(this, 40);
                } else if (distance < 600 && random.nextInt(200) == 1) this.pullAttackState.play(this, 30);
            }

            if (this.pullAttackState.getTick() == 10) {
                target.setDeltaMovement(target.position().vectorTo(getEyePosition()).scale(0.25F).add(0, 0.05f, 0));
                if (target instanceof Player player) player.hurtMarked = true;
            }
            if (this.swingAttackState.getTick() == 12 || this.thurstAttackState.getTick() == 12)
                this.setDeltaMovement(new Vec3(this.getX(), this.getY(), this.getZ())
                        .vectorTo(new Vec3(target.getX(), target.getY() + 0.1F, target.getZ())).scale(0.2F));
            if (this.swingAttackState.getTick() == 14 && distance < 9)
                EntityUtils.getRelativeEntities(this, LivingEntity.class,
                        5f, 0f, 0f, 6f, true, true).forEach(e -> {if (e != this) this.doHurtTarget(e);});
            if (this.thurstAttackState.getTick() == 14 && distance < 12) this.doHurtTarget(target);

            if (this.isUnderWater() && !this.hasEffect(MobEffects.LEVITATION))
                this.setDeltaMovement(new Vec3(this.getX(), this.getY(), this.getZ())
                        .vectorTo(new Vec3(target.getX(), target.getY(), target.getZ())).scale(0.04F));
            else if (this.isInWater() && !this.hasEffect(MobEffects.LEVITATION))
                this.setDeltaMovement(new Vec3(this.getX(), this.getY(), this.getZ())
                        .vectorTo(new Vec3(target.getX(), target.getY(), target.getZ())).scale(0.04F).add(0F, 0.08F, 0F));
        }

        if (this.isInAction())
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 3, false, false));

        if (this.hasEffect(MobEffects.LEVITATION)) {
            this.heal(2);
            if (!this.level().isClientSide()) {
                final Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(10), e -> true).stream()
                        .sorted(Comparator.comparingDouble(ent -> ent.distanceToSqr(center))).toList();
                final MobEffectInstance LEVITATION = this.getEffect(MobEffects.LEVITATION);
                final int DURATION = LEVITATION == null ? 0 : LEVITATION.getDuration();
                list.forEach(entity -> {
                    if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) return;
                    if (entity.getMaxHealth() <= 100) {
                        if (DURATION > 1) {
                            final double radius = 5;
                            final Vec3 orbit = new Vec3(center.x + Math.cos(entity.tickCount * -0.1F) * radius, center.y,
                                    center.z + Math.sin(entity.tickCount * -0.1F) * radius);
                            entity.setDeltaMovement(new Vec3(entity.getX(), entity.getY(), entity.getZ()).vectorTo(orbit).scale(0.2F));
                            if (entity instanceof Player _player) _player.hurtMarked = true;
                        } else if (DURATION == 1) {
                            entity.setDeltaMovement(new Vec3(this.getX(), this.getY(), this.getZ())
                                    .vectorTo(new Vec3(entity.getX(), entity.getY(), entity.getZ())).scale(0.2F));
                            if (entity instanceof Player _player) _player.hurtMarked = true;
                        }
                    }
                });

                this.particle1++;
                if (this.level() instanceof ServerLevel server && this.particle1 > 1) {
                    this.particle1 = 0;
                    var particleType = Objects.requireNonNull(AquamiraeParticleTypes.GHOST.simple());
                    server.sendParticles(particleType, getX(), getY() - 0.2, getZ(), 1, 0.3, 0.1, 0.3, 0.1);
                }
                if (this.isInWater()) this.setDeltaMovement(new Vec3(0F, 0.4F, 0F));
            }
        }
        this.particle2++;

        if (level() instanceof ServerLevel serverLevel && this.particle2 > 9) {
            this.particle2 = 0;
            var ghostShine = Objects.requireNonNull(AquamiraeParticleTypes.GHOST_SHINE.simple());
            serverLevel.sendParticles(ghostShine, getX(), getY() + 2, getZ(), 1, 0.15, 0.1, 0.15, 0.1);
        }

//        if (this.level().isClientSide) {
//            final Vec3 center = this.position();
//            this.level().getEntitiesOfClass(Player.class, new AABB(center, center).inflate(32)).forEach(AquamiraeClient::playCorneliaMusic);
//        }
        super.baseTick();
    }

    private void modifyYRot(float rotation, float mod) {
        this.yRotO = this.getYRot();
        this.setYRot(this.getYRot() + (rotation - this.getYRot()) * mod);
    }

    public Item getSeasonalWeapon() {
        return AquamiraeHelper.isWinterEventActive()
                ? AquamiraeItems.JAWBREAKER.get()
                : AquamiraeItems.TIDEPIERCER.get();
    }

    private void equip(WeaponLoadout loadout) {
        setItemSlot(EquipmentSlot.MAINHAND, loadout.mainhand);
        setItemSlot(EquipmentSlot.OFFHAND, loadout.offhand);
    }

    private WeaponLoadout chooseOptimalWeaponLoadout() {
        @Nullable var target = getTarget();
        if (target == null) return getDefaultWeaponLoadout();
        var context = new LoadoutContext(this, target);
        @Nullable WeaponOption bestOption = null;
        int total = 0;

        for (var option : WEAPON_OPTIONS) {
            int weight = option.weightFunction.applyAsInt(context);
            if (weight <= 0) continue;

            total += weight;
            if (random.nextInt(total) < weight) {
                bestOption = option;
            }
        }

        return bestOption != null
                ? bestOption.loadoutFunction.apply(context)
                : getDefaultWeaponLoadout();
    }

    private WeaponLoadout getDefaultWeaponLoadout() {
        return WeaponLoadout.mainhand(getSeasonalWeapon());
    }

    public void rage() {
        this.getEntityData().set(DATA_ID_PHASE, this.getEntityData().get(DATA_ID_PHASE) + 1);
        this.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 0, false, false));
        if (this.level() instanceof ServerLevel level) {
            var pos = this.blockPosition();
            LightningBolt entityToSpawn = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            entityToSpawn.moveTo(Vec3.atBottomCenterOf(pos));
            entityToSpawn.setVisualOnly(true);
            level.addFreshEntity(entityToSpawn);
            level.playSound(null, pos, AquamiraeSounds.ENTITY_CAPTAIN_CORNELIA_HORN, SoundSource.HOSTILE, 3, 1);
            level.playSound(null, pos, AquamiraeSounds.ENTITY_CAPTAIN_CORNELIA_RAGE, SoundSource.HOSTILE, 4, 1);
            ShipbreakerEvent.trigger(level, position());
            for (var player : level.getEntitiesOfClass(ServerPlayer.class, getBoundingBox().inflate(64f))) {
                FragmentumNetworking.sendTo(player, ClientboundTestPayload.MAZE_MOTHER_ENCOUNTER);
            }
        }
    }

    @Override
    protected void dropEquipment() {
        if (level() instanceof ServerLevel server) {
            if (Math.random() <= 0.2F) {
                final ItemEntity item = new ItemEntity(EntityType.ITEM, server);
                item.setItem(getSeasonalWeapon().getDefaultInstance());
                item.moveTo(position());
                server.addFreshEntity(item);
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return AquamiraeSounds.ENTITY_CAPTAIN_CORNELIA_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return AquamiraeSounds.ENTITY_CAPTAIN_CORNELIA_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return AquamiraeSounds.ENTITY_CAPTAIN_CORNELIA_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof AbstractArrow) return false;
        if (source.is(DamageTypes.FALL)) return false;
        if (source.is(DamageTypes.CACTUS)) return false;
        if (source.is(DamageTypes.DROWN)) return false;
        if (source.is(DamageTypes.LIGHTNING_BOLT)) return false;
        if (source.is(DamageTypes.EXPLOSION)) return false;
        if (source.is(DamageTypes.TRIDENT)) return false;
        return super.hurt(source, amount);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level, DifficultyInstance difficulty,
            MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        this.equip(getDefaultWeaponLoadout());
        this.playSound(AquamiraeSounds.ENTITY_CAPTAIN_CORNELIA_HORN, 3, 1);
        this.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 120, 0, false, false));
        ConfiguredAttribute.applyAttributes(this, CommonConfig.CAPTAIN_CORNELIA_ATTRIBUTES);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected void tickDeath() {
        this.controller.tickDeath(deathState);
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    static {
        DATA_ID_PHASE = SynchedEntityData.defineId(CaptainCorneliaBoss.class, EntityDataSerializers.INT);
        WEAPON_OPTIONS = List.of(
                new WeaponOption(
                        ctx -> 100,
                        ctx -> ctx.self().getDefaultWeaponLoadout()),
                new WeaponOption(
                        ctx -> 100,
                        ctx -> WeaponLoadout.dual(AquamiraeItems.TERRIBLE_BLADE.get())),
                new WeaponOption(
                        ctx -> 2 * (int) ctx.target.getHealth(),
                        ctx -> WeaponLoadout.dual(AquamiraeItems.SHATTERBLADE.get())),
                new WeaponOption(
                        ctx -> 20 * ctx.target.getArmorValue(),
                        ctx -> WeaponLoadout.mainhand(AquamiraeItems.SHATTERSPIKE.get())),
                new WeaponOption(
                        ctx -> (int) Mth.lerp(ctx.self.getHealth() / ctx.self.getMaxHealth(), 500, 0),
                        ctx -> WeaponLoadout.dual(AquamiraeItems.REMNANT_SABER.get())));
    }

    private record LoadoutContext(CaptainCorneliaBoss self, LivingEntity target) {}

    private record WeaponLoadout(ItemStack mainhand, ItemStack offhand) {

        public static WeaponLoadout mainhand(Item stack) {
            return new WeaponLoadout(stack.getDefaultInstance(), ItemStack.EMPTY);
        }

        public static WeaponLoadout offhand(Item stack) {
            return new WeaponLoadout(ItemStack.EMPTY, stack.getDefaultInstance());
        }

        public static WeaponLoadout dual(Item stack) {
            return new WeaponLoadout(stack.getDefaultInstance(), stack.getDefaultInstance());
        }
    }

    private record WeaponOption(
            ToIntFunction<LoadoutContext> weightFunction,
            Function<LoadoutContext, WeaponLoadout> loadoutFunction
    ) {}
}
