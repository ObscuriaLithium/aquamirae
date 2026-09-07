package dev.obscuria.aquamirae.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ItemTooltip(Component content, int insertAt, boolean emptyBefore, boolean emptyAfter) {

    public static final Codec<ItemTooltip> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemTooltip> STREAM_CODEC;

    public static ItemTooltip translate(String key) {
        return translate(key, 1, false, false);
    }

    public static ItemTooltip translateEmptyAfter(String key) {
        return translate(key, 1, false, true);
    }

    public static ItemTooltip translate(String key, int insertAt, boolean emptyBefore, boolean emptyAfter) {
        return new ItemTooltip(Component.translatable(key), insertAt, emptyBefore, emptyAfter);
    }

    static {
        CODEC = RecordCodecBuilder.create(builder -> builder.group(
                ComponentSerialization.CODEC.fieldOf("content").forGetter(ItemTooltip::content),
                Codec.INT.optionalFieldOf("insert_at", 1).forGetter(ItemTooltip::insertAt),
                Codec.BOOL.optionalFieldOf("empty_before", false).forGetter(ItemTooltip::emptyBefore),
                Codec.BOOL.optionalFieldOf("empty_after", false).forGetter(ItemTooltip::emptyAfter)
        ).apply(builder, ItemTooltip::new));
        STREAM_CODEC = StreamCodec.composite(
                ComponentSerialization.STREAM_CODEC, ItemTooltip::content,
                ByteBufCodecs.INT, ItemTooltip::insertAt,
                ByteBufCodecs.BOOL, ItemTooltip::emptyBefore,
                ByteBufCodecs.BOOL, ItemTooltip::emptyAfter,
                ItemTooltip::new);
    }
}
