package dev.obscuria.aquamirae.integration.mixin.server;

import dev.obscuria.aquamirae.integration.hook.server.HookServerPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer {

    @Inject(method = "restoreFrom", at = @At("HEAD"))
    private void restoreFrom$onHead(ServerPlayer that, boolean keepEverything, CallbackInfo ci) {
        HookServerPlayer.restoreFrom((ServerPlayer) (Object) this, that, keepEverything);
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void die$cancelDeath(DamageSource cause, CallbackInfo ci) {
        if (!HookServerPlayer.cancelDeath((ServerPlayer) (Object) this, cause)) return;
        ci.cancel();
    }
}