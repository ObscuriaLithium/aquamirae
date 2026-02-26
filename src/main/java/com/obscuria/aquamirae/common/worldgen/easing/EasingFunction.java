package com.obscuria.aquamirae.common.worldgen.easing;

@SuppressWarnings("unused")
@FunctionalInterface
public interface EasingFunction {

    float compute(float delta);

    default EasingFunction reverse() {
        return progress -> 1f - this.compute(progress);
    }

    default EasingFunction scale(float scale) {
        return progress -> this.compute(progress * (1f / scale));
    }

    default EasingFunction merge(EasingFunction other, float ratio) {
        return progress -> {
            if (progress <= ratio) {
                final var local = progress / ratio;
                return this.compute(local) * ratio;
            } else {
                final var local = (progress - ratio) / (1f - ratio);
                return ratio + other.compute(local) * (1f - ratio);
            }
        };
    }

    default EasingFunction mergeOut(EasingFunction other, float ratio) {
        return progress -> {
            if (progress <= ratio) {
                final var local = progress / ratio;
                return this.compute(local);
            } else {
                final var local = (progress - ratio) / (1f - ratio);
                return 1f - other.compute(local);
            }
        };
    }
}
