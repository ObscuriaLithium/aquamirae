package dev.obscuria.aquamirae.api.common.ability;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ItemLike;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class AbilityConditions {

    public static AbilityPredicate mainhand() {
        return (abilitySet, self) -> AbilityHelper.hasAbilitySet(self.getMainHandItem(), abilitySet);
    }

    public static Predicate<LivingEntity> wearing(EquipmentSlot slot, Supplier<? extends ItemLike> item) {
        return entity -> entity.getItemBySlot(slot).is(item.get().asItem());
    }
}