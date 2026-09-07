package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.config.early.EarlyConfig;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen {

    @ModifyExpressionValue(require = 0, method = "renderPanorama", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/TitleScreen;PANORAMA:Lnet/minecraft/client/renderer/PanoramaRenderer;"))
    private PanoramaRenderer renderPanorama$modifyPanorama(PanoramaRenderer original) {
        return EarlyConfig.THEMED_MAIN_MENU.get()
                ? AquamiraeClient.PANORAMA
                : original;
    }
}
