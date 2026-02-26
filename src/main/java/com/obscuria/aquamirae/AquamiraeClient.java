package com.obscuria.aquamirae;

import com.obscuria.aquamirae.client.AquamiraeBossBars;
import com.obscuria.aquamirae.client.screen.ConfigScreen;
import com.obscuria.aquamirae.registry.AquamiraeSounds;
import com.obscuria.obscureapi.event.ObscureAPIRegisterBossBarsEvent;
import com.obscuria.obscureapi.util.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Supplier;

public final class AquamiraeClient {

    private static int biomeMusic = 0;
    private static int corneliaMusic = 0;

    public static Supplier<ConfigScreenHandler.ConfigScreenFactory> getConfig() {
        return () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen));
    }

    public static void scrollSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound(AquamiraeSounds.EFFECT_MYSTERY.get());
    }

    public static void spawnParticles(Player player) {
        if (!AquamiraeConfig.Client.particles.get()) return;
        player.level().addParticle(ParticleTypes.WHITE_ASH, false,
                player.getX() - 6D + 12D * player.getRandom().nextDouble(), player.getY() + 4 - 3D * player.getRandom().nextDouble(),
                player.getZ() - 6D + 12D * player.getRandom().nextDouble(), 0, 100, 0);
    }

    public static void playAmbientSounds(Player player, boolean config) {
        if (config) {
            if (AquamiraeConfig.Client.ambientSounds.get() && Math.random() <= 0.01 && Math.random() <= 0.1)
                player.level().playLocalSound(player.getBlockX() - 10 + 20 * Math.random(), player.getBlockY(),
                        player.getBlockZ() - 10 + 20 * Math.random(),
                        AquamiraeSounds.AMBIENT_SHIP_HORN.get(), SoundSource.AMBIENT, 1, 1, false);
        } else player.level().playLocalSound(player.getBlockX() - 10 + 20 * Math.random(), player.getBlockY(),
                player.getBlockZ() - 10 + 20 * Math.random(),
                AquamiraeSounds.AMBIENT_SHIP_HORN.get(), SoundSource.AMBIENT, 1, 1, false);
    }

    public static void playCorneliaMusic(Player player) {
        if (AquamiraeConfig.Client.bossMusic.get() && player.isAlive() && corneliaMusic <= 0) {
            corneliaMusic = 20 * 150;
            Minecraft.getInstance().getMusicManager().stopPlaying();
            Minecraft.getInstance().getMusicManager().startPlaying(
                    new Music(Holder.direct(AquamiraeSounds.MUSIC_FORSAKEN_DROWNAGE.get()), 10, 100, true));
        }
    }

    private static void playBiomeMusic(Player player) {
        if (AquamiraeConfig.Client.biomeMusic.get() && player.isAlive() && biomeMusic <= 0 && corneliaMusic <= 0) {
            biomeMusic = 20 * new Random().nextInt(180, 300);
            Minecraft.getInstance().getMusicManager().stopPlaying();
            Minecraft.getInstance().getMusicManager().startPlaying(
                    new Music(Holder.direct(AquamiraeSounds.MUSIC_ICE_MAZE_THEME.get()), 10, 100, true));
        }
    }

    @Mod.EventBusSubscriber(value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void registerBossBars(ObscureAPIRegisterBossBarsEvent event) {
            if (AquamiraeConfig.Client.stylizedBossbar.get())
                event.register("entity.aquamirae.captain_cornelia", false, true, true, AquamiraeBossBars::cornelia);
        }
    }

    @Mod.EventBusSubscriber(value = {Dist.CLIENT})
    public static class ForgeEvents {

        @SubscribeEvent
        public static void tick(TickEvent.@NotNull ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END || Minecraft.getInstance().player == null || Minecraft.getInstance().isPaused())
                return;
            if (biomeMusic > 0) biomeMusic--;
            if (corneliaMusic > 0) corneliaMusic--;
            final Player player = Minecraft.getInstance().player;
            if (AquamiraeUtils.isInIceMaze(player)) {
                playAmbientSounds(player, true);
                playBiomeMusic(player);
                spawnParticles(player);
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void fixOldPerk(@NotNull ItemTooltipEvent event) {
            if (ItemUtils.hasPerk(event.getItemStack(), "rune_of_the_storm")) {
                ItemUtils.removePerk(event.getItemStack(), "rune_of_the_storm");
                ItemUtils.addPerk(event.getItemStack(), new ResourceLocation("aquamirae", "rune_of_the_storm"), 1);
            }
        }
    }
}
