package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.common.entity.Scuttler;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EchoCompassItem extends Item {

    public EchoCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide || entity.tickCount % 20 != 0) return;
        var entities = level.getEntities(entity, entity.getBoundingBox().inflate(128));
        for (var target : entities) {
            if (!hasEcho(target)) continue;
            stack.set(AquamiraeDataComponents.ECHO_TRACKER, GlobalPos.of(level.dimension(), target.blockPosition()));
            return;
        }
        stack.remove(AquamiraeDataComponents.ECHO_TRACKER);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(AquamiraeDataComponents.ECHO_TRACKER);
    }

    private static boolean hasEcho(Entity entity) {
        if (entity instanceof ItemEntity item) {
            return item.getItem().is(AquamiraeItems.SHIP_GRAVEYARD_ECHO.asItem());
        } else if (entity instanceof Scuttler scuttler) {
            return scuttler.getStolenItem().is(AquamiraeItems.SHIP_GRAVEYARD_ECHO.asItem());
        } else return false;
    }
}
