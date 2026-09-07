package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record ClientboundShipbreakerRemovePayload(UUID eventId) implements CustomPacketPayload {

    public static final Type<ClientboundShipbreakerRemovePayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundShipbreakerRemovePayload> STREAM_CODEC;

    public static void handle(Player player, ClientboundShipbreakerRemovePayload payload) {
        ClientPayloadListener.handleShipbreakerRemove(player, payload);
    }

    @Override
    public Type<ClientboundShipbreakerRemovePayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(Aquamirae.id("shipbreaker_remove"));
        STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, ClientboundShipbreakerRemovePayload::eventId,
                ClientboundShipbreakerRemovePayload::new);
    }
}
