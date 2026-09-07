package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.component.ItemTooltip;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class AquamiraeSmithingTemplateItem extends SmithingTemplateItem {

    private static final Component TERRIBLE_DESCRIPTION = Component.translatable(Util.makeDescriptionId("upgrade", Aquamirae.id("terrible"))).withStyle(ChatFormatting.GRAY);
    private static final Component TERRIBLE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", Aquamirae.id("terrible_upgrade_smithing_template.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component TERRIBLE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", Aquamirae.id("terrible_upgrade_smithing_template.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component TERRIBLE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Aquamirae.id("terrible_upgrade_smithing_template.base_slot_description")));
    private static final Component TERRIBLE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Aquamirae.id("terrible_upgrade_smithing_template.additions_slot_description")));

    private static final Component ABYSSAL_DESCRIPTION = Component.translatable(Util.makeDescriptionId("upgrade", Aquamirae.id("abyssal"))).withStyle(ChatFormatting.GRAY);
    private static final Component ABYSSAL_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", Aquamirae.id("abyssal_upgrade_smithing_template.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component ABYSSAL_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", Aquamirae.id("abyssal_upgrade_smithing_template.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component ABYSSAL_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Aquamirae.id("abyssal_upgrade_smithing_template.base_slot_description")));
    private static final Component ABYSSAL_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Aquamirae.id("abyssal_upgrade_smithing_template.additions_slot_description")));

    private static final ItemTooltip TOOLTIP = ItemTooltip.translate("tooltip.aquamirae.smithing_template", 2, true, false);

    public AquamiraeSmithingTemplateItem(
            Component appliesTo, Component ingredients, Component upgradeDescription,
            Component baseSlotDescription, Component additionsSlotDescription,
            List<ResourceLocation> baseSlotEmptyIcons,
            List<ResourceLocation> additionalSlotEmptyIcons,
            FeatureFlag... requiredFeatures
    ) {
        super(
                appliesTo, ingredients, upgradeDescription, baseSlotDescription,
                additionsSlotDescription, baseSlotEmptyIcons,
                additionalSlotEmptyIcons, requiredFeatures);
        this.components = DataComponentMap.composite(components(), DataComponentMap.builder()
                .set(AquamiraeDataComponents.TOOLTIP, TOOLTIP)
                .build());
    }

    public static AquamiraeSmithingTemplateItem createTerribleUpgradeTemplate() {
        return new AquamiraeSmithingTemplateItem(
                TERRIBLE_APPLIES_TO, TERRIBLE_INGREDIENTS, TERRIBLE_DESCRIPTION,
                TERRIBLE_BASE_SLOT_DESCRIPTION, TERRIBLE_ADDITIONS_SLOT_DESCRIPTION,
                List.of(ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
                        ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
                        ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
                        ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots")),
                List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_amethyst_shard")));
    }

    public static AquamiraeSmithingTemplateItem createAbyssalUpgradeTemplate() {
        return new AquamiraeSmithingTemplateItem(
                ABYSSAL_APPLIES_TO, ABYSSAL_INGREDIENTS, ABYSSAL_DESCRIPTION,
                ABYSSAL_BASE_SLOT_DESCRIPTION, ABYSSAL_ADDITIONS_SLOT_DESCRIPTION,
                List.of(ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
                        ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
                        ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
                        ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots")),
                List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_amethyst_shard")));
    }
}
