package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import net.minecraft.client.renderer.entity.PillagerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Pillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PillagerRenderer.class)
public abstract class MixinPillagerRenderer {

    @ModifyReturnValue(require = 0, method = "getTextureLocation(Lnet/minecraft/world/entity/monster/Pillager;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"))
    private ResourceLocation getTextureLocation$modifyReturnValue(
            ResourceLocation original,
            @Local(argsOnly = true) Pillager pillager
    ) {
        if (!AquamiraeHelper.isInIceMaze(pillager)) return original;
        return AquamiraeClient.PIRATE_PILLAGER_TEXTURE;
    }
}
