package dev.obscuria.aquamirae.integration.hook;

import dev.obscuria.aquamirae.api.common.TooltipImageProvider;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.fragmentum.content.world.tooltip.GroupTooltip;
import dev.obscuria.fragmentum.content.world.tooltip.TooltipOptions;
import dev.obscuria.fragmentum.content.world.tooltip.Tooltips;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class HookItemStack {

    private static final List<TooltipImageProvider> providersScratch = new ArrayList<>(8);

    public static void appendTooltipLines(ItemStack stack, List<Component> components) {
        @Nullable var tooltip = stack.get(AquamiraeDataComponents.TOOLTIP);
        if (tooltip == null) return;

        var lines = Tooltips.process(tooltip.content(), stack, TooltipOptions.DESCRIPTION);
        if (tooltip.emptyAfter()) components.add(tooltip.insertAt(), CommonComponents.EMPTY);
        components.addAll(tooltip.insertAt(), lines);
        if (tooltip.emptyBefore()) components.add(tooltip.insertAt(), CommonComponents.EMPTY);
    }

    public static Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        providersScratch.clear();

        for (var component : stack.getComponents()) {
            if (!(component.value() instanceof TooltipImageProvider provider)) continue;
            providersScratch.add(provider);
        }

        return switch (providersScratch.size()) {
            case 0 -> Optional.empty();
            case 1 -> Optional.of(providersScratch.getFirst().getTooltipImage(stack));
            default -> {
                Collections.sort(providersScratch);
                var components = new ArrayList<TooltipComponent>();
                for (TooltipImageProvider scratch : providersScratch)
                    components.add(scratch.getTooltipImage(stack));
                yield Optional.of(new GroupTooltip(components));
            }
        };
    }
}
