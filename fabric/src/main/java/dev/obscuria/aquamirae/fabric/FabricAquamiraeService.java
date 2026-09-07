package dev.obscuria.aquamirae.fabric;

import dev.obscuria.aquamirae.AquamiraeService;
import dev.obscuria.fragmentum.content.registry.DeferredEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class FabricAquamiraeService implements AquamiraeService {

    @Override
    public <T extends Mob> SpawnEggItem newSpawnEggItem(DeferredEntity<T> entity, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new SpawnEggItem(entity.get(),  backgroundColor, highlightColor, properties);
    }
}
