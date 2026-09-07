package dev.obscuria.aquamirae.api.common.entity;

import java.util.List;

public interface MultipartEntity {

    List<? extends SubPartEntity<?>> getSubParts();
}