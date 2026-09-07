package dev.obscuria.aquamirae.registry.references;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

public interface AquamiraeJukeboxSongs {

    ResourceKey<JukeboxSong> CRACKED_RECORD = create("cracked_record");
    ResourceKey<JukeboxSong> HORIZON = create("horizon");
    ResourceKey<JukeboxSong> FORSAKEN_DROWNAGE = create("forsaken_drownage");

    private static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Aquamirae.id(name));
    }
}
