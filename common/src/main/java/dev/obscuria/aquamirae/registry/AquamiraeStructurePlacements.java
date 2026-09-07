package dev.obscuria.aquamirae.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.region.structure.ConfigurableStructurePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public final class AquamiraeStructurePlacements {

    public static final StructurePlacementType<ConfigurableStructurePlacement> CONFIGURABLE_RANDOM_SPREAD = register("configurable_random_spread", ConfigurableStructurePlacement.CODEC);

    private static <T extends StructurePlacement> StructurePlacementType<T> register(String name, MapCodec<T> codec) {
        StructurePlacementType<T> type = () -> codec;
        AquamiraeRegistries.REGISTRAR.register(Registries.STRUCTURE_PLACEMENT, Aquamirae.id(name), () -> type);
        return type;
    }

    static void init() {}
}