package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.AquamiraeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AbyssalScyphoid extends WaterAnimal {

    public AbyssalScyphoid(EntityType<? extends AbyssalScyphoid> type, Level level) {
        super(type, level);
    }

    public boolean isSquishTick() {
        var variant = getAttributeValue(Attributes.SCALE);
        var cycle = 20 + 60 * variant;
        var offset = cycle * 0.05f;
        return (int) ((tickCount + 1000 * variant) % cycle) == (int) offset;
    }

    @Override
    public void travel(Vec3 motion) {
        // No movement
    }

    @Override
    public void tick() {
        super.tick();

        if (!isInWaterOrBubble()) {
            hurt(damageSources().dryOut(), 1000f);
        }

        if (level().isClientSide && !isSilent() && isSquishTick()) {
            level().playLocalSound(blockPosition(),
                    SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.HOSTILE,
                    3, 2f - (float) getAttributeValue(Attributes.SCALE), false);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (super.hurt(source, damage)) {
            doPoison(source.getDirectEntity());
            return true;
        }
        return false;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level, DifficultyInstance difficulty,
            MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).setBaseValue(Math.round(16 * getAttributeValue(Attributes.SCALE)));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COD_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected void doPush(Entity entity) {
        doPoison(entity);
    }

    private void doPoison(@Nullable Entity entity) {
        if (!(entity instanceof LivingEntity living)) return;
        if (living instanceof AbyssalScyphoid) return;
        if (living instanceof Anglerfish) return;
        if (!AquamiraeHelper.doPoison(this, living, 60, 2)) return;
        living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
        level().playSound(null, living, SoundEvents.SQUID_SQUIRT, SoundSource.HOSTILE, 0.5f, 1);
    }

    public static boolean checkSpawnRules(
            EntityType<AbyssalScyphoid> type,
            ServerLevelAccessor levelAccessor,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random
    ) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(type, levelAccessor, spawnType, pos, random);
    }
}
