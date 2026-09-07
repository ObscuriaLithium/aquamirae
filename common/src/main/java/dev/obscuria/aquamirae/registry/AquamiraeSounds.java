package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.fragmentum.content.registry.Deferred;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public final class AquamiraeSounds {

    public static final SoundEvent AMBIENT_BLIZZARD = register("ambient.blizzard");
    public static final SoundEvent AMBIENT_SHIP_HORN = register("ambient.ship_horn");

    public static final SoundEvent EVENT_EARTHQUAKE = register("event.earthquake");
    public static final SoundEvent EVENT_GLACIER_CRACK = register("event.glacier_crack");
    public static final SoundEvent EVENT_AIR_SUPPLY = register("event.air_supply");

    public static final SoundEvent ITEM_PIRATE_POUCH_OPEN = register("stack.pirate_pouch.open");
    public static final SoundEvent ITEM_TREASURE_POUCH_OPEN = register("stack.treasure_pouch.open");
    public static final SoundEvent ITEM_SHELL_HORN_USE = register("stack.shell_horn.use");

    public static final SoundEvent BLOCK_FROZEN_CHEST_UNLOCK = register("block.frozen_chest.unlock");

    public static final SoundEvent ENTITY_SCUTTLER_STEP = register("entity.scuttler.step");
    public static final SoundEvent ENTITY_SCUTTLER_HURT = register("entity.scuttler.hurt");
    public static final SoundEvent ENTITY_SCUTTLER_UPSIDED = register("entity.scuttler.upsided");
    public static final SoundEvent ENTITY_SCUTTLER_DEATH = register("entity.scuttler.death");
    public static final SoundEvent ENTITY_MAZE_MOTHER_MOAN = register("entity.maze_mother.moan");
    public static final SoundEvent ENTITY_GENERIC_DEEP_AMBIENT = register("entity.generic_deep.ambient");
    public static final SoundEvent ENTITY_GENERIC_DEEP_HURT = register("entity.generic_deep.hurt");
    public static final SoundEvent ENTITY_GENERIC_DEEP_DEATH = register("entity.generic_deep.death");
    public static final SoundEvent ENTITY_GENERIC_DEEP_BITE = register("entity.generic_deep.bite");
    public static final SoundEvent ENTITY_GOLDEN_MOTH_AMBIENT = register("entity.golden_moth.ambient");
    public static final SoundEvent ENTITY_GOLDEN_MOTH_CATCH = register("entity.golden_moth.catch");
    public static final SoundEvent ENTITY_CAPTAIN_CORNELIA_AMBIENT = register("entity.captain_cornelia.ambient");
    public static final SoundEvent ENTITY_CAPTAIN_CORNELIA_HURT = register("entity.captain_cornelia.hurt");
    public static final SoundEvent ENTITY_CAPTAIN_CORNELIA_DEATH = register("entity.captain_cornelia.death");
    public static final SoundEvent ENTITY_CAPTAIN_CORNELIA_HORN = register("entity.captain_cornelia.horn");
    public static final SoundEvent ENTITY_CAPTAIN_CORNELIA_RAGE = register("entity.captain_cornelia.rage");
    public static final SoundEvent ENTITY_CAPTAIN_CORNELIA_ATTACK_1 = register("entity.captain_cornelia.attack_1");
    public static final SoundEvent ENTITY_CAPTAIN_CORNELIA_ATTACK_2 = register("entity.captain_cornelia.attack_2");
    public static final SoundEvent ENTITY_EEL_ROAR = register("entity.eel.roar");

    public static final SoundEvent RECORD_CRACKED_RECORD = register("record.cracked_record");
    public static final SoundEvent RECORD_HORIZON = register("record.horizon");
    public static final SoundEvent RECORD_FORSAKEN_DROWNAGE = register("record.forsaken_drownage");

    public static final Deferred<SoundEvent, SoundEvent> AMBIENT_ICE_MAZE_LOOP = registerHolder("ambient.ice_maze.loop");
    public static final Deferred<SoundEvent, SoundEvent> MUSIC_TITLE_SCREEN_THEME = registerHolder("music.title_screen_theme");
    public static final Deferred<SoundEvent, SoundEvent> MUSIC_BLIZZARD_THEME = registerHolder("music.blizzard_theme");
    public static final Deferred<SoundEvent, SoundEvent> MUSIC_ICE_MAZE_THEME = registerHolder("music.ice_maze_theme");
    public static final Deferred<SoundEvent, SoundEvent> MUSIC_FORSAKEN_DROWNAGE = registerHolder("music.forsaken_drownage");

    private static SoundEvent register(String name) {
        var soundEvent = SoundEvent.createVariableRangeEvent(Aquamirae.id(name));
        AquamiraeRegistries.REGISTRAR.register(Registries.SOUND_EVENT, Aquamirae.id(name), () -> soundEvent);
        return soundEvent;
    }

    private static Deferred<SoundEvent, SoundEvent> registerHolder(String name) {
        var soundEvent = SoundEvent.createVariableRangeEvent(Aquamirae.id(name));
        return AquamiraeRegistries.REGISTRAR.register(Registries.SOUND_EVENT, Aquamirae.id(name), () -> soundEvent);
    }

    static void init() {}
}
