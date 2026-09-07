package dev.obscuria.aquamirae.api.common.ability;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public interface AbilitySetTier {

    int requiredPieces();

    Ability ability();

    boolean shouldActivate(Holder<AbilitySet> abilitySet, LivingEntity self);

    boolean shouldDisplay(Item item);
}
