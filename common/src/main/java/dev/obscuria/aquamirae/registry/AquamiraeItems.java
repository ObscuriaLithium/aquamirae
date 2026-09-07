package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.api.common.ability.AbilitySetComponent;
import dev.obscuria.aquamirae.common.component.ItemTooltip;
import dev.obscuria.aquamirae.common.component.Keepsake;
import dev.obscuria.aquamirae.common.item.*;
import dev.obscuria.aquamirae.common.item.SeaFoodItem;
import dev.obscuria.aquamirae.common.item.SharpBonesItem;
import dev.obscuria.aquamirae.registry.references.AquamiraeJukeboxSongs;
import dev.obscuria.fragmentum.content.registry.DeferredItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;

import java.util.function.Supplier;

public final class AquamiraeItems {

    public static final DeferredItem<Item> RUNE_OF_THE_STORM;
    public static final DeferredItem<Item> SHELL_HORN;
    public static final DeferredItem<Item> DEAD_SEA_SCROLL;
    public static final DeferredItem<Item> CRACKED_RECORD;
    public static final DeferredItem<Item> MUSIC_DISC_HORIZON;
    public static final DeferredItem<Item> MUSIC_DISC_FORSAKEN_DROWNAGE;

    public static final DeferredItem<Item> GOLDEN_MOTH_SPAWN_EGG;
    public static final DeferredItem<Item> SPINEFISH_SPAWN_EGG;
    public static final DeferredItem<Item> ABYSSAL_SCYPHOID_SPAWN_EGG;
    public static final DeferredItem<Item> MAW_SPAWN_EGG;
    public static final DeferredItem<Item> ANGLERFISH_SPAWN_EGG;
    public static final DeferredItem<Item> MAZE_MOTHER_SPAWN_EGG;
    public static final DeferredItem<Item> CAPTAIN_CORNELIA_SPAWN_EGG;
    public static final DeferredItem<Item> EEL_SPAWN_EGG;
    public static final DeferredItem<Item> TORTURED_SOUL_SPAWN_EGG;
    public static final DeferredItem<Item> SCUTTLER_SPAWN_EGG;
    public static final DeferredItem<Item> SHELLBACK_SPAWN_EGG;

    public static final DeferredItem<Item> TERRIBLE_UPGRADE_SMITHING_TEMPLATE;
    public static final DeferredItem<Item> ABYSSAL_UPGRADE_SMITHING_TEMPLATE;

    public static final DeferredItem<Item> TERRIBLE_HELMET;
    public static final DeferredItem<Item> TERRIBLE_CHESTPLATE;
    public static final DeferredItem<Item> TERRIBLE_LEGGINGS;
    public static final DeferredItem<Item> TERRIBLE_BOOTS;

    public static final DeferredItem<Item> SALVAGER_HELMET;
    public static final DeferredItem<Item> SALVAGER_SUIT;
    public static final DeferredItem<Item> SALVAGER_WADERS;
    public static final DeferredItem<Item> SALVAGER_BOOTS;

    public static final DeferredItem<Item> ABYSSAL_TIARA;
    public static final DeferredItem<Item> ABYSSAL_HEAUME;
    public static final DeferredItem<Item> ABYSSAL_BRIGANDINE;
    public static final DeferredItem<Item> ABYSSAL_LEGGINGS;
    public static final DeferredItem<Item> ABYSSAL_BOOTS;

    public static final DeferredItem<Item> REMNANT_SABER;
    public static final DeferredItem<Item> TERRIBLE_CLEAVER;
    public static final DeferredItem<Item> TERRIBLE_BLADE;
    public static final DeferredItem<Item> TERRIBLE_FANG;
    public static final DeferredItem<Item> DAGGER_OF_GREED;
    public static final DeferredItem<Item> SHATTERBLADE;
    public static final DeferredItem<Item> SHATTERSPIKE;
    public static final DeferredItem<Item> TIDEPIERCER;
    public static final DeferredItem<Item> JAWBREAKER;
    public static final DeferredItem<Item> TEMPEST_TRIDENT;
    public static final DeferredItem<Item> DREADWAKE;

    public static final DeferredItem<Item> TERRIBLE_CHAKRAM;
    public static final DeferredItem<Item> MAZE_ROSE;

    public static final DeferredItem<Item> ECHO_COMPASS;
    public static final DeferredItem<Item> LOGBOOK;

    public static final DeferredItem<Item> SHIP_GRAVEYARD_ECHO;
    public static final DeferredItem<Item> WISTERIA_NIVEIS;
    public static final DeferredItem<Item> OXYHELIUM;
    public static final DeferredItem<Item> MAELSTROM_VESTIGE;
    public static final DeferredItem<Item> ABYSSAL_AMETHYST;
    public static final DeferredItem<Item> ANGLER_FANG;
    public static final DeferredItem<Item> FROZEN_KEY;

    public static final DeferredItem<Item> PIRATE_POUCH;
    public static final DeferredItem<Item> TREASURE_POUCH;

    public static final DeferredItem<Item> SHARP_BONES;
    public static final DeferredItem<Item> FIN;
    public static final DeferredItem<Item> ESCA;
    public static final DeferredItem<Item> ESCA_SKEWER;
    public static final DeferredItem<Item> SPINEFISH;
    public static final DeferredItem<Item> COOKED_SPINEFISH;
    public static final DeferredItem<Item> JELLYFISH_JELLY;
    public static final DeferredItem<Item> SEA_CASSEROLE;
    public static final DeferredItem<Item> SEA_STEW;
    public static final DeferredItem<Item> POSEIDON_BREAKFAST;
    public static final DeferredItem<Item> VOID_SQUID;

    public static final DeferredItem<Item> FROZEN_CHEST;
    public static final DeferredItem<Item> GOLDEN_MOTH_JAR;
    public static final DeferredItem<Item> LUMINESCENT_LAMP;
    public static final DeferredItem<Item> LUMINESCENT_BUBBLE;
    public static final DeferredItem<Item> PAINTING_ANGLERFISH;
    public static final DeferredItem<Item> PAINTING_OXYHELIUM;
    public static final DeferredItem<Item> PAINTING_TORTURED_SOUL;
    public static final DeferredItem<Item> PAINTING_AURORA;

    static {
        RUNE_OF_THE_STORM = register("rune_of_the_storm", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.rune_of_the_storm"))));
        SHELL_HORN = register("shell_horn", () -> new ShellHornItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.shell_horn"))));
        DEAD_SEA_SCROLL = register("dead_sea_scroll", () -> new Item(new Item.Properties()));
        CRACKED_RECORD = register("cracked_record", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(AquamiraeJukeboxSongs.CRACKED_RECORD).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.cracked_record"))));
        MUSIC_DISC_HORIZON = register("music_disc_horizon", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(AquamiraeJukeboxSongs.HORIZON)));
        MUSIC_DISC_FORSAKEN_DROWNAGE = register("music_disc_forsaken_drownage", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).jukeboxPlayable(AquamiraeJukeboxSongs.FORSAKEN_DROWNAGE)));

        GOLDEN_MOTH_SPAWN_EGG = register("golden_moth_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.GOLDEN_MOTH, -3381760, -205, new Item.Properties().rarity(Rarity.UNCOMMON)));
        SPINEFISH_SPAWN_EGG = register("spinefish_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.SPINEFISH, 1589067, 11451069, new Item.Properties()));
        ABYSSAL_SCYPHOID_SPAWN_EGG = register("abyssal_scyphoid_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.ABYSSAL_SCYPHOID, -39169, -10092442, new Item.Properties()));
        MAW_SPAWN_EGG = register("maw_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.MAW, -16764109, -16737895, new Item.Properties()));
        ANGLERFISH_SPAWN_EGG = register("anglerfish_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.ANGLERFISH, -12963033, -4942746, new Item.Properties()));
        MAZE_MOTHER_SPAWN_EGG = register("maze_mother_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.MAZE_MOTHER, -10092442, -39169, new Item.Properties()));
        CAPTAIN_CORNELIA_SPAWN_EGG = register("captain_cornelia_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.CAPTAIN_CORNELIA, -7760229, -13355980, new Item.Properties().rarity(Rarity.EPIC)));
        EEL_SPAWN_EGG = register("eel_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.EEL, -16764109, -16737844, new Item.Properties()));
        TORTURED_SOUL_SPAWN_EGG = register("tortured_soul_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.TORTURED_SOUL, -13421773, -16724737, new Item.Properties()));
        SCUTTLER_SPAWN_EGG = register("scuttler_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.SCUTTLER, 1589067, -16737895, new Item.Properties()));
        SHELLBACK_SPAWN_EGG = register("shellback_spawn_egg", () -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.SHELLBACK, 1589067, -16737895, new Item.Properties()));

        TERRIBLE_UPGRADE_SMITHING_TEMPLATE = register("terrible_upgrade_smithing_template", AquamiraeSmithingTemplateItem::createTerribleUpgradeTemplate);
        ABYSSAL_UPGRADE_SMITHING_TEMPLATE = register("abyssal_upgrade_smithing_template", AquamiraeSmithingTemplateItem::createAbyssalUpgradeTemplate);

        TERRIBLE_HELMET = register("terrible_helmet", () -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.TERRIBLE, ArmorItem.Type.HELMET, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.15), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_ARMOR))));
        TERRIBLE_CHESTPLATE = register("terrible_chestplate", () -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.TERRIBLE, ArmorItem.Type.CHESTPLATE, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.15), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_ARMOR))));
        TERRIBLE_LEGGINGS = register("terrible_leggings", () -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.TERRIBLE, ArmorItem.Type.LEGGINGS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.15), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_ARMOR))));
        TERRIBLE_BOOTS = register("terrible_boots", () -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.TERRIBLE, ArmorItem.Type.BOOTS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.15), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_ARMOR))));

        SALVAGER_HELMET = register("salvager_helmet", () -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.SALVAGER_ANCIENT, ArmorItem.Type.HELMET, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.5), new Item.Properties().rarity(Rarity.RARE).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SALVAGER_ARMOR))));
        SALVAGER_SUIT = register("salvager_suit", () -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.SALVAGER_MODERN, ArmorItem.Type.CHESTPLATE, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.1).addValue(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.33).addValue(Attributes.SUBMERGED_MINING_SPEED, 0.33), new Item.Properties().rarity(Rarity.RARE).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SALVAGER_ARMOR))));
        SALVAGER_WADERS = register("salvager_waders", () -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.SALVAGER_MODERN, ArmorItem.Type.LEGGINGS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.1).addValue(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.33).addValue(Attributes.SUBMERGED_MINING_SPEED, 0.33), new Item.Properties().rarity(Rarity.RARE).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SALVAGER_ARMOR))));
        SALVAGER_BOOTS = register("salvager_boots", () -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.SALVAGER_MODERN, ArmorItem.Type.BOOTS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.1).addValue(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.33).addValue(Attributes.SUBMERGED_MINING_SPEED, 0.33), new Item.Properties().rarity(Rarity.RARE).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SALVAGER_ARMOR))));

        ABYSSAL_TIARA = register("abyssal_tiara", () -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_LIGHT, ArmorItem.Type.HELMET, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.3), new Item.Properties().rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ABYSSAL_HEAUME = register("abyssal_heaume", () -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_HEAVY, ArmorItem.Type.HELMET, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.2), new Item.Properties().rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ABYSSAL_BRIGANDINE = register("abyssal_brigandine", () -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_HEAVY, ArmorItem.Type.CHESTPLATE, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.2), new Item.Properties().rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ABYSSAL_LEGGINGS = register("abyssal_leggings", () -> new AquamiraeArmorItem( true, AquamiraeArmorMaterials.ABYSSAL_HEAVY, ArmorItem.Type.LEGGINGS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.2), new Item.Properties().rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ABYSSAL_BOOTS = register("abyssal_boots", () -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_HEAVY, ArmorItem.Type.BOOTS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.2), new Item.Properties().rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));

        REMNANT_SABER = register("remnant_saber", () -> new SwordItem(AquamiraeTiers.REMNANT_SABER, new Item.Properties().attributes(SwordItemFactory.createAttributes(4, -2, 0.2)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.REMNANT_SABER))));
        TERRIBLE_CLEAVER = register("terrible_cleaver", () -> new SwordItem(AquamiraeTiers.TERRIBLE_CLEAVER, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(SwordItemFactory.createAttributes(6, -3, 0.4)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_CLEAVER))));
        TERRIBLE_BLADE = register("terrible_blade", () -> new SwordItem(AquamiraeTiers.TERRIBLE_BLADE, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(SwordItemFactory.createAttributes(4, -1f, 0.4)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_BLADE))));
        TERRIBLE_FANG = register("terrible_fang", () -> new SwordItem(AquamiraeTiers.TERRIBLE_FANG, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(SwordItemFactory.createAttributes(5, -2, 0.4)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_FANG))));
        DAGGER_OF_GREED = register("dagger_of_greed", () -> new SwordItem(AquamiraeTiers.DAGGER_OF_GREED, new Item.Properties().rarity(Rarity.RARE).attributes(SwordItemFactory.createAttributes(8, -2, 1)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.DAGGER_OF_GREED))));
        SHATTERBLADE = register("shatterblade", () -> new SwordItem(AquamiraeTiers.SHATTERBLADE, new Item.Properties().rarity(Rarity.EPIC).attributes(SwordItemFactory.createAttributes(6, -2.6, 0.6)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SHATTERBLADE)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        SHATTERSPIKE = register("shatterspike", () -> new SwordItem(AquamiraeTiers.SHATTERSPIKE, new Item.Properties().rarity(Rarity.EPIC).attributes(SwordItemFactory.createAttributes(13, -3.2, 0.6, 0.33)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SHATTERSPIKE)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        TIDEPIERCER = register("tidepiercer", () -> new SwordItem(AquamiraeTiers.TIDEPIERCER, new Item.Properties().rarity(Rarity.EPIC).fireResistant().attributes(SwordItemFactory.createAttributes(13, -2.8, 0.8, 0.33)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TIDEPIERCER))));
        JAWBREAKER = register("jawbreaker", () -> new JawbreakerItem(AquamiraeTiers.JAWBREAKER, new Item.Properties().rarity(Rarity.EPIC).fireResistant().food(new FoodProperties.Builder().saturationModifier(0.3f).nutrition(2).build()).attributes(SwordItemFactory.createAttributes(13, -2.8, 0.8, 0.33)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.JAWBREAKER))));
        TEMPEST_TRIDENT = register("tempest_trident", () -> new SwordItem(AquamiraeTiers.TEMPEST_TRIDENT, new Item.Properties().rarity(Rarity.EPIC).fireResistant().attributes(SwordItemFactory.createAttributes(13, -2.8, 0.8, 0.33)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TEMPEST_TRIDENT)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        DREADWAKE = register("dreadwake", () -> new DreadwakeItem(AquamiraeTiers.DREADWAKE, new Item.Properties().rarity(Rarity.EPIC).fireResistant().attributes(SwordItemFactory.createAttributes(6, -2, 0.5)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.DREADWAKE)).component(AquamiraeDataComponents.KEEPSAKE, new Keepsake("VenOos")).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));

        TERRIBLE_CHAKRAM = register("terrible_chakram", () -> new TerribleChakramItem(new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON).component(DataComponents.UNBREAKABLE, new Unbreakable(true)).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.chakram"))));
        MAZE_ROSE = register("maze_rose", () -> new MazeRoseItem(new Item.Properties().stacksTo(5).rarity(Rarity.EPIC).component(DataComponents.UNBREAKABLE, new Unbreakable(true)).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.chakram"))));

        ECHO_COMPASS = register("echo_compass", () -> new EchoCompassItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.echo_compass"))));
        LOGBOOK = register("logbook", () -> new LogBookItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

        SHIP_GRAVEYARD_ECHO = register("ship_graveyard_echo", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE).component(AquamiraeDataComponents.NO_GRAVITY, Unit.INSTANCE).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.ship_graveyard_echo"))));
        WISTERIA_NIVEIS = register("wisteria_niveis", () -> new WisteriaNiveisItem(new Item.Properties().rarity(Rarity.COMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.wisteria_niveis"))));
        OXYHELIUM = register("oxyhelium", () -> new Item(new Item.Properties()));
        MAELSTROM_VESTIGE = register("maelstrom_vestige", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).component(AquamiraeDataComponents.NO_GRAVITY, Unit.INSTANCE).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.maelstrom_vestige"))));
        ABYSSAL_AMETHYST = register("abyssal_amethyst", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.abyssal_amethyst"))));
        ANGLER_FANG = register("angler_fang", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
        FROZEN_KEY = register("frozen_key", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.frozen_key"))));

        PIRATE_POUCH = register("pirate_pouch", () -> new StashItem(AquamiraeBlocks.PIRATE_POUCH, AquamiraeSounds.ITEM_PIRATE_POUCH_OPEN, new Item.Properties().component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.pirate_pouch"))));
        TREASURE_POUCH = register("treasure_pouch", () -> new StashItem(AquamiraeBlocks.TREASURE_POUCH, AquamiraeSounds.ITEM_TREASURE_POUCH_OPEN, new Item.Properties().component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.treasure_pouch"))));

        SHARP_BONES = register("sharp_bones", () -> new SharpBonesItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0f).build())));
        FIN = register("fin", () -> new SeaFoodItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0f).build())));
        ESCA = register("esca", () -> new SeaFoodItem(new Item.Properties().food(new FoodProperties.Builder().effect(new MobEffectInstance(AquamiraeEffects.FURY_OF_THE_DEPTHS.holder(), 3600, 0), 1).saturationModifier(1.2f).nutrition(2).alwaysEdible().build()).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ESCA_SKEWER = register("esca_skewer", () -> new SeaFoodItem(new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().saturationModifier(1.2f).nutrition(8).usingConvertsTo(Items.STICK).effect(new MobEffectInstance(AquamiraeEffects.FURY_OF_THE_DEPTHS.holder(), 9600, 2), 1).build()).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.esca_skewer"))));
        SPINEFISH = register("spinefish", () -> new SeaFoodItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0f).usingConvertsTo(SHARP_BONES::get).build()).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.spinefish"))));
        COOKED_SPINEFISH = register("cooked_spinefish", () -> new SeaFoodItem(new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().saturationModifier(0.8f).nutrition(8).usingConvertsTo(SHARP_BONES::get).effect(new MobEffectInstance(AquamiraeEffects.RESILIENCE_OF_THE_DEPTHS.holder(), 3600, 1), 1f).build()).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.cooked_spinefish"))));
        JELLYFISH_JELLY = register("jellyfish_jelly", () -> new SeaFoodItem(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(1).saturationModifier(2f).effect(new MobEffectInstance(MobEffects.POISON, 60, 2), 0.1f).build()).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.jellyfish_jelly"))));
        SEA_CASSEROLE = register("sea_casserole", () -> new SeaFoodItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.4f).alwaysEdible().effect(new MobEffectInstance(AquamiraeEffects.RESILIENCE_OF_THE_DEPTHS.holder(), 9600, 2), 1).build())));
        SEA_STEW = register("sea_stew", () -> new SeaFoodItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().alwaysEdible().nutrition(12).saturationModifier(1.2f).usingConvertsTo(Items.BOWL).effect(new MobEffectInstance(AquamiraeEffects.RESILIENCE_OF_THE_DEPTHS.holder(), 6000, 2), 1).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 0), 1).build())));
        POSEIDON_BREAKFAST = register("poseidon_breakfast", () -> new SeaFoodItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).food(new FoodProperties.Builder().alwaysEdible().nutrition(20).saturationModifier(1.6f).usingConvertsTo(Items.BOWL).effect(new MobEffectInstance(AquamiraeEffects.FURY_OF_THE_DEPTHS.holder(), 12000, 3), 1).effect(new MobEffectInstance(AquamiraeEffects.RESILIENCE_OF_THE_DEPTHS.holder(), 12000, 3), 1).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 12000, 1), 1).build()).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
        VOID_SQUID = register("void_squid", () -> new SeaFoodItem(new Item.Properties().stacksTo(8).rarity(Rarity.EPIC).food(new FoodProperties.Builder().alwaysEdible().nutrition(20).saturationModifier(2f).effect(new MobEffectInstance(AquamiraeEffects.DEVOURING_CURSE.holder(), 12000, 0), 1).build()).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.void_squid"))));

        FROZEN_CHEST = register("frozen_chest", () -> new BlockItem(AquamiraeBlocks.FROZEN_CHEST.get(), new Item.Properties().rarity(Rarity.EPIC)));
        GOLDEN_MOTH_JAR = register("golden_moth_jar", () -> new BlockItem(AquamiraeBlocks.GOLDEN_MOTH_JAR.get(), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.golden_moth_jar"))));
        LUMINESCENT_LAMP = register("luminescent_lamp", () -> new BlockItem(AquamiraeBlocks.LUMINESCENT_LAMP.get(), new Item.Properties().component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.luminescent_lamp"))));
        LUMINESCENT_BUBBLE = register("luminescent_bubble", () -> new LuminescentBubbleItem(new Item.Properties().component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.luminescent_bubble"))));
        PAINTING_ANGLERFISH = register("painting_anglerfish", () -> new BlockItem(AquamiraeBlocks.PAINTING_ANGLERFISH.get(), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.painting_anglerfish"))));
        PAINTING_OXYHELIUM = register("painting_oxyhelium", () -> new BlockItem(AquamiraeBlocks.PAINTING_OXYHELIUM.get(), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.painting_oxyhelium"))));
        PAINTING_TORTURED_SOUL = register("painting_tortured_soul", () -> new BlockItem(AquamiraeBlocks.PAINTING_TORTURED_SOUL.get(), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.painting_tortured_soul"))));
        PAINTING_AURORA = register("painting_aurora", () -> new BlockItem(AquamiraeBlocks.PAINTING_AURORA.get(), new Item.Properties().rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.painting_aurora"))));
    }

    private static DeferredItem<Item> register(String name, Supplier<Item> supplier) {
        return AquamiraeRegistries.REGISTRAR.registerItem(Aquamirae.id(name), supplier);
    }

    static void init() {}
}