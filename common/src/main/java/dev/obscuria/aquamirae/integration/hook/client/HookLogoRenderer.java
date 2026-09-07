package dev.obscuria.aquamirae.integration.hook.client;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class HookLogoRenderer {

    private static final ResourceLocation LOGO = Aquamirae.id("textures/gui/title/logo.png");

    public static void renderLogo(GuiGraphics graphics, int x, int y, float uOffset, float vOffset) {
        graphics.blit(LOGO, x + 10, y, uOffset, vOffset, 108, 24, 108, 24);
    }
}
