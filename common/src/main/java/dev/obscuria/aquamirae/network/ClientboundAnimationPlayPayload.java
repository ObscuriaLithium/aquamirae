package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public record ClientboundAnimationPlayPayload(int entityId, String animationName, int startTick, int duration) implements CustomPacketPayload {

    public static final Type<ClientboundAnimationPlayPayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAnimationPlayPayload> STREAM_CODEC;

    public static void handle(Player player, ClientboundAnimationPlayPayload payload) {
        ClientPayloadListener.handleAnimationPlay(player, payload);
    }

    @Override
    public Type<ClientboundAnimationPlayPayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(Aquamirae.id("animation_play"));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, ClientboundAnimationPlayPayload::entityId,
                ByteBufCodecs.STRING_UTF8, ClientboundAnimationPlayPayload::animationName,
                ByteBufCodecs.INT, ClientboundAnimationPlayPayload::startTick,
                ByteBufCodecs.INT, ClientboundAnimationPlayPayload::duration,
                ClientboundAnimationPlayPayload::new);
    }
}