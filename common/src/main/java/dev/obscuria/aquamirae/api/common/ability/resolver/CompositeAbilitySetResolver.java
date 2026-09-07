package dev.obscuria.aquamirae.api.common.ability.resolver;

import com.google.common.base.Suppliers;
import dev.obscuria.aquamirae.api.common.ability.AbilitySet;
import dev.obscuria.aquamirae.common.ability.CompositeAbilitySetResolverImpl;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface CompositeAbilitySetResolver extends AbilitySetResolver {

    static CompositeAbilitySetResolver of(AbilitySetResolver first, AbilitySetResolver second) {
        return new CompositeAbilitySetResolverImpl(first, second);
    }

    static CompositeAbilitySetResolver related(AbilitySetResolver resolver, Supplier<Holder<AbilitySet>> relatedAbilitySet) {
        return of(resolver, relatedResolver(relatedAbilitySet));
    }

    private static AbilitySetResolver relatedResolver(Supplier<Holder<AbilitySet>> relatedAbilitySet) {

        return new CompositeAbilitySetResolver() {

            private final Supplier<Holder<AbilitySet>> memoizedAbilitySet = Suppliers.memoize(relatedAbilitySet::get);

            @Override
            public int countPieces(Holder<AbilitySet> ignored, LivingEntity self) {
                var abilitySet = memoizedAbilitySet.get();
                var relatedResolver = abilitySet.value().resolver();
                return relatedResolver.countPieces(abilitySet, self);
            }

            @Override
            public int countPiecesForTooltip(Holder<AbilitySet> ignored, LivingEntity self, ItemStack stack) {
                var abilitySet = memoizedAbilitySet.get();
                var relatedResolver = abilitySet.value().resolver();
                return relatedResolver.countPiecesForTooltip(abilitySet, self, stack);
            }
        };
    }
}
