package dev.obscuria.aquamirae;

import dev.obscuria.fragmentum.content.registry.DeferredEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public interface AquamiraeService {

    <T extends Mob> SpawnEggItem newSpawnEggItem(DeferredEntity<T> entity, int backgroundColor, int highlightColor, Item.Properties properties);
}