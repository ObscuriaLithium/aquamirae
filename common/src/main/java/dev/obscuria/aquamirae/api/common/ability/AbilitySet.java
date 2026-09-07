package dev.obscuria.aquamirae.api.common.ability;

import dev.obscuria.aquamirae.api.common.ability.resolver.AbilitySetResolver;
import dev.obscuria.aquamirae.common.ability.AbilitySetImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Supplier;

public interface AbilitySet {

    static Builder builder() {
        return new AbilitySetImpl.BuilderImpl();
    }

    Component displayName();

    AbilitySetResolver resolver();

    AbilityStyle style();

    AbilityCounter counter();

    List<AbilitySetTier> tiers();

    interface Builder {

        Builder displayName(Component displayName);

        Builder resolver(AbilitySetResolver resolver);

        Builder style(AbilityStyle style);

        Builder counter(AbilityCounter counter);

        Builder addTier(int pieces, Ability ability);

        Builder addTier(int pieces, Ability ability, AbilityPredicate activationCondition);

        Builder addVariantTier(int pieces, Ability ability, EquipmentSlot slot, Supplier<? extends ItemLike> itemSupplier);

        AbilitySet build();
    }
}
