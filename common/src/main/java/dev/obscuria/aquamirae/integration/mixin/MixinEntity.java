package dev.obscuria.aquamirae.integration.mixin;

import dev.obscuria.aquamirae.registry.AquamiraeItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @ModifyVariable(method = "shouldRenderAtSqrDistance", at = @At("HEAD"), argsOnly = true)
    private double shouldRenderAtSqrDistance$modifyDistance(double distance) {
        var self = (Entity) (Object) this;
        if (!(self instanceof ItemEntity entity)) return distance;
        if (!entity.getItem().is(AquamiraeItems.SHIP_GRAVEYARD_ECHO.asItem())) return distance;
        return distance * 0.2;
    }
}