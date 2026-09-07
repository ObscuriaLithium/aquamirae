package dev.obscuria.aquamirae.integration.mixin;

import dev.obscuria.aquamirae.integration.hook.HookJukebox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxSongPlayer.class)
public abstract class MixinJukeboxSongPlayer {

    @Shadow @Final private BlockPos blockPos;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/JukeboxSongPlayer;stop(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/level/block/state/BlockState;)V"))
    private void tick$onStop(LevelAccessor level, BlockState state, CallbackInfo ci) {
        HookJukebox.recordFinishedNaturally(level, blockPos, state);
    }
}
