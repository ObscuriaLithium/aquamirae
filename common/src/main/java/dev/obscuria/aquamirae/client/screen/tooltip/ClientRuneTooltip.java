package dev.obscuria.aquamirae.client.screen.tooltip;

import com.google.common.base.Suppliers;
import dev.obscuria.aquamirae.common.component.RuneDamage;
import dev.obscuria.aquamirae.common.component.RuneProtection;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.fragmentum.content.world.tooltip.TooltipOptions;
import dev.obscuria.fragmentum.content.world.tooltip.Tooltips;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public record ClientRuneTooltip(
        ItemStack icon,
        List<Component> description
) implements ClientTooltipComponent {

    private static final Supplier<ItemStack> SHARED_ICON;
    private static final Component DAMAGE_DESCRIPTION;
    private static final Component PROTECTION_DESCRIPTION;
    private static final TooltipOptions OPTIONS;

    public static ClientRuneTooltip create(RuneDamage damage) {
        return create(DAMAGE_DESCRIPTION);
    }

    public static ClientRuneTooltip create(RuneProtection protection) {
        return create(PROTECTION_DESCRIPTION);
    }

    private static ClientRuneTooltip create(Component description) {
        return new ClientRuneTooltip(SHARED_ICON.get(), Tooltips.process(description, null, OPTIONS));
    }

    @Override
    public int getWidth(Font font) {
        var maxWidth = 0;
        for (var component : description()) {
            maxWidth = Math.max(maxWidth, font.width(component));
        }
        return 20 + maxWidth;
    }

    @Override
    public int getHeight() {
        return Math.max(22, description().size() * 10);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        graphics.renderItem(icon(), x + 2, y + 2);
        var offset = 0;
        for (var component : description()) {
            graphics.drawString(font, component, x + 20, y + offset, -1);
            offset += 10;
        }
    }

    static {
        SHARED_ICON = Suppliers.memoize(AquamiraeItems.RUNE_OF_THE_STORM::instantiate);
        DAMAGE_DESCRIPTION = Component.translatable("tooltip.aquamirae.rune_damage");
        PROTECTION_DESCRIPTION = Component.translatable("tooltip.aquamirae.rune_protection");
        OPTIONS = TooltipOptions.builder()
                .withDefaultStyle(TooltipOptions.DESCRIPTION.defaultStyle())
                .withDefaultPrefix(TooltipOptions.DESCRIPTION.defaultPrefix())
                .withProcessor(TooltipOptions.DESCRIPTION.processor())
                .withMaxLineLength(32)
                .build();
    }
}