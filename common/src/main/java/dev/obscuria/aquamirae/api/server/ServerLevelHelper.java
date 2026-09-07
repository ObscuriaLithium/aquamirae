package dev.obscuria.aquamirae.api.server;

import dev.obscuria.aquamirae.api.common.entity.SubPartEntity;
import dev.obscuria.aquamirae.integration.extension.server.ExtensionServerLevel;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ServerLevel;

public final class ServerLevelHelper {

    public static Int2ObjectMap<SubPartEntity<?>> getSubParts(ServerLevel level) {
        return ((ExtensionServerLevel) level).aquamirae$getSubParts();
    }

    public static void triggerEvent(ServerLevel level, TickableEvent event) {
        ((ExtensionServerLevel) level).aquamirae$triggerEvent(event);
    }
}
