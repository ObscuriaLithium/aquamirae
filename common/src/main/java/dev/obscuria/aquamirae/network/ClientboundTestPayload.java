package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public record ClientboundTestPayload(int action) implements CustomPacketPayload {

    public static final ClientboundTestPayload EARTHQUAKE = new ClientboundTestPayload(1);
    public static final ClientboundTestPayload MAZE_MOTHER_ENCOUNTER = new ClientboundTestPayload(2);

    public static final Type<ClientboundTestPayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTestPayload> STREAM_CODEC;

    public static void handle(Player player, ClientboundTestPayload payload) {
        ClientPayloadListener.handleTestAction(player, payload);
    }

    @Override
    public Type<ClientboundTestPayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(Aquamirae.id("test"));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, ClientboundTestPayload::action,
                ClientboundTestPayload::new);
    }
}
