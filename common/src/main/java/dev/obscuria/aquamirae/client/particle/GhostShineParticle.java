package dev.obscuria.aquamirae.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class GhostShineParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;
    private final float angularAcceleration;
    private float angularVelocity;

    protected GhostShineParticle(
            ClientLevel level,
            double x, double y, double z,
            double vx, double vy, double vz,
            SpriteSet spriteSet
    ) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.quadSize *= 0.8f;
        this.lifetime = 30;
        this.gravity = -0.1f;
        this.hasPhysics = false;
        this.xd = vx * 0.1;
        this.yd = vy * 0.1;
        this.zd = vz * 0.1;
        this.angularVelocity = 0.1f;
        this.angularAcceleration = 0.01f;
        this.setSpriteFromAge(spriteSet);
        this.setSize(0.2f, 0.2f);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = roll;
        this.roll += angularVelocity;
        this.angularVelocity += this.angularAcceleration;
        if (!removed) this.setSprite(spriteSet.get((age / 3) % 12 + 1, 12));
    }

    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(
                SimpleParticleType type, ClientLevel level,
                double x, double y, double z,
                double vx, double vy, double vz
        ) {
            return new GhostShineParticle(level, x, y, z, vx, vy, vz, this.spriteSet);
        }
    }
}
