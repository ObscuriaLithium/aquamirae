package dev.obscuria.aquamirae.common.region.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.common.region.noise.NoiseLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CrackedIceConfig(
        TagKey<Biome> biomeMask,
        NoiseLayer innerMask,
        NoiseLayer outerMask,
        NoiseLayer carverMask
) implements FeatureConfiguration {

    public static final Codec<CrackedIceConfig> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TagKey.codec(Registries.BIOME).fieldOf("biome_mask").forGetter(CrackedIceConfig::biomeMask),
                NoiseLayer.CODEC.fieldOf("inner_mask").forGetter(CrackedIceConfig::innerMask),
                NoiseLayer.CODEC.fieldOf("outer_mask").forGetter(CrackedIceConfig::outerMask),
                NoiseLayer.CODEC.fieldOf("carver_mask").forGetter(CrackedIceConfig::carverMask)
        ).apply(codec, CrackedIceConfig::new));
    }
}
