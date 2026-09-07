package dev.obscuria.aquamirae.integration.mixin.client;

import dev.obscuria.aquamirae.integration.hook.client.HookClientLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick$onTail(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        HookClientLevel.onTick((ClientLevel) (Object) this);
    }
}
