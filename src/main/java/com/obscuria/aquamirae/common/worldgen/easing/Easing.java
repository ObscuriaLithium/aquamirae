package com.obscuria.aquamirae.common.worldgen.easing;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

@SuppressWarnings("ALL")
public enum Easing implements EasingFunction, StringRepresentable
{
    LINEAR(EasingInternal.linear()),
    CEIL(EasingInternal.ceil()),
    FLOOR(EasingInternal.floor()),

    EASE_IN_SINE(EasingInternal.easeInSine()),
    EASE_IN_CIRCLE(EasingInternal.easeInCircle()),
    EASE_IN_QUAD(EasingInternal.easeInQuad()),
    EASE_IN_CUBIC(EasingInternal.easeInCubic()),
    EASE_IN_QUART(EasingInternal.easeInQuart()),
    EASE_IN_QUINT(EasingInternal.easeInQuint()),
    EASE_IN_EXPO(EasingInternal.easeInExpo()),
    EASE_IN_BACK(EasingInternal.easeInBack()),
    EASE_IN_ELASTIC(EasingInternal.easeInElastic()),
    EASE_IN_BOUNCE(EasingInternal.easeInBounce()),

    EASE_OUT_SINE(EasingInternal.easeOutSine()),
    EASE_OUT_CIRCLE(EasingInternal.easeOutCircle()),
    EASE_OUT_QUAD(EasingInternal.easeOutQuad()),
    EASE_OUT_CUBIC(EasingInternal.easeOutCubic()),
    EASE_OUT_QUART(EasingInternal.easeOutQuart()),
    EASE_OUT_QUINT(EasingInternal.easeOutQuint()),
    EASE_OUT_EXPO(EasingInternal.easeOutExpo()),
    EASE_OUT_BACK(EasingInternal.easeOutBack()),
    EASE_OUT_ELASTIC(EasingInternal.easeOutElastic()),
    EASE_OUT_BOUNCE(EasingInternal.easeOutBounce()),

    EASE_IN_OUT_SINE(EasingInternal.easeInOutSine()),
    EASE_IN_OUT_CIRCLE(EasingInternal.easeInOutCircle()),
    EASE_IN_OUT_QUAD(EasingInternal.easeInOutQuad()),
    EASE_IN_OUT_CUBIC(EasingInternal.easeInOutCubic()),
    EASE_IN_OUT_QUART(EasingInternal.easeInOutQuart()),
    EASE_IN_OUT_QUINT(EasingInternal.easeInOutQuint()),
    EASE_IN_OUT_EXPO(EasingInternal.easeInOutExpo()),
    EASE_IN_OUT_BACK(EasingInternal.easeInOutBack()),
    EASE_IN_OUT_ELASTIC(EasingInternal.easeInOutElastic()),
    EASE_IN_OUT_BOUNCE(EasingInternal.easeInOutBounce()),

    EASE_OUT_IN_SINE(EasingInternal.easeOutInSine()),
    EASE_OUT_IN_CIRCLE(EasingInternal.easeOutInCircle()),
    EASE_OUT_IN_QUAD(EasingInternal.easeOutInQuad()),
    EASE_OUT_IN_CUBIC(EasingInternal.easeOutInCubic()),
    EASE_OUT_IN_QUART(EasingInternal.easeOutInQuart()),
    EASE_OUT_IN_QUINT(EasingInternal.easeOutInQuint()),
    EASE_OUT_IN_EXPO(EasingInternal.easeOutInExpo()),
    EASE_OUT_IN_BACK(EasingInternal.easeOutInBack()),
    EASE_OUT_IN_ELASTIC(EasingInternal.easeOutInElastic()),
    EASE_OUT_IN_BOUNCE(EasingInternal.easeOutInBounce());

    public static final Codec<Easing> CODEC = StringRepresentable.fromEnum(Easing::values);
    private final EasingFunction function;

    Easing(EasingFunction function)
    {
        this.function = function;
    }

    @Override
    public float compute(float delta)
    {
        return function.compute(delta);
    }

    @Override
    public String getSerializedName()
    {
        return name().toLowerCase();
    }
}