package dev.obscuria.aquamirae.integration.hook;

import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.network.ClientboundTestPayload;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.server.event.ShipbreakerEvent;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class HookJukebox {

    public static void recordFinishedNaturally(LevelAccessor level, BlockPos pos, BlockState state) {

        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!(level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox)) return;
        if (!jukebox.getTheItem().is(AquamiraeItems.CRACKED_RECORD.asItem())) return;
        if (!AquamiraeHelper.isIceMazeAt(level, pos)) return;

        var recordItem = jukebox.removeItem(0, 1);
        ShipbreakerEvent.trigger(serverLevel, pos.getCenter());
        serverLevel.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1f, 1f);
        spawnItemParticles(serverLevel, pos, recordItem, 8);

        for (var player : serverLevel.getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(256))) {
            FragmentumNetworking.sendTo(player, ClientboundTestPayload.EARTHQUAKE);
        }
    }

    private static void spawnItemParticles(ServerLevel level, BlockPos pos, ItemStack stack, int amount) {
        for (var i = 0; i < amount; ++i) {
            var motion = new Vec3(level.random.triangle(0, 1f), 0.25f, level.random.triangle(0, 1f));
            var position = pos.getCenter().add(0, 0.55f, 0);
            level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack),
                    position.x, position.y, position.z, 1,
                    motion.x, motion.y, motion.z, 0);
        }
    }
}
