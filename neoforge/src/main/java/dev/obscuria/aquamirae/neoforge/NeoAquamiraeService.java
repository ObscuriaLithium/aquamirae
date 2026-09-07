package dev.obscuria.aquamirae.neoforge;

import dev.obscuria.aquamirae.AquamiraeService;
import dev.obscuria.fragmentum.content.registry.DeferredEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

public final class NeoAquamiraeService implements AquamiraeService {

    @Override
    public <T extends Mob> SpawnEggItem newSpawnEggItem(DeferredEntity<T> entitySupplier, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new DeferredSpawnEggItem(entitySupplier, backgroundColor, highlightColor, properties);
    }
}
