package dev.obscuria.aquamirae.integration.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.integration.hook.server.HookMinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;"))
    private void runServer$onBuildServerStatus(CallbackInfo ci) {
        HookMinecraftServer.onRunServer((MinecraftServer) (Object) this);
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void stopServer$onHead(CallbackInfo ci) {
        HookMinecraftServer.onStopServer((MinecraftServer) (Object) this);
    }

    @ModifyArg(method = "createLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;Lnet/minecraft/server/level/progress/ChunkProgressListener;ZJLjava/util/List;ZLnet/minecraft/world/RandomSequences;)V", ordinal = 0), index = 9)
    private List<CustomSpawner> createLevels$modifyCustomOverworldSpawners(List<CustomSpawner> customSpawners) {
        var result = new ArrayList<>(customSpawners);
        HookMinecraftServer.addCustomSpawners(Level.OVERWORLD, result::add);
        return result;
    }

    @ModifyArg(method = "createLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;Lnet/minecraft/server/level/progress/ChunkProgressListener;ZJLjava/util/List;ZLnet/minecraft/world/RandomSequences;)V", ordinal = 1), index = 9)
    private List<CustomSpawner> createLevels$modifyCustomDimensionSpawners(
            List<CustomSpawner> customSpawners,
            @Local(ordinal = 1) ResourceKey<Level> dimension
    ) {
        var result = new ArrayList<>(customSpawners);
        HookMinecraftServer.addCustomSpawners(dimension, result::add);
        return result;
    }
}