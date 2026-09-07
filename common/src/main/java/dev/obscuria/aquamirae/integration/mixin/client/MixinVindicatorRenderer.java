package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import net.minecraft.client.renderer.entity.VindicatorRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vindicator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VindicatorRenderer.class)
public abstract class MixinVindicatorRenderer {

    @ModifyReturnValue(require = 0, method = "getTextureLocation(Lnet/minecraft/world/entity/monster/Vindicator;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"))
    private ResourceLocation aquamirae$modifyTextureLocation(
            ResourceLocation original,
            @Local(argsOnly = true) Vindicator vindicator
    ) {
        if (!AquamiraeHelper.isInIceMaze(vindicator)) return original;
        return AquamiraeClient.PIRATE_VINDICATOR_TEXTURE;
    }
}
