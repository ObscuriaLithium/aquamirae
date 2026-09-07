package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public record ClientboundAnimationStopPayload(int entityId, String animationName) implements CustomPacketPayload {

    public static final Type<ClientboundAnimationStopPayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAnimationStopPayload> STREAM_CODEC;

    public static void handle(Player player, ClientboundAnimationStopPayload payload) {
        ClientPayloadListener.handleAnimationStop(player, payload);
    }


    @Override
    public Type<ClientboundAnimationStopPayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(Aquamirae.id("animation_stop"));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, ClientboundAnimationStopPayload::entityId,
                ByteBufCodecs.STRING_UTF8, ClientboundAnimationStopPayload::animationName,
                ClientboundAnimationStopPayload::new);
    }
}