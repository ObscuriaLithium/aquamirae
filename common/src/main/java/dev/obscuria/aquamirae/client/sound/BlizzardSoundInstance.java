package dev.obscuria.aquamirae.client.sound;

import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public final class BlizzardSoundInstance extends AbstractTickableSoundInstance {

    private final Player player;

    public BlizzardSoundInstance(Player player) {
        super(AquamiraeSounds.AMBIENT_BLIZZARD, SoundSource.WEATHER, RandomSource.create());
        this.player = player;
        this.volume = 0.2f;
        this.looping = true;
    }

    @Override
    public void tick() {
        if (canContinue()) {
            this.volume = Math.min(1f, volume + 0.025f);
        } else {
            this.volume = Math.max(0f, volume - 0.025f);
            if (volume <= 0f) stop();
        }
    }

    private boolean canContinue() {
        return player.level().isRaining()
                && AquamiraeHelper.isInIceMaze(player);
    }
}
