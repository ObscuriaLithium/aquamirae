package dev.obscuria.aquamirae.api.common.ability;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface AbilityPredicate {

    boolean test(Holder<AbilitySet> abilitySet, LivingEntity self);

    static AbilityPredicate alwaysTrue() {
        return (abilitySet, self) -> true;
    }
}
