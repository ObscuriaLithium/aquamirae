package dev.obscuria.aquamirae.common.region.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.storage.loot.LootTable;

public record CampConfig(
        int height,
        int barrelCount,
        ResourceKey<LootTable> barrelLootTable
) implements FeatureConfiguration {

    public static final Codec<CampConfig> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("height").forGetter(CampConfig::height),
                Codec.INT.fieldOf("barrel_cound").forGetter(CampConfig::barrelCount),
                ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("barrel_loot_table").forGetter(CampConfig::barrelLootTable)
        ).apply(codec, CampConfig::new));
    }
}
