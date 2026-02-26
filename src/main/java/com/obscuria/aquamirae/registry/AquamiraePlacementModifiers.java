
package com.obscuria.aquamirae.registry;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.common.worldgen.feature.placement.ChunkSampledBiomeFilter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AquamiraePlacementModifiers {

	public static final DeferredRegister<PlacementModifierType<?>> REGISTRY = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Aquamirae.MODID);

	public static final RegistryObject<PlacementModifierType<ChunkSampledBiomeFilter>> CHUNK_SAMPLED_BIOME = REGISTRY.register("chunk_sampled_biome", () -> () -> ChunkSampledBiomeFilter.CODEC);
}
