package dev.obscuria.aquamirae.api.common;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public interface TooltipImageProvider extends Comparable<TooltipImageProvider> {

    TooltipComponent getTooltipImage(ItemStack stack);

    default int priority() {
        return 0;
    }

    @Override
    default int compareTo(TooltipImageProvider other) {
        return Integer.compare(other.priority(), priority());
    }
}
