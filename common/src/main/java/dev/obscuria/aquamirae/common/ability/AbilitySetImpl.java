package dev.obscuria.aquamirae.common.ability;

import com.google.common.base.Predicates;
import dev.obscuria.aquamirae.api.common.ability.*;
import dev.obscuria.aquamirae.api.common.ability.resolver.AbilitySetResolver;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record AbilitySetImpl(
        Component displayName,
        AbilitySetResolver resolver,
        AbilityStyle style,
        AbilityCounter counter,
        List<AbilitySetTier> tiers
) implements AbilitySet {

    public static final class BuilderImpl implements Builder {

        private final List<AbilitySetTier> tiers = new ArrayList<>();
        private AbilityStyle style = AbilityStyles.BLUE_GEM;
        private AbilityCounter counter = AbilityCounters.PIECES;
        private @Nullable Component displayName;
        private @Nullable AbilitySetResolver resolver;

        public BuilderImpl() {}

        @Override
        public Builder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        @Override
        public Builder resolver(AbilitySetResolver resolver) {
            this.resolver = resolver;
            return this;
        }

        @Override
        public Builder style(AbilityStyle style) {
            this.style = style;
            return this;
        }

        @Override
        public Builder counter(AbilityCounter counter) {
            this.counter = counter;
            return this;
        }

        @Override
        public Builder addTier(int pieces, Ability ability) {
            this.tiers.add(new Tier(pieces, ability, AbilityPredicate.alwaysTrue(), Predicates.alwaysTrue()));
            return this;
        }

        @Override
        public Builder addTier(int pieces, Ability ability, AbilityPredicate activationCondition) {
            this.tiers.add(new Tier(pieces, ability, activationCondition, Predicates.alwaysTrue()));
            return this;
        }

        @Override
        public Builder addVariantTier(int pieces, Ability ability, EquipmentSlot slot, Supplier<? extends ItemLike> itemSupplier) {
            this.tiers.add(new Tier(pieces, ability,
                    (abilitySet, self) -> self.getItemBySlot(slot).is(itemSupplier.get().asItem()),
                    item -> item.equals(itemSupplier.get().asItem())));
            return this;
        }

        @Override
        public AbilitySet build() {
            return new AbilitySetImpl(
                    Objects.requireNonNull(displayName),
                    Objects.requireNonNull(resolver),
                    style, counter, List.copyOf(tiers));
        }
    }

    private record Tier(
            int requiredPieces,
            Ability ability,
            AbilityPredicate activationCondition,
            Predicate<Item> tooltipFilter
    ) implements AbilitySetTier {

        @Override
        public boolean shouldActivate(Holder<AbilitySet> abilitySet, LivingEntity self) {
            return activationCondition.test(abilitySet, self);
        }

        @Override
        public boolean shouldDisplay(Item item) {
            return tooltipFilter.test(item);
        }
    }
}
