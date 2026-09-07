package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class DreadwakeItem extends SwordItem {

    public DreadwakeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        var name = super.getName(stack);
        var awakeningTier = getAwakeningTier(stack);
        if (awakeningTier < 1) return name;
        var prefix = Component.translatable("item.aquamirae.dreadwake.tier_" + awakeningTier);
        return Component.translatable("item.aquamirae.dreadwake.withPrefix", prefix, name);
    }

    private int getAwakeningTier(ItemStack stack) {
        int changes = stack.getOrDefault(AquamiraeDataComponents.ABYSSAL_CHARGES, 0);
        if (changes >= 1000) return 3;
        if (changes >= 500) return 2;
        if (changes >= 250) return 1;
        return 0;
    }
}
