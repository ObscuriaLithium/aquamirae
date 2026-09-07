package dev.obscuria.aquamirae.neoforge;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.integration.hook.HookLivingEntity;
import dev.obscuria.aquamirae.neoforge.common.IceMazeBiomeModifier;
import dev.obscuria.fragmentum.content.registry.FragmentumRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(Aquamirae.MOD_ID)
public final class NeoAquamirae {

    public NeoAquamirae(IEventBus eventBus) {
        Aquamirae.init();
        eventBus.addListener(NeoAquamirae::registerCustomAttributes);
        registerBiomeModifiers();
    }

    private static void registerCustomAttributes(EntityAttributeModificationEvent event) {
        for (var entityType : event.getTypes()) {
            HookLivingEntity.createLivingAttributes(attribute -> event.add(entityType, attribute));
        }
    }

    private static void registerBiomeModifiers() {
        var registrar = FragmentumRegistry.registrar(Aquamirae.MOD_ID);
        registrar.register(
                NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS,
                Aquamirae.id("ice_maze"),
                () -> IceMazeBiomeModifier.CODEC);
    }
}