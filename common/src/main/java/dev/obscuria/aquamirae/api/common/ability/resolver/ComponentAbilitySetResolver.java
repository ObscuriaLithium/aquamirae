package dev.obscuria.aquamirae.api.common.ability.resolver;

import dev.obscuria.aquamirae.common.ability.ComponentAbilitySetResolverImpl;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.EquipmentSlot;

public interface ComponentAbilitySetResolver extends AbilitySetResolver {

    static ComponentAbilitySetResolver mainhand(DataComponentType<Integer> componentType) {
        return new ComponentAbilitySetResolverImpl(componentType, EquipmentSlot.MAINHAND);
    }
}
