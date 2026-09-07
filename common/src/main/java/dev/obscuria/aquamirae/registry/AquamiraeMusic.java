package dev.obscuria.aquamirae.registry;

import com.google.common.base.Suppliers;
import net.minecraft.sounds.Music;

import java.util.function.Supplier;

public final class AquamiraeMusic {

    public static final Supplier<Music> TITLE_SCREEN_THEME;
    public static final Supplier<Music> BLIZZARD_THEME;
    public static final Supplier<Music> ICE_MAZE_THEME;
    public static final Supplier<Music> FORSAKEN_DROWNAGE;

    static {
        TITLE_SCREEN_THEME = Suppliers.memoize(() -> new Music(AquamiraeSounds.MUSIC_TITLE_SCREEN_THEME.holder(), 40, 60, true));
        BLIZZARD_THEME = Suppliers.memoize(() -> new Music(AquamiraeSounds.MUSIC_BLIZZARD_THEME.holder(), 120, 600, false));
        ICE_MAZE_THEME = Suppliers.memoize(() -> new Music(AquamiraeSounds.MUSIC_ICE_MAZE_THEME.holder(), 1200, 6000, false));
        FORSAKEN_DROWNAGE = Suppliers.memoize(() -> new Music(AquamiraeSounds.MUSIC_FORSAKEN_DROWNAGE.holder(), 0, 0, true));
    }
}