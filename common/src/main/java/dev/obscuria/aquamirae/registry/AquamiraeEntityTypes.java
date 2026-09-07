package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.entity.*;
import dev.obscuria.aquamirae.common.entity.projectile.AbyssalShard;
import dev.obscuria.aquamirae.common.entity.projectile.MazeRose;
import dev.obscuria.aquamirae.common.entity.projectile.TerribleChakram;
import dev.obscuria.fragmentum.content.registry.DeferredEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public final class AquamiraeEntityTypes {

    public static final DeferredEntity<Scuttler> SCUTTLER = register("scuttler",
            () -> EntityType.Builder.of(Scuttler::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .clientTrackingRange(64));
    public static final DeferredEntity<Shellback> SHELLBACK = register("shellback",
            () -> EntityType.Builder.of(Shellback::new, MobCategory.CREATURE)
                    .sized(1.8f, 1.3f)
                    .clientTrackingRange(128));
    public static final DeferredEntity<GoldenMoth> GOLDEN_MOTH = register("golden_moth",
            () -> EntityType.Builder.of(GoldenMoth::new, MobCategory.AMBIENT)
                    .sized(0.4f, 0.3f)
                    .clientTrackingRange(64));
    public static final DeferredEntity<Maw> MAW = register("maw",
            () -> EntityType.Builder.of(Maw::new, MobCategory.MONSTER)
                    .sized(1.4f, 1.4f)
                    .clientTrackingRange(128));

    public static final DeferredEntity<Spinefish> SPINEFISH = register("spinefish",
            () -> EntityType.Builder.of(Spinefish::new, MobCategory.WATER_AMBIENT)
                    .sized(0.7f, 0.7f)
                    .clientTrackingRange(64));
    public static final DeferredEntity<AbyssalScyphoid> ABYSSAL_SCYPHOID = register("abyssal_scyphoid",
            () -> EntityType.Builder.of(AbyssalScyphoid::new, MobCategory.WATER_AMBIENT)
                    .sized(0.6f, 1.2f)
                    .clientTrackingRange(128));
    public static final DeferredEntity<Anglerfish> ANGLERFISH = register("anglerfish",
            () -> EntityType.Builder.of(Anglerfish::new, MobCategory.MONSTER)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(128));
    public static final DeferredEntity<MazeMother> MAZE_MOTHER = register("maze_mother",
            () -> EntityType.Builder.of(MazeMother::new, MobCategory.MONSTER)
                    .sized(8f, 3f)
                    .clientTrackingRange(256));
    public static final DeferredEntity<CaptainCorneliaBoss> CAPTAIN_CORNELIA = register("captain_cornelia",
            () -> EntityType.Builder.of(CaptainCorneliaBoss::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.3f)
                    .clientTrackingRange(64)
                    .fireImmune());
    public static final DeferredEntity<Eel> EEL = register("eel",
            () -> EntityType.Builder.of(Eel::new, MobCategory.MONSTER)
                    .sized(2.4f, 3.4f)
                    .clientTrackingRange(128));
    public static final DeferredEntity<TorturedSoul> TORTURED_SOUL = register("tortured_soul",
            () -> EntityType.Builder.of(TorturedSoul::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(64));

    public static final DeferredEntity<TerribleChakram> TERRIBLE_CHAKRAM = register("terrible_chakram",
            () -> EntityType.Builder.of(TerribleChakram::new, MobCategory.MISC)
                    .sized(1.2f, 0.6f)
                    .clientTrackingRange(64));
    public static final DeferredEntity<MazeRose> MAZE_ROSE = register("maze_rose",
            () -> EntityType.Builder.of(MazeRose::new, MobCategory.MISC)
                    .sized(1.2f, 0.6f)
                    .clientTrackingRange(64));
    public static final DeferredEntity<AbyssalShard> ABYSSAL_SHARD = register("abyssal_shard",
            () -> EntityType.Builder.of(AbyssalShard::new, MobCategory.MISC)
                    .sized(0.4f, 0.4f)
                    .clientTrackingRange(4));

    private static <T extends Entity> DeferredEntity<T> register(String name, Supplier<EntityType.Builder<T>> supplier) {
        return AquamiraeRegistries.REGISTRAR.registerEntity(Aquamirae.id(name), () -> supplier.get().build(name));
    }

    static void init() {
        var registrar = AquamiraeRegistries.REGISTRAR;

        registrar.registerAttributes(SCUTTLER, Mob::createMobAttributes);
        registrar.registerAttributes(SHELLBACK, Mob::createMobAttributes);
        registrar.registerAttributes(GOLDEN_MOTH, GoldenMoth::createAttributes);
        registrar.registerAttributes(MAW, Monster::createMonsterAttributes);
        registrar.registerAttributes(SPINEFISH, Mob::createMobAttributes);
        registrar.registerAttributes(ABYSSAL_SCYPHOID, Mob::createMobAttributes);
        registrar.registerAttributes(ANGLERFISH, Monster::createMonsterAttributes);
        registrar.registerAttributes(MAZE_MOTHER, Monster::createMonsterAttributes);
        registrar.registerAttributes(CAPTAIN_CORNELIA, Monster::createMonsterAttributes);
        registrar.registerAttributes(EEL, Monster::createMonsterAttributes);
        registrar.registerAttributes(TORTURED_SOUL, Monster::createMonsterAttributes);

        registrar.registerSpawnPlacement(
                SCUTTLER, SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Scuttler::checkSpawnRules);
        registrar.registerSpawnPlacement(
                MAW, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Maw::checkSpawnRules);
        registrar.registerSpawnPlacement(
                TORTURED_SOUL, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TorturedSoul::checkSpawnRules);
        registrar.registerSpawnPlacement(
                SPINEFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Spinefish::checkSpawnRules);
        registrar.registerSpawnPlacement(
                ABYSSAL_SCYPHOID, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbyssalScyphoid::checkSpawnRules);
        registrar.registerSpawnPlacement(
                ANGLERFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Anglerfish::checkSpawnRules);
    }
}
