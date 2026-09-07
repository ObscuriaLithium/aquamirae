package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record ClientboundTravelPayload(ResourceKey<Level> from, ResourceKey<Level> to) implements CustomPacketPayload {

    public static final Type<ClientboundTravelPayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTravelPayload> STREAM_CODEC;

    public static void handle(Player player, ClientboundTravelPayload payload) {
        ClientPayloadListener.handleTravel(player, payload);
    }

    @Override
    public Type<ClientboundTravelPayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(Aquamirae.id("travel"));
        STREAM_CODEC = StreamCodec.composite(
                ResourceKey.streamCodec(Registries.DIMENSION), ClientboundTravelPayload::from,
                ResourceKey.streamCodec(Registries.DIMENSION), ClientboundTravelPayload::to,
                ClientboundTravelPayload::new);
    }
}