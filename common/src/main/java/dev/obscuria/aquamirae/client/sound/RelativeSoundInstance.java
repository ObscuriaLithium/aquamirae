package dev.obscuria.aquamirae.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RelativeSoundInstance extends AbstractTickableSoundInstance {

    private final double relativeX;
    private final double relativeY;
    private final double relativeZ;

    public RelativeSoundInstance(SoundEvent soundEvent, Vec3 pos, float volume, float pitch) {
        this(soundEvent, pos.x, pos.y, pos.z, volume, pitch);
    }

    public RelativeSoundInstance(
            SoundEvent sound,
            double relativeX,
            double relativeY,
            double relativeZ,
            float volume,
            float pitch
    ) {
        super(sound, SoundSource.AMBIENT, RandomSource.create());

        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeZ = relativeZ;

        this.volume = volume;
        this.pitch = pitch;
        this.looping = false;
        this.attenuation = Attenuation.LINEAR;

        updatePosition();
    }

    @Override
    public void tick() {
        updatePosition();
    }

    private void updatePosition() {
        @Nullable var player = Minecraft.getInstance().player;
        if (player == null) return;
        this.x = player.getX() + relativeX;
        this.y = player.getY() + relativeY;
        this.z = player.getZ() + relativeZ;
    }
}