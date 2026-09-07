package dev.obscuria.aquamirae.api.common.ability.resolver;

import dev.obscuria.aquamirae.common.ability.EquipmentAbilitySetResolverImpl;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Set;

public interface EquipmentAbilitySetResolver extends AbilitySetResolver {

    static EquipmentAbilitySetResolver anyEquipmentSlot() {
        return new EquipmentAbilitySetResolverImpl(Set.of(EquipmentSlot.values()));
    }

    static EquipmentAbilitySetResolver anyArmorSlot() {
        return new EquipmentAbilitySetResolverImpl(Set.of(
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET));
    }

    static EquipmentAbilitySetResolver mainhand() {
        return new EquipmentAbilitySetResolverImpl(Set.of(EquipmentSlot.MAINHAND));
    }
}
