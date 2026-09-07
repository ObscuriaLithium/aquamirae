package dev.obscuria.aquamirae.fabric;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.region.IceMazePatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;

public final class FabricAquamirae implements ModInitializer {

    @Override
    public void onInitialize() {
        Aquamirae.init();
        BiomeModifications.create(Aquamirae.id("ice_maze")).add(
                ModificationPhase.POST_PROCESSING,
                context -> IceMazePatcher.isPatchTarget(context.getBiomeRegistryEntry()),
                context -> IceMazePatcher.patch(new FabricBiomePatcher(context)));
    }
}