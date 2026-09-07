package dev.obscuria.aquamirae.client;

import com.mojang.blaze3d.shaders.FogShape;
import dev.obscuria.aquamirae.config.ClientConfig;
import dev.obscuria.aquamirae.registry.tags.AquamiraeBiomeTags;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;

public final class FogEvents {

    private FogEvents() {}

    @Nullable public static FogColorCallback SET_COLOR = null;
    @Nullable public static FogDistanceCallback SET_DISTANCE =
            ((camera, fogType, fogMode, farPlane, partialTicks, start, end, shape) -> {
                var level = camera.getEntity().level();
                if (!level.isRaining()) return null;
                if (!level.getBiome(camera.getBlockPosition()).is(AquamiraeBiomeTags.HAS_BLIZZARD)) return null;
                return FogDistanceResult.of(
                        ClientConfig.BLIZZARD_FOG_START.get().floatValue(),
                        ClientConfig.BLIZZARD_FOG_END.get().floatValue(),
                        shape);
            });

    @FunctionalInterface
    public interface FogColorCallback {

        @Nullable
        FogColorResult override(
                Camera camera,
                @Nullable ClientLevel level,
                FogType fogType,
                float partialTicks,
                int renderDistanceChunks,
                float r, float g, float b
        );
    }

    @FunctionalInterface
    public interface FogDistanceCallback {

        @Nullable
        FogDistanceResult override(
                Camera camera,
                FogType fogType,
                FogMode fogMode,
                float farPlane,
                float partialTicks,
                float start, float end,
                FogShape shape
        );
    }

    public static final class FogColorResult {

        public float r, g, b;

        private FogColorResult(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public static FogColorResult of(float r, float g, float b) {
            return new FogColorResult(r, g, b);
        }

        public FogColorResult set(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }
    }

    public static final class FogDistanceResult {

        public float start, end;
        public FogShape shape;

        private FogDistanceResult(float start, float end, FogShape shape) {
            this.start = start;
            this.end = end;
            this.shape = shape;
        }

        public static FogDistanceResult of(float start, float end, FogShape shape) {
            return new FogDistanceResult(start, end, shape);
        }

        public FogDistanceResult set(float start, float end, FogShape shape) {
            this.start = start;
            this.end = end;
            this.shape = shape;
            return this;
        }
    }

    public enum FogMode {
        FOG_SKY,
        FOG_TERRAIN
    }
}