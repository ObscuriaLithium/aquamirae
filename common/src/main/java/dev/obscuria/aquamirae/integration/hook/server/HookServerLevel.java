package dev.obscuria.aquamirae.integration.hook.server;

import dev.obscuria.aquamirae.api.common.entity.MultipartEntity;
import dev.obscuria.aquamirae.api.common.entity.SubPartEntity;
import dev.obscuria.aquamirae.api.server.ServerLevelHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public final class HookServerLevel {

    public static SubPartEntity<?> supplyEntityPart(ServerLevel level, int id) {
        return ServerLevelHelper.getSubParts(level).get(id);
    }

    public static void OnTrackingStart(ServerLevel level, Entity entity) {
        if (entity instanceof MultipartEntity multipartEntity) {
            for (var subPart : multipartEntity.getSubParts()) {
                putSubPart(level, subPart.getId(), subPart);
            }
        }
    }

    public static void OnTrackingEnd(ServerLevel level, Entity entity) {
        if (entity instanceof MultipartEntity multipartEntity) {
            for (var subPart : multipartEntity.getSubParts()) {
                removeSubPart(level, subPart.getId());
            }
        }
    }

    public static void putSubPart(ServerLevel level, int id, SubPartEntity<?> entity) {
        ServerLevelHelper.getSubParts(level).put(id, entity);
    }

    public static void removeSubPart(ServerLevel level, int id) {
        ServerLevelHelper.getSubParts(level).remove(id);
    }
}
