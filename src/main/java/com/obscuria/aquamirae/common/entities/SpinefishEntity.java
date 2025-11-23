package com.obscuria.aquamirae.common.entities;

import com.obscuria.aquamirae.registry.AquamiraeItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

@ShipGraveyardEntity
public class SpinefishEntity extends SchoolingFishEntity {

    public SpinefishEntity(EntityType<? extends SpinefishEntity> type, World world) {
        super(type, world);
    }

    public ItemStack getBucketItem() {
        return AquamiraeItems.SPINEFISH_BUCKET.getDefaultStack();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COD_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_COD_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }

    public static SpawnRestriction.SpawnPredicate<SpinefishEntity> getSpawnRules() {
        return WaterCreatureEntity::canSpawn;
    }
}
