package dev.obscuria.aquamirae.integration.mixin.client;

import dev.obscuria.aquamirae.integration.hook.client.HookMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void setLevel$onHead(ClientLevel level, ReceivingLevelScreen.Reason reason, CallbackInfo ci) {
        HookMinecraft.onSetLevel((Minecraft) (Object) this);
    }

    @Inject(method = "runTick(Z)V", at = @At("TAIL"))
    private void runTick$onTail(boolean renderLevel, CallbackInfo ci) {
        HookMinecraft.onRunTick((Minecraft) (Object) this);
    }

    @Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
    private void getSituationalMusic$onHead(CallbackInfoReturnable<Music> cir) {
        HookMinecraft.supplyBossMusic((Minecraft) (Object) this, cir::setReturnValue);
    }

    @Inject(method = "getSituationalMusic", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;", shift = At.Shift.AFTER), cancellable = true)
    private void getSituationalMusic$onGetBiome(CallbackInfoReturnable<Music> cir) {
        HookMinecraft.supplyGameplayMusic((Minecraft) (Object) this, cir::setReturnValue);
    }

    @Inject(method = "getSituationalMusic", at = @At("RETURN"), cancellable = true)
    private void getSituationalMusic$onReturn(CallbackInfoReturnable<Music> cir) {
        HookMinecraft.supplyMenuMusic((Minecraft) (Object) this, cir.getReturnValue(), cir::setReturnValue);
    }
}