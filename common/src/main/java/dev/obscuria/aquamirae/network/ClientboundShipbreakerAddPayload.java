package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public record ClientboundShipbreakerAddPayload(UUID eventId, double headIndex, List<Vec3> path) implements CustomPacketPayload {

    public static final Type<ClientboundShipbreakerAddPayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundShipbreakerAddPayload> STREAM_CODEC;

    public static void handle(Player player, ClientboundShipbreakerAddPayload payload) {
        ClientPayloadListener.handleShipbreakerAdd(player, payload);
    }

    @Override
    public Type<ClientboundShipbreakerAddPayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(Aquamirae.id("shipbreaker_add"));
        STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, ClientboundShipbreakerAddPayload::eventId,
                ByteBufCodecs.DOUBLE, ClientboundShipbreakerAddPayload::headIndex,
                ByteBufCodecs.fromCodec(Vec3.CODEC).apply(ByteBufCodecs.list()), ClientboundShipbreakerAddPayload::path,
                ClientboundShipbreakerAddPayload::new);
    }
}
