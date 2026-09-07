package dev.obscuria.aquamirae.api.server;

import net.minecraft.server.level.ServerLevel;

public interface TickableEvent {

    void tick(ServerLevel level);

    boolean isFinished();

    default void onStart(ServerLevel level) {}

    default void onStop(ServerLevel level) {}
}
