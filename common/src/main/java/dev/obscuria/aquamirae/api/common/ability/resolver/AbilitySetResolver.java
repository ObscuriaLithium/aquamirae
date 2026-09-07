package dev.obscuria.aquamirae.api.common.ability.resolver;

import dev.obscuria.aquamirae.api.common.ability.AbilitySet;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface AbilitySetResolver {

    int countPieces(Holder<AbilitySet> abilitySet, LivingEntity self);

    default int countPiecesForTooltip(Holder<AbilitySet> abilitySet, LivingEntity self, ItemStack stack) {
        return countPieces(abilitySet, self);
    }
}