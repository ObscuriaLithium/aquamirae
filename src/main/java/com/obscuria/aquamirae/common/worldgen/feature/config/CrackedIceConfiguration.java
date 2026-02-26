package com.obscuria.aquamirae.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.obscuria.aquamirae.common.worldgen.noise.NoiseLayer;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CrackedIceConfiguration(
        NoiseLayer innerMask,
        NoiseLayer outerMask,
        NoiseLayer carverMask
) implements FeatureConfiguration {

    public static final Codec<CrackedIceConfiguration> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                NoiseLayer.CODEC.fieldOf("inner_mask").forGetter(CrackedIceConfiguration::innerMask),
                NoiseLayer.CODEC.fieldOf("outer_mask").forGetter(CrackedIceConfiguration::outerMask),
                NoiseLayer.CODEC.fieldOf("carver_mask").forGetter(CrackedIceConfiguration::carverMask)
        ).apply(codec, CrackedIceConfiguration::new));
    }
}
