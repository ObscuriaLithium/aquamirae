package dev.obscuria.aquamirae.common.entity.projectile;

import com.google.common.base.Suppliers;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class AbyssalShard extends AbstractHomingShard {

    private static final Supplier<ItemStack> PARTICLE_ITEM;

    public AbyssalShard(EntityType<? extends AbyssalShard> type, Level level) {
        super(type, level);
    }

    public static AbyssalShard create(Level level, @Nullable Entity owner, Vec3 origin, Vec3 motion) {
        var shard = new AbyssalShard(AquamiraeEntityTypes.ABYSSAL_SHARD.get(), level);
        shard.setOwner(owner);
        shard.setDeltaMovement(motion);
        shard.setPos(origin);
        level.addFreshEntity(shard);
        return shard;
    }

    @Override
    public int getHomingRadius() {
        return 16;
    }

    @Override
    protected void tickShard() {
        if (this.level().isClientSide()) return;
        final var strength = Math.min(1, this.tickCount / (30.0 * this.getShardScale()));
        this.setHomingStrength(0.1f * (float) Math.pow(strength, 3.0));
    }

    @Override
    protected void makeParticles() {}

    @Override
    public float getDamage(Entity entity) {
        return 10f * this.getShardScale();
    }

    @Override
    protected boolean canTargetEntity(Entity entity) {
        return super.canTargetEntity(entity) && isMonsterOrHostile(entity);
    }

    @Override
    protected void doPostHurtEffects(EntityHitResult result) {
        final var entity = result.getEntity();
        if (!(entity instanceof LivingEntity living)) return;
        living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 50, 0));
    }

    @Override
    public void onBreak() {
        if (!(this.level() instanceof ServerLevel level)) return;
        level.playSound(null, this.blockPosition(),
                SoundEvents.AMETHYST_BLOCK_BREAK,
                SoundSource.NEUTRAL,
                1f, 1f);
        level.sendParticles(
                new ItemParticleOption(ParticleTypes.ITEM, PARTICLE_ITEM.get()),
                this.getX(), this.getY(), this.getZ(),
                8, 0, 0, 0, 0.15);
        if (getShardScale() > 0.75f) {
            for (var i = 0; i < 3; i++) {
                var motion = this.getDeltaMovement().scale(-1f)
                        .xRot((float) getRandom().triangle(0.0, 0.75))
                        .yRot((float) getRandom().triangle(0.0, 0.75))
                        .scale((float) getRandom().triangle(0.9, 0.4));
                var shard = AbyssalShard.create(level(), getOwner(), position(), motion);
                shard.setShardScale(0.5f);
            }
        }
    }

    private boolean isMonsterOrHostile(Entity entity) {
        return entity instanceof Monster || isHostile(entity);
    }

    private boolean isHostile(Entity entity) {
        return this.getOwner() != null
                && entity instanceof Mob mob
                && mob.getTarget() == this.getOwner();
    }

    static {
        PARTICLE_ITEM = Suppliers.memoize(AquamiraeItems.ABYSSAL_AMETHYST::instantiate);
    }
}
