package dev.obscuria.aquamirae.config;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.fragmentum.config.ConfigBuilder;
import dev.obscuria.fragmentum.config.ConfigValue;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public final class ClientConfig {

    public static final ConfigValue<Boolean> BLIZZARD_CUSTOM_FOG;
    public static final ConfigValue<Double> BLIZZARD_FOG_START;
    public static final ConfigValue<Double> BLIZZARD_FOG_END;
    public static final ConfigValue<Boolean> BLIZZARD_CUSTOM_PARTICLES;
    public static final ConfigValue<Double> BLIZZARD_WIND_OFFSET;
    public static final ConfigValue<Double> BLIZZARD_SPEED_MULTIPLIER;

    public static final Set<ConfigValue<?>> VALUES;

    public static void init() {}

    static {
        var builder = new ConfigBuilder("obscuria/aquamirae-client.toml");
        builder.comment(
                "=============[ Aquamirae Client Config ]============",
                " Please prefer editing the config through the in-game ",
                " configuration screen (available via the mods list),",
                " as it includes many hints and quality-of-life improvements.",
                "====================================================");

        BLIZZARD_CUSTOM_FOG = builder.defineBoolean("blizzard_custom_fog", true);
        BLIZZARD_FOG_START = builder.defineDouble("blizzard_fog_start", 0, 0, 256);
        BLIZZARD_FOG_END = builder.defineDouble("blizzard_fog_end", 128, 0, 256);
        BLIZZARD_CUSTOM_PARTICLES = builder.defineBoolean("blizzard_custom_particles", true);
        BLIZZARD_WIND_OFFSET = builder.defineDouble("blizzard_wind_offset", 10, 0, 100);
        BLIZZARD_SPEED_MULTIPLIER = builder.defineDouble("blizzard_speed_multiplier", 20, 0, 100);

        VALUES = builder.collectValues();
        builder.buildClient(Aquamirae.MOD_ID);
    }
}