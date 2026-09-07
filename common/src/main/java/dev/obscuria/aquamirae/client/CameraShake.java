package dev.obscuria.aquamirae.client;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@UtilityClass
public final class CameraShake {

    public static float SMOOTHING = 10.0f;

    private static final List<ShakeEntry> ACTIVE = new ArrayList<>();

    public static float offsetX = 0.0f;
    public static float offsetY = 0.0f;

    private static float currentStrength = 0.0f;

    public static void shake(float strength, float duration) {
        if (strength <= 0 || duration <= 0) return;
        ACTIVE.add(new ShakeEntry(strength, duration));
    }

    public static void clear() {
        ACTIVE.clear();
        currentStrength = 0.0f;
    }

    public static void tick(float deltaSeconds) {

        // --- advance timers & prune expired entries ---
        Iterator<ShakeEntry> it = ACTIVE.iterator();
        while (it.hasNext()) {
            ShakeEntry e = it.next();
            e.remaining -= deltaSeconds;
            if (e.remaining <= 0f) it.remove();
        }

        // --- find the strongest currently active shake ---
        float targetStrength = 0.0f;
        for (ShakeEntry e : ACTIVE) {
            // Fade strength linearly towards the end of the entry's lifetime
            float progress = e.remaining / e.totalDuration; // 1 → 0
            float faded = e.initialStrength * progress;
            if (faded > targetStrength) targetStrength = faded;
        }

        // --- exp-lerp towards the target (delta-independent) ---
        // Formula: current += (target - current) * (1 - exp(-k * dt))
        float factor = 1.0f - (float) Math.exp(-SMOOTHING * deltaSeconds);
        currentStrength += (targetStrength - currentStrength) * factor;

        // --- generate random offsets scaled by currentStrength ---
        if (currentStrength > 1e-4f) {
            offsetX = (float) (Math.random() * 2.0 - 1.0) * currentStrength;
            offsetY = (float) (Math.random() * 2.0 - 1.0) * currentStrength;
        } else {
            offsetX = 0.0f;
            offsetY = 0.0f;
        }
    }

    static final class ShakeEntry {

        final float initialStrength;
        final float totalDuration;
        float remaining;

        ShakeEntry(float strength, float duration) {
            this.initialStrength = strength;
            this.totalDuration = duration;
            this.remaining = duration;
        }
    }
}