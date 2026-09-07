package dev.obscuria.aquamirae.integration.hook.client;

import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.item.ItemStack;

public final class HookItemRenderer {

    public static int modifyPackedLight(ItemStack stack, int packedLight) {
        return stack.has(AquamiraeDataComponents.NO_SHADING)
                ? LightTexture.FULL_BRIGHT
                : packedLight;
    }
}
