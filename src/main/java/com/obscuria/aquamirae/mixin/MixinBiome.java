package com.obscuria.aquamirae.mixin;

import com.obscuria.aquamirae.Aquamirae;
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
    private void preventFreezing(LevelReader level, BlockPos pos, boolean flag, CallbackInfoReturnable<Boolean> info) {
        if (!level.getBiome(pos).is(Aquamirae.ICE_MAZE)) return;
        info.setReturnValue(false);
    }
}
