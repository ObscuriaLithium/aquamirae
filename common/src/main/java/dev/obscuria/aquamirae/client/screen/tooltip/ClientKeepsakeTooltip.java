package dev.obscuria.aquamirae.client.screen.tooltip;

import dev.obscuria.aquamirae.common.component.Keepsake;
import dev.obscuria.fragmentum.content.util.color.Colors;
import dev.obscuria.fragmentum.content.util.color.HSV;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public record ClientKeepsakeTooltip(Component component) implements ClientTooltipComponent {

    private static final long START_TIME = System.currentTimeMillis();
    private static final String LANG_KEY = "tooltip.aquamirae.keepsake";
    private static final HSV BASE_COLOR = Colors.rgbOf(16733525).toHSV();

    public static ClientKeepsakeTooltip create(Keepsake keepsake) {
        return new ClientKeepsakeTooltip(Component.translatable(LANG_KEY, keepsake.owner()));
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(component);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        var timer = (System.currentTimeMillis() - START_TIME) / 500.0f;
        var color = BASE_COLOR.shiftHue(0.15f * Mth.cos(timer)).toARGB(1f).decimal();
        graphics.drawString(font, component, x, y, color);
    }
}
