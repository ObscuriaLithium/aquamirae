package dev.obscuria.aquamirae.common.component;

import com.mojang.serialization.Codec;
import dev.obscuria.aquamirae.api.common.TooltipImageProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record RuneProtection() implements TooltipImageProvider, TooltipComponent {

    public static final RuneProtection SHARED = new RuneProtection();
    public static final Codec<RuneProtection> CODEC = Codec.unit(SHARED);
    public static final StreamCodec<RegistryFriendlyByteBuf, RuneProtection> STREAM_CODEC = StreamCodec.unit(SHARED);

    @Override
    public TooltipComponent getTooltipImage(ItemStack stack) {
        return this;
    }
}
