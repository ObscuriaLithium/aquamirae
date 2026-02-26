package com.obscuria.aquamirae.common.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.obscuria.aquamirae.common.worldgen.easing.Easing;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;

public record NoiseLayer(
        Holder<PackedNoise> noise,
        Easing easing,
        float min,
        float max
) {

    public static final Codec<NoiseLayer> CODEC;

    public boolean test(float x, float z) {
        final var noiseValue = noise.value().sample(x, z);
        return noiseValue >= min && noiseValue <= max;
    }

    public boolean test(float x, float y, float z) {
        final var noiseValue = noise.value().sample(x, y, z);
        return noiseValue >= min && noiseValue <= max;
    }

    public float sample(float x, float z) {
        return easing.compute(Mth.clamp((noise.value().sample(x, z) - min) / (max - min), 0f, 1f));
    }

    public float sample(float x, float y, float z) {
        return easing.compute(Mth.clamp((noise.value().sample(x, y, z) - min) / (max - min), 0f, 1f));
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                PackedNoise.CODEC.fieldOf("noise").forGetter(NoiseLayer::noise),
                Easing.CODEC.optionalFieldOf("easing", Easing.LINEAR).forGetter(NoiseLayer::easing),
                Codec.FLOAT.fieldOf("min").forGetter(NoiseLayer::min),
                Codec.FLOAT.fieldOf("max").forGetter(NoiseLayer::max)
        ).apply(codec, NoiseLayer::new));
    }
}
