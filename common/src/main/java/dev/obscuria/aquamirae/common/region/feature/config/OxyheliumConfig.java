package dev.obscuria.aquamirae.common.region.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record OxyheliumConfig(
        IntProvider maxDepth,
        IntProvider maxLength,
        IntProvider count,
        IntProvider spreadRadius,
        FloatProvider lengthPenaltyPerBlock,
        TagKey<Block> growableOn
) implements FeatureConfiguration {

    public static final Codec<OxyheliumConfig> CODEC;

    public boolean canGrowOn(BlockState state) {
        return state.is(growableOn) || state.is(AquamiraeBlocks.OXYHELIUM.get());
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                IntProvider.POSITIVE_CODEC.fieldOf("max_depth").forGetter(OxyheliumConfig::maxDepth),
                IntProvider.POSITIVE_CODEC.fieldOf("max_length").forGetter(OxyheliumConfig::maxLength),
                IntProvider.POSITIVE_CODEC.fieldOf("count").forGetter(OxyheliumConfig::count),
                IntProvider.POSITIVE_CODEC.fieldOf("spread_radius").forGetter(OxyheliumConfig::spreadRadius),
                FloatProvider.CODEC.fieldOf("length_penalty_per_block").forGetter(OxyheliumConfig::lengthPenaltyPerBlock),
                TagKey.codec(Registries.BLOCK).fieldOf("growable_on").forGetter(OxyheliumConfig::growableOn)
        ).apply(codec, OxyheliumConfig::new));
    }
}