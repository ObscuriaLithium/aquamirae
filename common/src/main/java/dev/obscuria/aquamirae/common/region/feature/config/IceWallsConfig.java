package dev.obscuria.aquamirae.common.region.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.common.region.noise.NoiseLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record IceWallsConfig(
        TagKey<Biome> biomeMask,
        NoiseLayer groundMask,
        int wallLayer1Height,
        NoiseLayer wallLayer1,
        int wallLayer2Height,
        NoiseLayer wallLayer2
) implements FeatureConfiguration {

    public static final Codec<IceWallsConfig> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TagKey.codec(Registries.BIOME).fieldOf("biome_mask").forGetter(IceWallsConfig::biomeMask),
                NoiseLayer.CODEC.fieldOf("ground_mask").forGetter(IceWallsConfig::groundMask),
                Codec.INT.fieldOf("wall_layer_1_height").forGetter(IceWallsConfig::wallLayer1Height),
                NoiseLayer.CODEC.fieldOf("wall_layer_1").forGetter(IceWallsConfig::wallLayer1),
                Codec.INT.fieldOf("wall_layer_2_height").forGetter(IceWallsConfig::wallLayer2Height),
                NoiseLayer.CODEC.fieldOf("wall_layer_2").forGetter(IceWallsConfig::wallLayer2)
        ).apply(codec, IceWallsConfig::new));
    }
}
