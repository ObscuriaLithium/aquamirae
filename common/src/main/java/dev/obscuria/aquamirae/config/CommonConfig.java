package dev.obscuria.aquamirae.config;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.item.ShellHornItem;
import dev.obscuria.aquamirae.server.spawner.BiomeSpawnerMode;
import dev.obscuria.fragmentum.config.ConfigBuilder;
import dev.obscuria.fragmentum.config.ConfigValue;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;

@UtilityClass
public final class CommonConfig {

    public static final ConfigValue<ShellHornItem.UsageMode> SHELL_HORN_USAGE_MODE;
    public static final ConfigValue<Integer> SHELL_HORN_SEARCH_RADIUS;
    public static final ConfigValue<Integer> SHELL_HORN_SPAWN_OFFSET;
    public static final ConfigValue<Integer> SHELL_HORN_SUMMON_DELAY;
    public static final ConfigValue<Integer> SHELL_HORN_COOLDOWN;

    public static final ConfigValue<Double> REMNANT_SABER_BASE_BONUS;
    public static final ConfigValue<Double> REMNANT_SABER_PER_KILL_BONUS;
    public static final ConfigValue<Double> REMNANT_SABER_MAX_BONUS;

    public static final ConfigValue<Double> TERRIBLE_BLADE_POISON_AMPLIFIER;
    public static final ConfigValue<Double> TERRIBLE_BLADE_POISON_DURATION;
    public static final ConfigValue<Double> TERRIBLE_BLADE_REGENERATION_AMPLIFIER;
    public static final ConfigValue<Double> TERRIBLE_BLADE_REGENERATION_DURATION;

    public static final ConfigValue<Double> TERRIBLE_CLEAVER_MIN_BONUS_DAMAGE;
    public static final ConfigValue<Double> TERRIBLE_CLEAVER_MAX_BONUS_DAMAGE;
    public static final ConfigValue<Double> TERRIBLE_CLEAVER_CRIT_CHANCE;
    public static final ConfigValue<Double> TERRIBLE_CLEAVER_MIN_CRIT_BONUS;
    public static final ConfigValue<Double> TERRIBLE_CLEAVER_MAX_CRIT_BONUS;

    public static final ConfigValue<Double> TERRIBLE_FANG_PER_HEART_BONUS;
    public static final ConfigValue<Double> TERRIBLE_FANG_MAX_BONUS;

    public static final ConfigValue<List<? extends String>> SCUTTLER_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> SHELLBACK_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> GOLDEN_MOTH_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> SPINEFISH_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> MAW_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> ANGLERFISH_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> EEL_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> TORTURED_SOUL_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> MAZE_MOTHER_ATTRIBUTES;
    public static final ConfigValue<List<? extends String>> CAPTAIN_CORNELIA_ATTRIBUTES;

    public static final ConfigValue<List<? extends String>> ICE_MAZE_BIOMES;
    public static final ConfigValue<Integer> PIRATE_SHIP_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> PIRATE_SHIP_PLACEMENT_SEPARATION;
    public static final ConfigValue<Integer> PIRATE_OUTPOST_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> PIRATE_OUTPOST_PLACEMENT_SEPARATION;
    public static final ConfigValue<Integer> PIRATE_SHELTER_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> PIRATE_SHELTER_PLACEMENT_SEPARATION;

    public static final ConfigValue<Boolean> ENABLE_SHIPBREAKER_GRIEFING;
    public static final ConfigValue<Boolean> ENABLE_SHIPBREAKER_SPAWNING;
    public static final ConfigValue<Double> SHIPBREAKER_SPAWN_COOLDOWN;
    public static final ConfigValue<Double> SHIPBREAKER_SPAWN_COOLDOWN_VARIANCE;
    public static final ConfigValue<Double> SHIPBREAKER_SPAWN_RETRY_DELAY;

    public static final ConfigValue<BiomeSpawnerMode> BIOME_SPAWNER_MODE;
    public static final ConfigValue<Boolean> ENABLE_MAW_SPAWNING;
    public static final ConfigValue<Integer> MAW_SPAWN_WEIGHT;
    public static final ConfigValue<Integer> MAW_SPAWN_MIN_COUNT;
    public static final ConfigValue<Integer> MAW_SPAWN_MAX_COUNT;
    public static final ConfigValue<Boolean> ENABLE_ANGLERFISH_SPAWNING;
    public static final ConfigValue<Integer> ANGLERFISH_SPAWN_WEIGHT;
    public static final ConfigValue<Integer> ANGLERFISH_SPAWN_MIN_COUNT;
    public static final ConfigValue<Integer> ANGLERFISH_SPAWN_MAX_COUNT;
    public static final ConfigValue<Boolean> ENABLE_SCUTTLER_SPAWNING;
    public static final ConfigValue<Integer> SCUTTLER_SPAWN_WEIGHT;
    public static final ConfigValue<Integer> SCUTTLER_SPAWN_MIN_COUNT;
    public static final ConfigValue<Integer> SCUTTLER_SPAWN_MAX_COUNT;
    public static final ConfigValue<Boolean> ENABLE_SPINEFISH_SPAWNING;
    public static final ConfigValue<Integer> SPINEFISH_SPAWN_WEIGHT;
    public static final ConfigValue<Integer> SPINEFISH_SPAWN_MIN_COUNT;
    public static final ConfigValue<Integer> SPINEFISH_SPAWN_MAX_COUNT;
    public static final ConfigValue<Boolean> ENABLE_ABYSSAL_SCYPHOID_SPAWNING;
    public static final ConfigValue<Integer> ABYSSAL_SCYPHOID_SPAWN_WEIGHT;
    public static final ConfigValue<Integer> ABYSSAL_SCYPHOID_SPAWN_MIN_COUNT;
    public static final ConfigValue<Integer> ABYSSAL_SCYPHOID_SPAWN_MAX_COUNT;
    public static final ConfigValue<Boolean> ENABLE_MAZE_MOTHER_SPAWNING;
    public static final ConfigValue<Double> MAZE_MOTHER_SPAWN_CHANCE;

    public static final Set<ConfigValue<?>> VALUES;

    public static void init() {}

    static {
        var builder = new ConfigBuilder("obscuria/aquamirae-common.toml");
        builder.comment(
                "=============[ Aquamirae Common Config ]============",
                " Please prefer editing the config through the in-game ",
                " configuration screen (available via the mods list),",
                " as it includes many hints and quality-of-life improvements.",
                "====================================================");

        SHELL_HORN_USAGE_MODE = builder.DefineEnum("shell_horn_usage_mode", ShellHornItem.UsageMode.ICE_MAZE);
        SHELL_HORN_SEARCH_RADIUS = builder.defineInt("shell_horn_search_radius", 12, 6, 64);
        SHELL_HORN_SPAWN_OFFSET = builder.defineInt("shell_horn_spawn_offset", -4, -16, 0);
        SHELL_HORN_SUMMON_DELAY = builder.defineInt("shell_horn_summon_delay", 3, 0, 60);
        SHELL_HORN_COOLDOWN = builder.defineInt("shell_horn_cooldown", 5, 0, 60);

        REMNANT_SABER_BASE_BONUS = builder.defineDouble("remnant_saber_base_bonus", 0.1, 0, 1);
        REMNANT_SABER_PER_KILL_BONUS = builder.defineDouble("remnant_saber_per_kill_bonus", 0.001, 0, 1);
        REMNANT_SABER_MAX_BONUS = builder.defineDouble("remnant_saber_max_bonus", 2, 0, 10);

        TERRIBLE_BLADE_POISON_AMPLIFIER = builder.defineDouble("terrible_blade_poison_amplifier", 0, 0, 9);
        TERRIBLE_BLADE_POISON_DURATION = builder.defineDouble("terrible_blade_poison_duration", 6, 0, 60);
        TERRIBLE_BLADE_REGENERATION_AMPLIFIER = builder.defineDouble("terrible_blade_regeneration_amplifier", 1, 0, 9);
        TERRIBLE_BLADE_REGENERATION_DURATION = builder.defineDouble("terrible_blade_regeneration_duration", 6, 0, 60);

        TERRIBLE_CLEAVER_MIN_BONUS_DAMAGE = builder.defineDouble("terrible_cleaver_min_bonus_damage", 0.25, 0, 10);
        TERRIBLE_CLEAVER_MAX_BONUS_DAMAGE = builder.defineDouble("terrible_cleaver_max_bonus_damage", 0.5, 0, 10);
        TERRIBLE_CLEAVER_CRIT_CHANCE = builder.defineDouble("terrible_cleaver_crit_chance", 0.5, 0, 1);
        TERRIBLE_CLEAVER_MIN_CRIT_BONUS = builder.defineDouble("terrible_cleaver_min_crit_bonus", 0.5, 0, 10);
        TERRIBLE_CLEAVER_MAX_CRIT_BONUS = builder.defineDouble("terrible_cleaver_max_crit_bonus", 1.0, 0, 10);

        TERRIBLE_FANG_PER_HEART_BONUS = builder.defineDouble("terrible_fang_per_heart_bonus", 0.15, 0, 3);
        TERRIBLE_FANG_MAX_BONUS = builder.defineDouble("terrible_fang_max_bonus", 1.5, 0, 3);

        SCUTTLER_ATTRIBUTES = builder.defineList("scuttler_attributes", List.of(
                        "generic.scale set(0.5~1.5)",
                        "generic.max_health set(8~12)",
                        "generic.movement_speed set(0.3)",
                        "generic.armor set(2~4)",
                        "generic.step_height set(1)"),
                () -> "");
        SHELLBACK_ATTRIBUTES = builder.defineList("shellback_attributes", List.of(
                        "generic.max_health set(30~50)",
                        "generic.movement_speed set(0.25)",
                        "generic.armor set(10~20)",
                        "generic.step_height set(1)"),
                () -> "");
        GOLDEN_MOTH_ATTRIBUTES = builder.defineList("golden_moth_attributes", List.of(
                        "generic.max_health set(3~4)",
                        "generic.movement_speed set(0.4)",
                        "generic.flying_speed set(0.4)"),
                () -> "");
        SPINEFISH_ATTRIBUTES = builder.defineList("spinefish_attributes", List.of(
                        "generic.scale set(0.5~1.5)",
                        "generic.max_health set(8~12)"),
                () -> "");
        MAW_ATTRIBUTES = builder.defineList("maw_attributes", List.of(
                        "generic.scale set(0.85~1.15)",
                        "generic.max_health set(18~22)",
                        "generic.movement_speed set(0.18~0.22)",
                        "generic.attack_damage set(4)",
                        "generic.attack_knockback set(0.3)",
                        "generic.follow_range set(24)",
                        "generic.step_height set(1)"),
                () -> "");
        ANGLERFISH_ATTRIBUTES = builder.defineList("anglerfish_attributes", List.of(
                        "generic.max_health set(30~40)",
                        "generic.armor set(2~4)",
                        "generic.movement_speed set(0.35)",
                        "generic.attack_damage set(5)",
                        "generic.attack_knockback set(1)",
                        "generic.follow_range set(64)"),
                () -> "");
        EEL_ATTRIBUTES = builder.defineList("eel_attributes", List.of(
                        "generic.max_health set(160~200)",
                        "generic.armor set(20)",
                        "generic.attack_damage set(8)",
                        "generic.attack_knockback set(2)",
                        "generic.knockback_resistance set(100)",
                        "generic.movement_speed set(0)"),
                () -> "");
        TORTURED_SOUL_ATTRIBUTES = builder.defineList("tortured_soul_attributes", List.of(
                        "generic.max_health set(30)",
                        "generic.armor set(4)",
                        "generic.movement_speed set(0.2)",
                        "generic.attack_damage set(7)",
                        "generic.attack_knockback set(0.7)",
                        "generic.knockback_resistance set(0)",
                        "generic.follow_range set(24)"),
                () -> "");
        MAZE_MOTHER_ATTRIBUTES = builder.defineList("maze_mother_attributes", List.of(
                        "generic.max_health set(100)",
                        "generic.armor set(10)",
                        "generic.movement_speed set(64)",
                        "generic.attack_damage set(5)",
                        "generic.attack_knockback set(0.5)",
                        "generic.knockback_resistance set(0.2)",
                        "generic.follow_range set(128)"),
                () -> "");
        CAPTAIN_CORNELIA_ATTRIBUTES = builder.defineList("captain_cornelia_attributes", List.of(
                        "generic.max_health set(300)",
                        "generic.armor set(16)",
                        "generic.movement_speed set(0.2)",
                        "generic.attack_damage set(1)",
                        "generic.attack_knockback set(2)",
                        "generic.knockback_resistance set(0.75)",
                        "generic.follow_range set(128)",
                        "generic.step_height set(1)",
                        "aquamirae:depths_fury set(2)",
                        "aquamirae:depths_resilience set(2)"),
                () -> "");

        ICE_MAZE_BIOMES = builder.defineList("ice_maze_biomes", List.of(
                        "minecraft:deep_frozen_ocean",
                        "minecraft:deep_cold_ocean"),
                () -> "");
        PIRATE_SHIP_PLACEMENT_SPACING = builder.defineInt("pirate_ship_placement_spacing", 35, 1, 512);
        PIRATE_SHIP_PLACEMENT_SEPARATION = builder.defineInt("pirate_ship_placement_separation", 10, 1, 512);
        PIRATE_OUTPOST_PLACEMENT_SPACING = builder.defineInt("pirate_outpost_placement_spacing", 18, 1, 512);
        PIRATE_OUTPOST_PLACEMENT_SEPARATION = builder.defineInt("pirate_outpost_placement_separation", 10, 1, 512);
        PIRATE_SHELTER_PLACEMENT_SPACING = builder.defineInt("pirate_shelter_placement_spacing", 30, 1, 512);
        PIRATE_SHELTER_PLACEMENT_SEPARATION = builder.defineInt("pirate_shelter_placement_separation", 20, 1, 512);

        ENABLE_SHIPBREAKER_GRIEFING = builder.defineBoolean("shipbreaker_griefing", false);
        ENABLE_SHIPBREAKER_SPAWNING = builder.defineBoolean("enable_shipbreaker_spawning", true);
        SHIPBREAKER_SPAWN_COOLDOWN = builder.defineDouble("shipbreaker_spawn_cooldown", 30, 1, 300);
        SHIPBREAKER_SPAWN_COOLDOWN_VARIANCE = builder.defineDouble("shipbreaker_spawn_cooldown_variance", 10, 0, 300);
        SHIPBREAKER_SPAWN_RETRY_DELAY = builder.defineDouble("shipbreaker_spawn_retry_delay", 1, 1, 60);

        BIOME_SPAWNER_MODE = builder.DefineEnum("biome_spawner_mode", BiomeSpawnerMode.OVERRIDE);
        ENABLE_MAW_SPAWNING = builder.define("enable_maw_spawning", true);
        MAW_SPAWN_WEIGHT = builder.defineInt("maw_spawn_weight", 600, 1, 1000);
        MAW_SPAWN_MIN_COUNT = builder.defineInt("maw_spawn_min_count", 2, 1, 10);
        MAW_SPAWN_MAX_COUNT = builder.defineInt("maw_spawn_max_count", 4, 1, 10);
        ENABLE_ANGLERFISH_SPAWNING = builder.define("enable_anglerfish_spawning", true);
        ANGLERFISH_SPAWN_WEIGHT = builder.defineInt("anglerfish_spawn_weight", 10, 1, 1000);
        ANGLERFISH_SPAWN_MIN_COUNT = builder.defineInt("anglerfish_spawn_min_count", 1, 1, 10);
        ANGLERFISH_SPAWN_MAX_COUNT = builder.defineInt("anglerfish_spawn_max_count", 1, 1, 10);
        ENABLE_SCUTTLER_SPAWNING = builder.define("enable_scuttler_spawning", true);
        SCUTTLER_SPAWN_WEIGHT = builder.defineInt("scuttler_spawn_weight", 100, 1, 1000);
        SCUTTLER_SPAWN_MIN_COUNT = builder.defineInt("scuttler_spawn_min_count", 1, 1, 10);
        SCUTTLER_SPAWN_MAX_COUNT = builder.defineInt("scuttler_spawn_max_count", 2, 1, 10);
        ENABLE_SPINEFISH_SPAWNING = builder.define("enable_spinefish_spawning", true);
        SPINEFISH_SPAWN_WEIGHT = builder.defineInt("spinefish_spawn_weight", 100, 1, 1000);
        SPINEFISH_SPAWN_MIN_COUNT = builder.defineInt("spinefish_spawn_min_count", 4, 1, 10);
        SPINEFISH_SPAWN_MAX_COUNT = builder.defineInt("spinefish_spawn_max_count", 8, 1, 10);
        ENABLE_ABYSSAL_SCYPHOID_SPAWNING = builder.define("enable_abyssal_scyphoid_spawning", true);
        ABYSSAL_SCYPHOID_SPAWN_WEIGHT = builder.defineInt("abyssal_spawn_weight", 50, 1, 1000);
        ABYSSAL_SCYPHOID_SPAWN_MIN_COUNT = builder.defineInt("abyssal_spawn_min_count", 2, 1, 10);
        ABYSSAL_SCYPHOID_SPAWN_MAX_COUNT = builder.defineInt("abyssal_spawn_max_count", 4, 1, 10);
        ENABLE_MAZE_MOTHER_SPAWNING = builder.define("enable_maze_mother_spawning", true);
        MAZE_MOTHER_SPAWN_CHANCE = builder.defineDouble("maze_mother_spawn_chance", 0.005, 0, 1);

        VALUES = builder.collectValues();
        builder.buildCommon(Aquamirae.MOD_ID);
    }
}