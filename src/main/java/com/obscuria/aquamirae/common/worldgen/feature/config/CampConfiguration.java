package com.obscuria.aquamirae.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CampConfiguration(
        int height,
        int barrelCount,
        ResourceLocation barrelLootTable
) implements FeatureConfiguration {

    public static final Codec<CampConfiguration> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("height").forGetter(CampConfiguration::height),
                Codec.INT.fieldOf("barrel_cound").forGetter(CampConfiguration::barrelCount),
                ResourceLocation.CODEC.fieldOf("barrel_loot_table").forGetter(CampConfiguration::barrelLootTable)
        ).apply(codec, CampConfiguration::new));
    }
}
