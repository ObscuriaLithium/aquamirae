package dev.obscuria.aquamirae.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record LogBookContents(Component topic, Component content) implements TooltipProvider {

    public static final Codec<LogBookContents> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, LogBookContents> STREAM_CODEC;
    public static final LogBookContents EMPTY;

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag isAdvanced) {
        consumer.accept(topic.copy().withStyle(ChatFormatting.GRAY));
    }

    static {
        EMPTY = new LogBookContents(CommonComponents.EMPTY, CommonComponents.EMPTY);
        CODEC = RecordCodecBuilder.create(builder -> builder.group(
                ComponentSerialization.CODEC.fieldOf("topic").forGetter(LogBookContents::topic),
                ComponentSerialization.CODEC.fieldOf("content").forGetter(LogBookContents::content)
        ).apply(builder, LogBookContents::new));
        STREAM_CODEC = StreamCodec.composite(
                ComponentSerialization.STREAM_CODEC, LogBookContents::topic,
                ComponentSerialization.STREAM_CODEC, LogBookContents::content,
                LogBookContents::new);
    }
}
