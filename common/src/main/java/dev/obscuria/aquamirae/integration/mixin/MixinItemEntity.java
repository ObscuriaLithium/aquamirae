package dev.obscuria.aquamirae.integration.mixin;

import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity {

    @Inject(method = "setItem", at = @At("RETURN"))
    private void setItem$onReturn(ItemStack stack, CallbackInfo info) {
        var self = (ItemEntity) (Object) this;
        if (!stack.has(AquamiraeDataComponents.NO_GRAVITY)) return;
        self.setNoGravity(true);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void tick$onSuperTick(CallbackInfo info) {
        var self = (ItemEntity) (Object) this;
        if (!self.getItem().has(AquamiraeDataComponents.NO_GRAVITY)) return;
        self.setDeltaMovement(self.getDeltaMovement().multiply(1f, 0.95f, 1f));
    }
}
