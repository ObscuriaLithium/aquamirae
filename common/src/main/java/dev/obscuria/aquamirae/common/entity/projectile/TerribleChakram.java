package dev.obscuria.aquamirae.common.entity.projectile;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class TerribleChakram extends AbstractChakram {

    public TerribleChakram(EntityType<? extends TerribleChakram> type, Level level) {
        super(type, level, 20);
    }

    @Override
    protected void onHurtEntity(EntityHitResult result) {
        if (!(result.getEntity() instanceof LivingEntity entity)) return;
        entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
    }
}
