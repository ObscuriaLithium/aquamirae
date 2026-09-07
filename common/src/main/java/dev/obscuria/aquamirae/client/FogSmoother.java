package dev.obscuria.aquamirae.client;

import com.mojang.blaze3d.shaders.FogShape;
import org.jetbrains.annotations.Nullable;

public final class FogSmoother {

    public static float COLOR_SPEED_IN = 1.0f;
    public static float COLOR_SPEED_OUT = 0.2f;
    public static float DISTANCE_SPEED_IN = 1.0f;
    public static float DISTANCE_SPEED_OUT = 0.2f;

    private final ColorResult colorResult = new ColorResult();
    private final DistResult distResult = new DistResult();

    private float colorBlend = 0f;
    private float customR, customG, customB;
    private boolean hadCustomColor = false;
    private float distBlend = 0f;
    private float customStart, customEnd;
    private FogShape customShape = FogShape.SPHERE;
    private boolean hadCustomDist = false;

    public static final FogSmoother INSTANCE = new FogSmoother();

    private FogSmoother() {}

    public ColorResult smoothColor(
            float vanillaR, float vanillaG, float vanillaB,
            @Nullable FogEvents.FogColorResult override,
            float dt
    ) {
        if (override != null) {
            customR = override.r;
            customG = override.g;
            customB = override.b;
            hadCustomColor = true;
        }

        float blendTarget = (override != null) ? 1f : 0f;
        float colorSpeed = (override != null) ? COLOR_SPEED_IN : COLOR_SPEED_OUT;
        colorBlend = expLerp(colorBlend, blendTarget, dt, colorSpeed);

        if (!hadCustomColor || colorBlend <= 0.0001f) {
            colorResult.r = vanillaR;
            colorResult.g = vanillaG;
            colorResult.b = vanillaB;
            return colorResult;
        }

        colorResult.r = lerp(colorBlend, vanillaR, customR);
        colorResult.g = lerp(colorBlend, vanillaG, customG);
        colorResult.b = lerp(colorBlend, vanillaB, customB);
        return colorResult;
    }

    public DistResult smoothDistance(
            float vanillaStart, float vanillaEnd, FogShape vanillaShape,
            @Nullable FogEvents.FogDistanceResult override,
            float dt
    ) {
        if (override != null) {
            customStart = override.start;
            customEnd = override.end;
            customShape = override.shape;
            hadCustomDist = true;
        }

        float blendTarget = (override != null) ? 1f : 0f;
        float distSpeed = (override != null) ? DISTANCE_SPEED_IN : DISTANCE_SPEED_OUT;
        distBlend = expLerp(distBlend, blendTarget, dt, distSpeed);

        if (!hadCustomDist || distBlend <= 0.0001f) {
            distResult.start = vanillaStart;
            distResult.end = vanillaEnd;
            distResult.shape = vanillaShape;
            return distResult;
        }

        distResult.start = lerp(distBlend, vanillaStart, customStart);
        distResult.end = lerp(distBlend, vanillaEnd, customEnd);
        distResult.shape = (distBlend >= 0.5f) ? customShape : vanillaShape;
        return distResult;
    }

    public void reset() {
        colorBlend = 0f;
        distBlend = 0f;
        hadCustomColor = false;
        hadCustomDist = false;
    }

    private static float expLerp(float current, float target, float dt, float speed) {
        return target + (current - target) * (float) Math.exp(-dt * speed);
    }

    private static float lerp(float t, float a, float b) {
        return a + (b - a) * t;
    }

    public static final class ColorResult {

        public float r, g, b;
    }

    public static final class DistResult {

        public float start, end;
        public FogShape shape;
    }
}