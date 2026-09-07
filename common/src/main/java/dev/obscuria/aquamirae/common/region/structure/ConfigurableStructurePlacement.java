package dev.obscuria.aquamirae.common.region.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.config.reference.IntConfigReference;
import dev.obscuria.aquamirae.registry.AquamiraeStructurePlacements;
import lombok.Getter;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

public class ConfigurableStructurePlacement extends RandomSpreadStructurePlacement {

    public static final MapCodec<ConfigurableStructurePlacement> CODEC;

    @Getter private final Config config;

    @SuppressWarnings("all")
    public ConfigurableStructurePlacement(
            Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod,
            float frequency, int salt, Optional<ExclusionZone> exclusionZone, Config config
    ) {
        super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone,
                config.computeSpacing(), config.computeSeparation(), config.spreadType);
        this.config = config;
    }

    @Override
    public StructurePlacementType<ConfigurableStructurePlacement> type() {
        return AquamiraeStructurePlacements.CONFIGURABLE_RANDOM_SPREAD;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(builder ->
                placementCodec(builder).and(
                        Config.CODEC.fieldOf("config").forGetter(ConfigurableStructurePlacement::getConfig)
                ).apply(builder, ConfigurableStructurePlacement::new));
    }

    public record Config(
            IntConfigReference spacing,
            IntConfigReference separation,
            RandomSpreadType spreadType
    ) {

        public static final MapCodec<Config> CODEC;

        public int computeSpacing() {
            return Math.max(separation.get() + 1, spacing.get());
        }

        public int computeSeparation() {
            return separation.get();
        }

        static {
            CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    IntConfigReference.CODEC.fieldOf("spacing").forGetter(Config::spacing),
                    IntConfigReference.CODEC.fieldOf("separation").forGetter(Config::separation),
                    RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(Config::spreadType)
            ).apply(builder, Config::new));
        }
    }
}