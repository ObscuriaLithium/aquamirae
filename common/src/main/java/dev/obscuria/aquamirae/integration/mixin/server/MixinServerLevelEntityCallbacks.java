package dev.obscuria.aquamirae.integration.mixin.server;

import dev.obscuria.aquamirae.integration.hook.server.HookServerLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.level.ServerLevel$EntityCallbacks")
public abstract class MixinServerLevelEntityCallbacks {

    @Shadow(aliases = "field_26936") @Final ServerLevel this$0;

    @Inject(method = "onTrackingStart(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateDynamicGameEventListener(Ljava/util/function/BiConsumer;)V", shift = At.Shift.BEFORE))
    private void onTrackingStart$onUpdateDynamicGameEventListener(Entity p_143371_, CallbackInfo ci) {
        HookServerLevel.OnTrackingStart(this$0, p_143371_);
    }

    @Inject(method = "onTrackingEnd(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateDynamicGameEventListener(Ljava/util/function/BiConsumer;)V", shift = At.Shift.BEFORE))
    private void onTrackingEnd$onUpdateDynamicGameEventListener(Entity p_143375_, CallbackInfo ci) {
        HookServerLevel.OnTrackingEnd(this$0, p_143375_);
    }
}