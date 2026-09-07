package dev.obscuria.aquamirae.integration.hook.client;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;

public final class HookBossHealthOverlay {

    private static final ResourceLocation TEXTURE = Aquamirae.id("textures/gui/overlay/captain_cornelia_bossbar.png");
    private static final String CORNELIA_BOSS_BAR_KEY = "entity.aquamirae.captain_cornelia";

    public static void drawBar(LerpingBossEvent event, GuiGraphics graphics, int x, int y) {
        if (!(event.getName().getContents() instanceof TranslatableContents contents)) return;
        if (!contents.getKey().equals(CORNELIA_BOSS_BAR_KEY)) return;
        graphics.blit(TEXTURE, x - 37, y - 20, 0, 0, 256, 64, 256, 64);
    }
}
