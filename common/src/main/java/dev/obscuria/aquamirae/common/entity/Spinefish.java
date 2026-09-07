package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class Spinefish extends AbstractSchoolingFish {

    public Spinefish(EntityType<? extends Spinefish> type, Level level) {
        super(type, level);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return Items.AIR.getDefaultInstance();
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
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level, DifficultyInstance difficulty,
            MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        ConfiguredAttribute.applyAttributes(this, CommonConfig.SPINEFISH_ATTRIBUTES);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public static boolean checkSpawnRules(
            EntityType<Spinefish> type, ServerLevelAccessor levelAccessor,
            MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(type, levelAccessor, spawnType, pos, random);
    }
}
