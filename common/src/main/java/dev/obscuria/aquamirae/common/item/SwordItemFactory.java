package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.registry.AquamiraeAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public interface SwordItemFactory {

    ResourceLocation BASE_DEPTHS_FURY_ID = Aquamirae.id("base_depths_fury");
    ResourceLocation BASE_ENTITY_INTERACTION_RANGE_ID = Aquamirae.id("base_entity_interaction_range");

    static ItemAttributeModifiers createAttributes(int attackDamage, double attackSpeed, double depthsFury) {
        return createBaseAttributes(attackDamage, attackSpeed)
                .add(AquamiraeAttributes.DEPTHS_FURY.holder(), new AttributeModifier(BASE_DEPTHS_FURY_ID, depthsFury, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    static ItemAttributeModifiers createAttributes(int attackDamage, double attackSpeed, double depthsFury, double rangeBonus) {
        return createBaseAttributes(attackDamage, attackSpeed)
                .add(AquamiraeAttributes.DEPTHS_FURY.holder(), new AttributeModifier(BASE_DEPTHS_FURY_ID, depthsFury, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(BASE_ENTITY_INTERACTION_RANGE_ID, rangeBonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    private static ItemAttributeModifiers.Builder createBaseAttributes(int attackDamage, double attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
    }
}
