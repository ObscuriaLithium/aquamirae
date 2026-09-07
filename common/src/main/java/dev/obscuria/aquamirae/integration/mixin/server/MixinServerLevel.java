package dev.obscuria.aquamirae.integration.mixin.server;

import dev.obscuria.aquamirae.api.server.TickableEvent;
import dev.obscuria.aquamirae.api.common.entity.SubPartEntity;
import dev.obscuria.aquamirae.integration.extension.server.ExtensionServerLevel;
import dev.obscuria.aquamirae.integration.hook.server.HookServerLevel;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel implements ExtensionServerLevel {

    private final @Unique Int2ObjectMap<SubPartEntity<?>> aquamirae$subPartEntities = new Int2ObjectOpenHashMap<>();
    private final @Unique List<TickableEvent> aquamirae$tickableEvents = new ArrayList<>();

    @Override
    public Int2ObjectMap<SubPartEntity<?>> aquamirae$getSubParts() {
        return aquamirae$subPartEntities;
    }

    @Override
    public void aquamirae$triggerEvent(TickableEvent event) {
        this.aquamirae$tickableEvents.add(event);
        event.onStart((ServerLevel) (Object) this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick$onTail(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        var self = (ServerLevel) (Object) this;
        aquamirae$tickableEvents.removeIf(event -> {
            event.tick(self);
            if (event.isFinished()) {
                event.onStop(self);
                return true;
            }
            return false;
        });
    }

    @Inject(method = "getEntityOrPart", at = @At("RETURN"), cancellable = true)
    private void getEntityOrPart$modifyResult(int id, CallbackInfoReturnable<@Nullable Entity> cir) {
        if (cir.getReturnValue() != null) return;
        cir.setReturnValue(HookServerLevel.supplyEntityPart((ServerLevel) (Object) this, id));
    }
}
