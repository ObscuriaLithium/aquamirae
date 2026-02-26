package com.obscuria.aquamirae.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.obscuria.aquamirae.common.worldgen.noise.NoiseLayer;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record WallsConfiguration(
        NoiseLayer groundMask,
        int wallLayer1Height,
        NoiseLayer wallLayer1,
        int wallLayer2Height,
        NoiseLayer wallLayer2
) implements FeatureConfiguration {

    public static final Codec<WallsConfiguration> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                NoiseLayer.CODEC.fieldOf("ground_mask").forGetter(WallsConfiguration::groundMask),
                Codec.INT.fieldOf("wall_layer_1_height").forGetter(WallsConfiguration::wallLayer1Height),
                NoiseLayer.CODEC.fieldOf("wall_layer_1").forGetter(WallsConfiguration::wallLayer1),
                Codec.INT.fieldOf("wall_layer_2_height").forGetter(WallsConfiguration::wallLayer2Height),
                NoiseLayer.CODEC.fieldOf("wall_layer_2").forGetter(WallsConfiguration::wallLayer2)
        ).apply(codec, WallsConfiguration::new));
    }
}
