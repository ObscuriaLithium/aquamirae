package dev.obscuria.aquamirae.server.event;

import dev.obscuria.aquamirae.api.server.TickableEvent;
import dev.obscuria.aquamirae.registry.references.AquamiraeDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record CrystallizationEvent(
        LivingEntity entity
) implements TickableEvent {

    @Override
    public void tick(ServerLevel level) {
        var registry = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        var type = registry.getHolderOrThrow(AquamiraeDamageTypes.CRYSTALLIZATION);
        this.entity.hurt(new DamageSource(type), 999999f);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
