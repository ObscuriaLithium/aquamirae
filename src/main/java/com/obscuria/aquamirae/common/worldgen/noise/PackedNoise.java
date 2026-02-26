package com.obscuria.aquamirae.common.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.obscuria.aquamirae.registry.AquamiraeRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.StringRepresentable;

public record PackedNoise(
        NoiseTemplate template,
        FastNoiseLite instance
) {

    public static final Codec<PackedNoise> DIRECT_CODEC;
    public static final Codec<Holder<PackedNoise>> CODEC;

    public PackedNoise(NoiseTemplate template) {
        this(template, template.instantiate());
    }

    public float sample(float x, float z) {
        return instance.GetNoise(x, z) * 0.5f + 0.5f;
    }

    public float sample(float x, float y, float z) {
        return instance.GetNoise(x, y, z) * 0.5f + 0.5f;
    }

    static {
        DIRECT_CODEC = NoiseTemplate.CODEC.xmap(PackedNoise::new, PackedNoise::template);
        CODEC = RegistryFileCodec.create(AquamiraeRegistries.Keys.NOISE, DIRECT_CODEC);
    }

    public record NoiseTemplate(
            float frequency,
            NoiseType noiseType,
            RotationType3D rotationType3D,
            FractalType fractalType,
            int fractalOctaves,
            float fractalLacunarity,
            float fractalGain,
            float fractalWeightedStrength,
            float fractalPingPongStrength,
            CellularDistanceFunction cellularDistanceFunction,
            CellularReturnType cellularReturnType,
            float cellularJitter,
            DomainWarpType domainWarpType,
            float domainWarpAmplitude) {

        public static final Codec<NoiseTemplate> CODEC;

        public FastNoiseLite instantiate() {
            var instance = new FastNoiseLite();
            instance.SetNoiseType(noiseType.type);
            instance.SetFrequency(frequency);
            instance.SetRotationType3D(rotationType3D.type);
            instance.SetFractalType(fractalType.type);
            instance.SetFractalOctaves(fractalOctaves);
            instance.SetFractalLacunarity(fractalLacunarity);
            instance.SetFractalGain(fractalGain);
            instance.SetFractalWeightedStrength(fractalWeightedStrength);
            instance.SetFractalPingPongStrength(fractalPingPongStrength);
            instance.SetCellularDistanceFunction(cellularDistanceFunction.type);
            instance.SetCellularReturnType(cellularReturnType.type);
            instance.SetCellularJitter(cellularJitter);
            instance.SetDomainWarpType(domainWarpType.type);
            instance.SetDomainWarpAmp(domainWarpAmplitude);
            return instance;
        }

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    Codec.FLOAT.optionalFieldOf("frequency", 0.01f).forGetter(NoiseTemplate::frequency),
                    StringRepresentable.fromEnum(NoiseType::values).optionalFieldOf("noise_type", NoiseType.OpenSimplex2).forGetter(NoiseTemplate::noiseType),
                    StringRepresentable.fromEnum(RotationType3D::values).optionalFieldOf("rotation_type_3d", RotationType3D.None).forGetter(NoiseTemplate::rotationType3D),
                    StringRepresentable.fromEnum(FractalType::values).optionalFieldOf("fractal_type", FractalType.None).forGetter(NoiseTemplate::fractalType),
                    Codec.INT.optionalFieldOf("fractal_octaves", 3).forGetter(NoiseTemplate::fractalOctaves),
                    Codec.FLOAT.optionalFieldOf("fractal_lacunarity", 2.0f).forGetter(NoiseTemplate::fractalLacunarity),
                    Codec.FLOAT.optionalFieldOf("fractal_gain", 0.5f).forGetter(NoiseTemplate::fractalGain),
                    Codec.FLOAT.optionalFieldOf("fractal_weighted_strength", 0.0f).forGetter(NoiseTemplate::fractalWeightedStrength),
                    Codec.FLOAT.optionalFieldOf("fractal_ping_pong_strength", 2.0f).forGetter(NoiseTemplate::fractalPingPongStrength),
                    StringRepresentable.fromEnum(CellularDistanceFunction::values).optionalFieldOf("cellular_distance_function", CellularDistanceFunction.EuclideanSq).forGetter(NoiseTemplate::cellularDistanceFunction),
                    StringRepresentable.fromEnum(CellularReturnType::values).optionalFieldOf("cellular_return_type", CellularReturnType.Distance).forGetter(NoiseTemplate::cellularReturnType),
                    Codec.FLOAT.optionalFieldOf("cellular_jitter", 1.0f).forGetter(NoiseTemplate::cellularJitter),
                    StringRepresentable.fromEnum(DomainWarpType::values).optionalFieldOf("domain_warp_type", DomainWarpType.OpenSimplex2).forGetter(NoiseTemplate::domainWarpType),
                    Codec.FLOAT.optionalFieldOf("domain_warp_amplitude", 1.0f).forGetter(NoiseTemplate::domainWarpAmplitude)
            ).apply(codec, NoiseTemplate::new));
        }
    }

    public enum NoiseType implements NoiseEnum {
        OpenSimplex2(FastNoiseLite.NoiseType.OpenSimplex2),
        OpenSimplex2S(FastNoiseLite.NoiseType.OpenSimplex2S),
        Cellular(FastNoiseLite.NoiseType.Cellular),
        Perlin(FastNoiseLite.NoiseType.Perlin),
        ValueCubic(FastNoiseLite.NoiseType.ValueCubic),
        Value(FastNoiseLite.NoiseType.Value);

        public final FastNoiseLite.NoiseType type;

        NoiseType(FastNoiseLite.NoiseType type) {
            this.type = type;
        }
    }

    public enum RotationType3D implements NoiseEnum {
        None(FastNoiseLite.RotationType3D.None),
        ImproveXYPlanes(FastNoiseLite.RotationType3D.ImproveXYPlanes),
        ImproveXZPlanes(FastNoiseLite.RotationType3D.ImproveXZPlanes);

        public final FastNoiseLite.RotationType3D type;

        RotationType3D(FastNoiseLite.RotationType3D type) {
            this.type = type;
        }
    }

    public enum FractalType implements NoiseEnum {
        None(FastNoiseLite.FractalType.None),
        FBm(FastNoiseLite.FractalType.FBm),
        Ridged(FastNoiseLite.FractalType.Ridged),
        PingPong(FastNoiseLite.FractalType.PingPong),
        DomainWarpProgressive(FastNoiseLite.FractalType.DomainWarpProgressive),
        DomainWarpIndependent(FastNoiseLite.FractalType.DomainWarpIndependent);

        public final FastNoiseLite.FractalType type;

        FractalType(FastNoiseLite.FractalType type) {
            this.type = type;
        }
    }

    public enum CellularDistanceFunction implements NoiseEnum {
        Euclidean(FastNoiseLite.CellularDistanceFunction.Euclidean),
        EuclideanSq(FastNoiseLite.CellularDistanceFunction.EuclideanSq),
        Manhattan(FastNoiseLite.CellularDistanceFunction.Manhattan),
        Hybrid(FastNoiseLite.CellularDistanceFunction.Hybrid);

        public final FastNoiseLite.CellularDistanceFunction type;

        CellularDistanceFunction(FastNoiseLite.CellularDistanceFunction type) {
            this.type = type;
        }
    }

    public enum CellularReturnType implements NoiseEnum {
        CellValue(FastNoiseLite.CellularReturnType.CellValue),
        Distance(FastNoiseLite.CellularReturnType.Distance),
        Distance2(FastNoiseLite.CellularReturnType.Distance2),
        Distance2Add(FastNoiseLite.CellularReturnType.Distance2Add),
        Distance2Sub(FastNoiseLite.CellularReturnType.Distance2Sub),
        Distance2Mul(FastNoiseLite.CellularReturnType.Distance2Mul),
        Distance2Div(FastNoiseLite.CellularReturnType.Distance2Div);

        public final FastNoiseLite.CellularReturnType type;

        CellularReturnType(FastNoiseLite.CellularReturnType type) {
            this.type = type;
        }
    }

    public enum DomainWarpType implements NoiseEnum {
        OpenSimplex2(FastNoiseLite.DomainWarpType.OpenSimplex2),
        OpenSimplex2Reduced(FastNoiseLite.DomainWarpType.OpenSimplex2Reduced),
        BasicGrid(FastNoiseLite.DomainWarpType.BasicGrid);

        public final FastNoiseLite.DomainWarpType type;

        DomainWarpType(FastNoiseLite.DomainWarpType type) {
            this.type = type;
        }
    }

    private interface NoiseEnum extends StringRepresentable {

        @Override
        default String getSerializedName() {
            return toString();
        }
    }
}
