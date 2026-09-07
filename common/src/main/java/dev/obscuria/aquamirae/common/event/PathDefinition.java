package dev.obscuria.aquamirae.common.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.registry.AquamiraeRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public record PathDefinition(
        float hScale,
        float vScale,
        List<Vec3> points
) {

    public static final Codec<Holder<PathDefinition>> CODEC;
    public static final Codec<PathDefinition> DIRECT_CODEC;
    public static final ResourceKey<PathDefinition> DEFAULT;

    public List<Vec3> resolve(ServerLevel level, Vec3 origin) {
        var result = new ArrayList<Vec3>();

        var angle = level.random.nextDouble() * Math.PI * 2.0;
        var sin = Math.sin(angle);
        var cos = Math.cos(angle);

        for (var point : points) {
            var rotated = new Vec3(point.x * cos - point.z * sin, point.y, point.x * sin + point.z * cos);
            result.add(rotated.multiply(hScale, vScale, hScale).add(origin.x, level.getSeaLevel(), origin.z));
        }

        return result;
    }

    static {
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.FLOAT.fieldOf("h_scale").forGetter(PathDefinition::hScale),
                Codec.FLOAT.fieldOf("v_scale").forGetter(PathDefinition::vScale),
                Vec3.CODEC.listOf().fieldOf("points").forGetter(PathDefinition::points)
        ).apply(codec, PathDefinition::new));
        CODEC = RegistryFileCodec.create(AquamiraeRegistries.Key.SHIPBREAKER_PATH, DIRECT_CODEC);
        DEFAULT = ResourceKey.create(AquamiraeRegistries.Key.SHIPBREAKER_PATH, Aquamirae.id("default"));
    }
}
