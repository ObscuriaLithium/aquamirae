package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.integration.hook.client.HookItemRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @ModifyVariable(require = 0, method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int render$modifyPackedLight(int packedLight, @Local(argsOnly = true) ItemStack stack) {
        return HookItemRenderer.modifyPackedLight(stack, packedLight);
    }
}
