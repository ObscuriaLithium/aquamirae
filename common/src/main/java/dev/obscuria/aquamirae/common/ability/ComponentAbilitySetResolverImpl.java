package dev.obscuria.aquamirae.common.ability;

import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.api.common.ability.AbilitySet;
import dev.obscuria.aquamirae.api.common.ability.resolver.ComponentAbilitySetResolver;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record ComponentAbilitySetResolverImpl(
        DataComponentType<Integer> componentType,
        EquipmentSlot slot
) implements ComponentAbilitySetResolver {

    @Override
    public int countPieces(Holder<AbilitySet> abilitySet, LivingEntity self) {
        var stack = self.getItemBySlot(slot);
        if (!hasSameAbility(stack, abilitySet)) return -1;
        return stack.getOrDefault(componentType, 0);
    }

    @Override
    public int countPiecesForTooltip(Holder<AbilitySet> abilitySet, LivingEntity self, ItemStack stack) {
        return stack.getOrDefault(componentType, 0);
    }

    private boolean hasSameAbility(ItemStack stack, Holder<AbilitySet> abilitySet) {
        return AbilityHelper.isSame(abilitySet, stack.get(AquamiraeDataComponents.ABILITY_SET));
    }
}
