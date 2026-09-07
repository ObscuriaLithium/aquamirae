package dev.obscuria.aquamirae.fabric.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.integration.hook.client.HookBossHealthOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public abstract class MixinBossHealthOverlay {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V"))
    private void render$onDrawBar(
            GuiGraphics guiGraphics, CallbackInfo ci,
            @Local LerpingBossEvent event,
            @Local(ordinal = 2) int x,
            @Local(ordinal = 1) int y
    ) {
        HookBossHealthOverlay.drawBar(event, guiGraphics, x, y);
    }
}
