package dev.obscuria.aquamirae.fabric;

import dev.obscuria.aquamirae.common.region.BiomePatcher;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record FabricBiomePatcher(BiomeModificationContext context) implements BiomePatcher {

        @Override
        public void clearSpawns() {
            this.context.getSpawnSettings().clearSpawns();
        }

        @Override
        public void addCarver(GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> carverKey) {
            this.context.getGenerationSettings().addCarver(step, carverKey);
        }

        @Override
        public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey) {
            this.context.getGenerationSettings().addFeature(step, placedFeatureKey);
        }

        @Override
        public void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry) {
            this.context.getSpawnSettings().addSpawn(spawnGroup, spawnEntry);
        }

        @Override
        public void setPrecipitation(boolean hasPrecipitation) {
            this.context.getWeather().setPrecipitation(hasPrecipitation);
        }

        @Override
        public void setTemperature(float temperature) {
            this.context.getWeather().setTemperature(temperature);
        }

        @Override
        public void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
            this.context.getWeather().setTemperatureModifier(temperatureModifier);
        }

        @Override
        public void setDownfall(float downfall) {
            this.context.getWeather().setDownfall(downfall);
        }

        @Override
        public void setAmbientParticle(AmbientParticleSettings settings) {
            this.context.getEffects().setParticleConfig(settings);
        }

        @Override
        public void setAmbientLoop(Holder<SoundEvent> sound) {
            this.context.getEffects().setAmbientSound(sound);
        }

        @Override
        public void setFogColor(int color) {
            this.context.getEffects().setFogColor(color);
        }

        @Override
        public void setSkyColor(int color) {
            this.context.getEffects().setSkyColor(color);
        }

        @Override
        public void setWaterColor(int color) {
            this.context.getEffects().setWaterColor(color);
        }

        @Override
        public void setWaterFogColor(int color) {
            this.context.getEffects().setWaterFogColor(color);
        }
    }