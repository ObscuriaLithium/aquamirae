package dev.obscuria.aquamirae.neoforge.client;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.integration.hook.client.HookBossHealthOverlay;
import dev.obscuria.aquamirae.config.ConfigScreenBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Aquamirae.MOD_ID, dist = Dist.CLIENT)
public final class NeoAquamiraeClient {

    public NeoAquamiraeClient() {
        AquamiraeClient.init();
        registerConfigScreen();
        NeoForge.EVENT_BUS.register(NeoAquamiraeClient.class);
    }

    @SubscribeEvent
    public static void onCustomizeBossEventOverlay(final CustomizeGuiOverlayEvent.BossEventProgress event) {
        HookBossHealthOverlay.drawBar(event.getBossEvent(), event.getGuiGraphics(), event.getX(), event.getY());
    }

    private static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () ->
                (client, parent) -> ConfigScreenBuilder.create(parent));
    }
}