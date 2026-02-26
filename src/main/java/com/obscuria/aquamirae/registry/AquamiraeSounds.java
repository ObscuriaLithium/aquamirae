
package com.obscuria.aquamirae.registry;

import com.obscuria.aquamirae.Aquamirae;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class AquamiraeSounds {

	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Aquamirae.MODID);

    public static final RegistryObject<SoundEvent> AMBIENT_ICE_MAZE_LOOP = REGISTRY.register("ambient.ice_maze.loop",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "ambient.ice_maze.loop")));
    public static final RegistryObject<SoundEvent> ENTITY_GOLDEN_MOTH_CATCH = REGISTRY.register("entity.golden_moth.catch",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.golden_moth.catch")));
    public static final RegistryObject<SoundEvent> ENTITY_GOLDEN_MOTH_AMBIENT = REGISTRY.register("entity.golden_moth.ambient",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.golden_moth.ambient")));
    public static final RegistryObject<SoundEvent> BLOCK_FROZEN_CHEST_UNLOCK = REGISTRY.register("block.frozen_chest.unlock",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "block.frozen_chest.unlock")));
    public static final RegistryObject<SoundEvent> EFFECT_OXYGEN = REGISTRY.register("effect.oxygen",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "effect.oxygen")));
    public static final RegistryObject<SoundEvent> AMBIENT_SHIP_HORN = REGISTRY.register("ambient.ship_horn",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "ambient.ship_horn")));
    public static final RegistryObject<SoundEvent> ITEM_SHELL_HORN_USE = REGISTRY.register("item.shell_horn.use",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "item.shell_horn.use")));
    public static final RegistryObject<SoundEvent> ITEM_TERRIBLE_SWORD = REGISTRY.register("item.terrible_sword",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "item.terrible_sword")));
    public static final RegistryObject<SoundEvent> ITEM_TREASURE_POUCH_OPEN = REGISTRY.register("item.treasure_pouch.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "item.treasure_pouch.open")));
    public static final RegistryObject<SoundEvent> ITEM_POUCH_OPEN = REGISTRY.register("item.pouch.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "item.pouch.open")));
    public static final RegistryObject<SoundEvent> MUSIC_FORSAKEN_DROWNAGE = REGISTRY.register("music.forsaken_drownage",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "music.forsaken_drownage")));
    public static final RegistryObject<SoundEvent> ENTITY_CAPTAIN_CORNELIA_HORN = REGISTRY.register("entity.captain_cornelia.horn",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.captain_cornelia.horn")));
    public static final RegistryObject<SoundEvent> ENTITY_CAPTAIN_CORNELIA_ATTACK_1 = REGISTRY.register("entity.captain_cornelia.attack_1",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.captain_cornelia.attack_1")));
    public static final RegistryObject<SoundEvent> ENTITY_CAPTAIN_CORNELIA_ATTACK_2 = REGISTRY.register("entity.captain_cornelia.attack_2",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.captain_cornelia.attack_2")));
    public static final RegistryObject<SoundEvent> ENTITY_DEEP_AMBIENT = REGISTRY.register("entity.deep_ambient",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.deep_ambient")));
    public static final RegistryObject<SoundEvent> ENTITY_DEEP_HURT = REGISTRY.register("entity.deep_hurt",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.deep_hurt")));
    public static final RegistryObject<SoundEvent> ENTITY_DEEP_DEATH = REGISTRY.register("entity.deep_death",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.deep_death")));
    public static final RegistryObject<SoundEvent> ENTITY_CAPTAIN_CORNELIA_AMBIENT = REGISTRY.register("entity.captain_cornelia.ambient",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.captain_cornelia.ambient")));
    public static final RegistryObject<SoundEvent> EFFECT_MYSTERY = REGISTRY.register("effect.mystery",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "effect.mystery")));
    public static final RegistryObject<SoundEvent> ITEM_SCROLL_USE = REGISTRY.register("item.scroll.use",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "item.scroll.use")));
    public static final RegistryObject<SoundEvent> ENTITY_CAPTAIN_CORNELIA_HURT = REGISTRY.register("entity.captain_cornelia.hurt",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.captain_cornelia.hurt")));
    public static final RegistryObject<SoundEvent> ENTITY_CAPTAIN_CORNELIA_DEATH = REGISTRY.register("entity.captain_cornelia.death",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.captain_cornelia.death")));
    public static final RegistryObject<SoundEvent> ENTITY_EEL_BITE = REGISTRY.register("entity.eel.bite",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.eel.bite")));
    public static final RegistryObject<SoundEvent> ENTITY_EEL_ROAR = REGISTRY.register("entity.eel.roar",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.eel.roar")));
    public static final RegistryObject<SoundEvent> ENTITY_CAPTAIN_CORNELIA_RAGE = REGISTRY.register("entity.captain_cornelia.rage",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "entity.captain_cornelia.rage")));
    public static final RegistryObject<SoundEvent> MUSIC_ICE_MAZE_THEME = REGISTRY.register("music.ice_maze_theme",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "music.ice_maze_theme")));
    public static final RegistryObject<SoundEvent> RECORD_HORIZON = REGISTRY.register("record.horizon",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "record.horizon")));
    public static final RegistryObject<SoundEvent> RECORD_FORSAKEN_DROWNAGE = REGISTRY.register("record.forsaken_drownage",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquamirae.MODID, "record.forsaken_drownage")));
}
