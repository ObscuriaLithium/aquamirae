package dev.obscuria.aquamirae.neoforge.common;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.common.region.IceMazePatcher;
import dev.obscuria.aquamirae.neoforge.NeoBiomePatcher;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public record IceMazeBiomeModifier() implements BiomeModifier {

    public static final IceMazeBiomeModifier SHARED =  new IceMazeBiomeModifier();
    public static final MapCodec<IceMazeBiomeModifier> CODEC = MapCodec.unit(SHARED);

    @Override
    public void modify(Holder<Biome> holder, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.AFTER_EVERYTHING || !IceMazePatcher.isPatchTarget(holder)) return;
        IceMazePatcher.patch(NeoBiomePatcher.create(builder));
    }

    @Override
    public MapCodec<IceMazeBiomeModifier> codec() {
        return CODEC;
    }
}
