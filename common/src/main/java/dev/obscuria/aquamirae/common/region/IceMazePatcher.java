package dev.obscuria.aquamirae.common.region;

import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.registry.references.AquamiraeConfiguredCarvers;
import dev.obscuria.aquamirae.registry.references.AquamiraePlacedFeatures;
import dev.obscuria.aquamirae.registry.tags.AquamiraeBiomeTags;
import dev.obscuria.aquamirae.server.spawner.AquamiraeSpawnData;
import dev.obscuria.aquamirae.server.spawner.BiomeSpawnerMode;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

public final class IceMazePatcher {

    public static boolean isPatchTarget(Holder<Biome> biome) {
        return biome.is(AquamiraeBiomeTags.ICE_MAZE);
    }

    public static void patch(BiomePatcher patcher) {

        patcher.addCarver(GenerationStep.Carving.AIR, AquamiraeConfiguredCarvers.CANYON);

        patcher.addFeature(GenerationStep.Decoration.RAW_GENERATION, AquamiraePlacedFeatures.SURFACE_ICE_WALLS);
        patcher.addFeature(GenerationStep.Decoration.RAW_GENERATION, AquamiraePlacedFeatures.SURFACE_CRACKED_ICE);
        patcher.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquamiraePlacedFeatures.SURFACE_CAMP);
        patcher.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquamiraePlacedFeatures.SURFACE_WISTERIA_NIVEIS);
        patcher.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquamiraePlacedFeatures.OXYHELIUM);

        var spawnerMode = CommonConfig.BIOME_SPAWNER_MODE.get();
        if (spawnerMode.equals(BiomeSpawnerMode.OVERRIDE)) patcher.clearSpawns();
        if (!spawnerMode.equals(BiomeSpawnerMode.NONE)) {
            for (var category : MobCategory.values()) {
                for (var spawnData : AquamiraeSpawnData.bake(category)) {
                    patcher.addSpawn(category, spawnData);
                }
            }
        }

        patcher.setTemperature(-1);
        patcher.setTemperatureModifier(Biome.TemperatureModifier.NONE);

        patcher.setAmbientParticle(new AmbientParticleSettings(ParticleTypes.WHITE_ASH, 0.05f));
        patcher.setAmbientLoop(AquamiraeSounds.AMBIENT_ICE_MAZE_LOOP.holder());
        patcher.setFogColor(12638463);
        patcher.setSkyColor(8364543);
        patcher.setWaterColor(3750089);
        patcher.setWaterFogColor(329011);
    }
}
