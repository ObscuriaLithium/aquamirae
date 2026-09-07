package dev.obscuria.aquamirae.common.ability;

import dev.obscuria.aquamirae.api.common.ability.AbilitySet;
import dev.obscuria.aquamirae.api.common.ability.resolver.AbilitySetResolver;
import dev.obscuria.aquamirae.api.common.ability.resolver.CompositeAbilitySetResolver;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record CompositeAbilitySetResolverImpl(
        AbilitySetResolver firstResolver,
        AbilitySetResolver secondResolver
) implements CompositeAbilitySetResolver {

    @Override
    public int countPieces(Holder<AbilitySet> abilitySet, LivingEntity self) {
        var firstPieces = firstResolver.countPieces(abilitySet, self);
        if (firstPieces <= 0) return 0;
        return firstPieces + secondResolver.countPieces(abilitySet, self);
    }

    @Override
    public int countPiecesForTooltip(Holder<AbilitySet> abilitySet, LivingEntity self, ItemStack stack) {
        var firstPieces = firstResolver.countPiecesForTooltip(abilitySet, self, stack);
        if (firstPieces <= 0) return 0;
        return firstPieces + secondResolver.countPiecesForTooltip(abilitySet, self, stack);
    }
}
