package dev.obscuria.aquamirae.common.component;

import com.mojang.serialization.Codec;
import dev.obscuria.aquamirae.api.common.TooltipImageProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record Keepsake(String owner) implements TooltipComponent, TooltipImageProvider {

    public static final Codec<Keepsake> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Keepsake> STREAM_CODEC;

    @Override
    public TooltipComponent getTooltipImage(ItemStack stack) {
        return this;
    }

    @Override
    public int priority() {
        return 100;
    }

    static {
        CODEC = Codec.STRING.xmap(Keepsake::new, Keepsake::owner);
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Keepsake::owner,
                Keepsake::new);
    }
}
