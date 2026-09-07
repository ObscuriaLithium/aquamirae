package dev.obscuria.aquamirae.integration.mixin;

import dev.obscuria.aquamirae.registry.tags.AquamiraeBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class MixinBiome {

    @Inject(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At("HEAD"), cancellable = true)
    private void shouldFreeze$cancelFreezing(LevelReader level, BlockPos pos, boolean flag, CallbackInfoReturnable<Boolean> info) {
        if (!level.getBiome(pos).is(AquamiraeBiomeTags.WITHOUT_WATER_FREEZING)) return;
        info.setReturnValue(false);
    }
}
