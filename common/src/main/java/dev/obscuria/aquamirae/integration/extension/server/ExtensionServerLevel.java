package dev.obscuria.aquamirae.integration.extension.server;

import dev.obscuria.aquamirae.api.server.TickableEvent;
import dev.obscuria.aquamirae.api.common.entity.SubPartEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public interface ExtensionServerLevel {

    Int2ObjectMap<SubPartEntity<?>> aquamirae$getSubParts();

    void aquamirae$triggerEvent(TickableEvent event);
}