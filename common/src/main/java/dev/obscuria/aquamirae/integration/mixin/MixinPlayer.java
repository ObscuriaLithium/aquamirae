package dev.obscuria.aquamirae.integration.mixin;

import dev.obscuria.aquamirae.integration.hook.HookLivingEntity;
import dev.obscuria.aquamirae.integration.hook.HookPlayer;
import dev.obscuria.aquamirae.integration.extension.ExtensionPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements ExtensionPlayer {

    @Unique private static final EntityDataAccessor<Float> DATA_REMNANT_SABER_BONUS;

    private MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public float aquamirae$getRemnantSaberBonus() {
        var self = (Player) (Object) this;
        return self.getEntityData().get(DATA_REMNANT_SABER_BONUS);
    }

    @Override
    public void aquamirae$setRemnantSaberBonus(float value) {
        var self = (Player) (Object) this;
        self.getEntityData().set(DATA_REMNANT_SABER_BONUS, Math.max(value, 0f));
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    private void defineSynchedData$onHead(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_REMNANT_SABER_BONUS, 0f);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData$onTail(CompoundTag compound, CallbackInfo ci) {
        var aquamiraeData = new CompoundTag();
        aquamiraeData.putFloat("RemnantSaberBonus", aquamirae$getRemnantSaberBonus());
        compound.put("AquamiraeData", aquamiraeData);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData$onTail(CompoundTag compound, CallbackInfo ci) {
        var aquamiraeData = compound.getCompound("AquamiraeData");
        if (aquamiraeData.contains("RemnantSaberBonus", Tag.TAG_FLOAT))
            aquamirae$setRemnantSaberBonus(aquamiraeData.getFloat("RemnantSaberBonus"));
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;gameEvent(Lnet/minecraft/core/Holder;)V"))
    private void actuallyHurt$onDamageEvent(DamageSource damageSrc, float damageAmount, CallbackInfo ci) {
        HookLivingEntity.onHurt((Player) (Object) this, damageSrc);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick$onTail(CallbackInfo ci) {
        HookPlayer.tick((Player) (Object) this);
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void isInvulnerableTo$modifyResult(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (HookPlayer.isInvulnerableTo((Player) (Object) this, source))
            cir.setReturnValue(true);
    }

    static {
        DATA_REMNANT_SABER_BONUS = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
    }
}
