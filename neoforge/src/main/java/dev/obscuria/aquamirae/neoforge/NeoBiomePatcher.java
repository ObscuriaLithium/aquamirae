package dev.obscuria.aquamirae.neoforge;

import dev.obscuria.aquamirae.common.region.BiomePatcher;
import dev.obscuria.fragmentum.FragmentumProxy;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.List;

public record NeoBiomePatcher(
        ModifiableBiomeInfo.BiomeInfo.Builder builder,
        Registry<ConfiguredWorldCarver<?>> configuredCarvers,
        Registry<PlacedFeature> placedFeatures
) implements BiomePatcher {

    public static NeoBiomePatcher create(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        var registryAccess = FragmentumProxy.registryAccess();
        return new NeoBiomePatcher(builder,
                registryAccess.registryOrThrow(Registries.CONFIGURED_CARVER),
                registryAccess.registryOrThrow(Registries.PLACED_FEATURE));
    }

    @Override
    public void clearSpawns() {
        this.builder.getMobSpawnSettings().spawners.values().forEach(List::clear);
        this.builder.getMobSpawnSettings().mobSpawnCosts.clear();
    }

    @Override
    public void addCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> carverKey) {
        this.builder.getGenerationSettings().addCarver(carving, configuredCarvers.getHolderOrThrow(carverKey));
    }

    @Override
    public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey) {
        this.builder.getGenerationSettings().addFeature(step, placedFeatures.getHolderOrThrow(placedFeatureKey));
    }

    @Override
    public void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry) {
        this.builder.getMobSpawnSettings().addSpawn(spawnGroup, spawnEntry);
    }

    @Override
    public void setPrecipitation(boolean hasPrecipitation) {
        this.builder.getClimateSettings().setHasPrecipitation(hasPrecipitation);
    }

    @Override
    public void setTemperature(float temperature) {
        this.builder.getClimateSettings().setTemperature(temperature);
    }

    @Override
    public void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
        this.builder.getClimateSettings().setTemperatureModifier(temperatureModifier);
    }

    @Override
    public void setDownfall(float downfall) {
        this.builder.getClimateSettings().setDownfall(downfall);
    }

    @Override
    public void setAmbientParticle(AmbientParticleSettings settings) {
        this.builder.getSpecialEffects().ambientParticle(settings);
    }

    @Override
    public void setAmbientLoop(Holder<SoundEvent> sound) {
        this.builder.getSpecialEffects().ambientLoopSound(sound);
    }

    @Override
    public void setFogColor(int color) {
        this.builder.getSpecialEffects().fogColor(color);
    }

    @Override
    public void setSkyColor(int color) {
        this.builder.getSpecialEffects().skyColor(color);
    }

    @Override
    public void setWaterColor(int color) {
        this.builder.getSpecialEffects().waterColor(color);
    }

    @Override
    public void setWaterFogColor(int color) {
        this.builder.getSpecialEffects().waterFogColor(color);
    }
}
