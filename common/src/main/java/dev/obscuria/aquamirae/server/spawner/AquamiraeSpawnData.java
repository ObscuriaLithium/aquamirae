package dev.obscuria.aquamirae.server.spawner;

import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import dev.obscuria.fragmentum.config.ConfigValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public enum AquamiraeSpawnData {
    MAW(
            MobCategory.MONSTER,
            AquamiraeEntityTypes.MAW::get,
            CommonConfig.ENABLE_MAW_SPAWNING,
            CommonConfig.MAW_SPAWN_WEIGHT,
            CommonConfig.MAW_SPAWN_MIN_COUNT,
            CommonConfig.MAW_SPAWN_MAX_COUNT),
    ANGLERFISH(
            MobCategory.MONSTER,
            AquamiraeEntityTypes.ANGLERFISH::get,
            CommonConfig.ENABLE_ANGLERFISH_SPAWNING,
            CommonConfig.ANGLERFISH_SPAWN_WEIGHT,
            CommonConfig.ANGLERFISH_SPAWN_MIN_COUNT,
            CommonConfig.ANGLERFISH_SPAWN_MAX_COUNT),
    SCUTTLER(
            MobCategory.CREATURE,
            AquamiraeEntityTypes.SCUTTLER::get,
            CommonConfig.ENABLE_SCUTTLER_SPAWNING,
            CommonConfig.SCUTTLER_SPAWN_WEIGHT,
            CommonConfig.SCUTTLER_SPAWN_MIN_COUNT,
            CommonConfig.SCUTTLER_SPAWN_MAX_COUNT),
    SPINEFISH(
            MobCategory.WATER_AMBIENT,
            AquamiraeEntityTypes.SPINEFISH::get,
            CommonConfig.ENABLE_SPINEFISH_SPAWNING,
            CommonConfig.SPINEFISH_SPAWN_WEIGHT,
            CommonConfig.SPINEFISH_SPAWN_MIN_COUNT,
            CommonConfig.SPINEFISH_SPAWN_MAX_COUNT),
    ABYSSAL_SCYPHOID(
            MobCategory.WATER_AMBIENT,
            AquamiraeEntityTypes.ABYSSAL_SCYPHOID::get,
            CommonConfig.ENABLE_ABYSSAL_SCYPHOID_SPAWNING,
            CommonConfig.ABYSSAL_SCYPHOID_SPAWN_WEIGHT,
            CommonConfig.ABYSSAL_SCYPHOID_SPAWN_MIN_COUNT,
            CommonConfig.ABYSSAL_SCYPHOID_SPAWN_MAX_COUNT);

    private final MobCategory mobCategory;
    private final Supplier<EntityType<? extends Mob>> typeSupplier;
    private final ConfigValue<Boolean> enabled;
    private final ConfigValue<Integer> weight;
    private final ConfigValue<Integer> minCount;
    private final ConfigValue<Integer> maxCount;

    AquamiraeSpawnData(
            MobCategory mobCategory,
            Supplier<EntityType<? extends Mob>> typeSupplier,
            ConfigValue<Boolean> isEnabled,
            ConfigValue<Integer> weight,
            ConfigValue<Integer> minCount,
            ConfigValue<Integer> maxCount
    ) {
        this.mobCategory = mobCategory;
        this.typeSupplier = typeSupplier;
        this.enabled = isEnabled;
        this.weight = weight;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    public static List<MobSpawnSettings.SpawnerData> bake(MobCategory category) {
        var result = new ArrayList<MobSpawnSettings.SpawnerData>();
        for (var value : values()) {
            if (value.mobCategory != category) continue;
            if (!value.enabled.get()) continue;
            result.add(value.bake());
        }
        return result;
    }

    public MobSpawnSettings.SpawnerData bake() {
        return new MobSpawnSettings.SpawnerData(typeSupplier.get(), weight.get(), minCount.get(), maxCount.get());
    }
}
