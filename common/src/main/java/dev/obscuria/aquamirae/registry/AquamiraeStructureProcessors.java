package dev.obscuria.aquamirae.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.region.structure.LogbookProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public final class AquamiraeStructureProcessors {

    public static final StructureProcessorType<LogbookProcessor> LOGBOOK = register("logbook", LogbookProcessor.CODEC);

    private static <T extends StructureProcessor> StructureProcessorType<T> register(String name, MapCodec<T> codec) {
        StructureProcessorType<T> type = () -> codec;
        AquamiraeRegistries.REGISTRAR.register(Registries.STRUCTURE_PROCESSOR, Aquamirae.id(name), () -> type);
        return type;
    }

    static void init() {}
}