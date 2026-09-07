package dev.obscuria.aquamirae.common.component;

import com.mojang.serialization.Codec;
import dev.obscuria.aquamirae.api.common.TooltipImageProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record RuneDamage() implements TooltipImageProvider, TooltipComponent {

    public static final RuneDamage SHARED = new RuneDamage();
    public static final Codec<RuneDamage> CODEC = Codec.unit(SHARED);
    public static final StreamCodec<RegistryFriendlyByteBuf, RuneDamage> STREAM_CODEC = StreamCodec.unit(SHARED);

    @Override
    public TooltipComponent getTooltipImage(ItemStack stack) {
        return this;
    }
}
