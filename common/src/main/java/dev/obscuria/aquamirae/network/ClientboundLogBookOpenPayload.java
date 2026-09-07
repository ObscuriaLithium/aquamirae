package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ClientboundLogBookOpenPayload(ItemStack stack) implements CustomPacketPayload {

    public static final Type<ClientboundLogBookOpenPayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLogBookOpenPayload> STREAM_CODEC;

    public static void handle(Player player, ClientboundLogBookOpenPayload payload) {
        ClientPayloadListener.handleLogBookOpen(player, payload);
    }

    @Override
    public Type<ClientboundLogBookOpenPayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(Aquamirae.id("log_book_open"));
        STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, ClientboundLogBookOpenPayload::stack,
                ClientboundLogBookOpenPayload::new);
    }
}