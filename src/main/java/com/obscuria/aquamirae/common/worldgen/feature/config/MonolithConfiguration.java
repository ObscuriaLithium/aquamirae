package com.obscuria.aquamirae.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.obscuria.aquamirae.common.worldgen.noise.NoiseLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

public record MonolithConfiguration(
        NoiseLayer spreadMask,
        NoiseLayer carverMask,
        Block material,
        int offset,
        int height
) implements FeatureConfiguration {

    public static final Codec<MonolithConfiguration> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                NoiseLayer.CODEC.fieldOf("spread_mask").forGetter(MonolithConfiguration::spreadMask),
                NoiseLayer.CODEC.fieldOf("carver_mask").forGetter(MonolithConfiguration::carverMask),
                ForgeRegistries.BLOCKS.getCodec().fieldOf("material").forGetter(MonolithConfiguration::material),
                Codec.INT.fieldOf("offset").forGetter(MonolithConfiguration::offset),
                Codec.INT.fieldOf("height").forGetter(MonolithConfiguration::height)
        ).apply(codec, MonolithConfiguration::new));
    }
}
