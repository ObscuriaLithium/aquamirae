
package com.obscuria.aquamirae.registry;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.common.worldgen.structure.LogbookProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AquamiraeStructureProcessors {

	public static final DeferredRegister<StructureProcessorType<?>> REGISTRY = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Aquamirae.MODID);

	public static final RegistryObject<StructureProcessorType<LogbookProcessor>> LOGBOOK = REGISTRY.register("logbook", () -> () -> LogbookProcessor.CODEC);
}
