package dev.obscuria.aquamirae.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeaFoodItem extends Item {

    public SeaFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            ItemStack stack, TooltipContext context,
            List<Component> components, TooltipFlag isAdvanced
    ) {
        @Nullable var foodProperties = stack.get(DataComponents.FOOD);
        if (foodProperties == null) return;
        if (foodProperties.effects().isEmpty()) return;
        if (components.size() > 1) components.add(CommonComponents.EMPTY);
        var foodEffects =  foodProperties.effects().stream().map(FoodProperties.PossibleEffect::effect).toList();
        PotionContents.addPotionTooltip(foodEffects, components::add, 1f, context.tickRate());
    }
}