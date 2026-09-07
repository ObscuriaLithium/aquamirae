package dev.obscuria.aquamirae.client;

import dev.obscuria.aquamirae.client.sound.BlizzardSoundInstance;
import dev.obscuria.aquamirae.config.ClientConfig;
import dev.obscuria.aquamirae.registry.tags.AquamiraeBiomeTags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class BlizzardContext {

    private static final float WIND_DRIFT_SPEED = 0.01f;
    private static final float GUST_AMPLITUDE = 0.0f;
    private static final float GUST_FREQUENCY = 0.05f;

    public static boolean active = false;
    public static float windX = 0f;
    public static float windZ = 0f;
    public static float speedFactor = 1f;

    private static double totalTime = 0.0;
    private static float windAngle = 0.78f;
    private static @Nullable BlizzardSoundInstance sound;

    private BlizzardContext() {}

    public static void update(double camX, double camY, double camZ, float deltaSec) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) {
            active = false;
            return;
        }

        var player = mc.player;
        if (player != null) {
            if (sound != null && sound.isStopped()) sound = null;
            if (sound == null && active) {
                sound = new BlizzardSoundInstance(player);
                Minecraft.getInstance().getSoundManager().play(sound);
            }
        }

        BlockPos pos = BlockPos.containing(camX, camY, camZ);
        var biome = level.getBiome(pos);
        active = biome.is(AquamiraeBiomeTags.HAS_BLIZZARD);

        if (!active) {
            speedFactor = 1f;
            windX = 0f;
            windZ = 0f;
            return;
        }

        totalTime += deltaSec;
        double t = totalTime;

        if (!Minecraft.getInstance().isPaused())
            windAngle += WIND_DRIFT_SPEED * deltaSec * (float) Math.sin(t * 0.3);

        float gustFactor = 1f + GUST_AMPLITUDE * (float) Math.sin(t * GUST_FREQUENCY * Math.PI * 2.0);
        float strength = ClientConfig.BLIZZARD_WIND_OFFSET.get().floatValue() * gustFactor;

        windX = (float) Math.cos(windAngle) * strength;
        windZ = (float) Math.sin(windAngle) * strength;
        speedFactor = ClientConfig.BLIZZARD_SPEED_MULTIPLIER.get().floatValue();
    }
}