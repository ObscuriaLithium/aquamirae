package dev.obscuria.aquamirae.api.common.ability;

import net.minecraft.core.Holder;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record AbilitySetTooltip(ItemStack stack, Holder<AbilitySet> abilitySet) implements TooltipComponent {}