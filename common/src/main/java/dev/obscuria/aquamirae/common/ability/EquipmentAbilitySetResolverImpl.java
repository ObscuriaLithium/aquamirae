package dev.obscuria.aquamirae.common.ability;

import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.api.common.ability.AbilitySet;
import dev.obscuria.aquamirae.api.common.ability.resolver.EquipmentAbilitySetResolver;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public record EquipmentAbilitySetResolverImpl(
        Set<EquipmentSlot> allowedSlots
) implements EquipmentAbilitySetResolver {

    @Override
    public int countPieces(Holder<AbilitySet> abilitySet, LivingEntity self) {
        var totalPieces = 0;
        for (var allowedSlot : allowedSlots) {
            totalPieces += hasSameAbility(self.getItemBySlot(allowedSlot), abilitySet) ? 1 : 0;
        }
        return totalPieces;
    }

    @Override
    public int countPiecesForTooltip(Holder<AbilitySet> abilitySet, LivingEntity self, ItemStack stack) {
        return Math.max(allowedSlots.size() == 1 ? 1 : 0, countPieces(abilitySet, self));
    }

    private boolean hasSameAbility(ItemStack stack, Holder<AbilitySet> abilitySet) {
        return AbilityHelper.isSame(abilitySet, stack.get(AquamiraeDataComponents.ABILITY_SET));
    }
}
