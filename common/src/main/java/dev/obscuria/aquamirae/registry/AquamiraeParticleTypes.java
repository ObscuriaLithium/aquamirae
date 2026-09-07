package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.fragmentum.content.registry.DeferredParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public final class AquamiraeParticleTypes {

    public static final DeferredParticle<SimpleParticleType> SHINE = register("shine", () -> new SimpleParticleType(true));
    public static final DeferredParticle<SimpleParticleType> GHOST_SHINE = register("ghost_shine", () -> new SimpleParticleType(true));
    public static final DeferredParticle<SimpleParticleType> GHOST = register("ghost", () -> new SimpleParticleType(true));

    private static <T extends ParticleOptions> DeferredParticle<T> register(String name, Supplier<ParticleType<T>> supplier) {
        return AquamiraeRegistries.REGISTRAR.registerParticle(Aquamirae.id(name), supplier);
    }

    static void init() {}
}
