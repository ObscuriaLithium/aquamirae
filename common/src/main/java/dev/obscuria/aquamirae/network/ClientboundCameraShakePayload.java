package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public record ClientboundCameraShakePayload(float strength, float duration) implements CustomPacketPayload {

    public static final Type<ClientboundCameraShakePayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCameraShakePayload> STREAM_CODEC;

    public static void handle(Player player, ClientboundCameraShakePayload payload) {
        ClientPayloadListener.handleCameraShake(player, payload);
    }

    @Override
    public Type<ClientboundCameraShakePayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(Aquamirae.id("camera_shake"));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, ClientboundCameraShakePayload::duration,
                ByteBufCodecs.FLOAT, ClientboundCameraShakePayload::duration,
                ClientboundCameraShakePayload::new);
    }
}
