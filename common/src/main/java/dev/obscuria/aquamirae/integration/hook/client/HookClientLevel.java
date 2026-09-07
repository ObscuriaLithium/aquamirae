package dev.obscuria.aquamirae.integration.hook.client;

import dev.obscuria.aquamirae.client.event.ShipbreakerClientState;
import net.minecraft.client.multiplayer.ClientLevel;

public final class HookClientLevel {

    public static void onTick(ClientLevel level) {
        ShipbreakerClientState.tick(level);
    }
}
