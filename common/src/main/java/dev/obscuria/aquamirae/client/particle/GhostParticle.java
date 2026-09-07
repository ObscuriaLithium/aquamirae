package dev.obscuria.aquamirae.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class GhostParticle extends TextureSheetParticle {

	private final SpriteSet spriteSet;

	protected GhostParticle(
			ClientLevel level,
			double x, double y, double z,
			double vx, double vy, double vz,
			SpriteSet spriteSet
	) {
		super(level, x, y, z);
		this.spriteSet = spriteSet;
		this.setSize(0.2f, 0.2f);
		this.quadSize *= 1.3f;
		this.lifetime = 40;
		this.gravity = -0.3f;
		this.hasPhysics = false;
		this.xd = vx * 0.1;
		this.yd = vy * 0.1;
		this.zd = vz * 0.1;
		this.setSpriteFromAge(spriteSet);
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
		if (!this.removed) this.setSprite(this.spriteSet.get((this.age / 3) % 12 + 1, 12));
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {

		private final SpriteSet spriteSet;

		public Provider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(
				SimpleParticleType type, ClientLevel level,
				double x, double y, double z,
				double vx, double vy, double vz
		) {
			return new GhostParticle(level, x, y, z, vx, vy, vz, spriteSet);
		}
	}
}
