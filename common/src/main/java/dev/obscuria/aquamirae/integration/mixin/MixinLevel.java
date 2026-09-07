package dev.obscuria.aquamirae.integration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.api.common.entity.MultipartEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(Level.class)
public abstract class MixinLevel {

    @WrapOperation(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/entity/LevelEntityGetter;get(Lnet/minecraft/world/phys/AABB;Ljava/util/function/Consumer;)V"))
    private static <T extends Entity> void getEntities$modifyResult(
            LevelEntityGetter<T> instance, AABB aabb,
            Consumer<T> consumer, Operation<Void> original,
            @Local(argsOnly = true) Entity entity,
            @Local(argsOnly = true) Predicate<Entity> predicate,
            @Local List<Entity> list
    ) {
        original.call(instance, aabb, (Consumer<T>) target -> {
            consumer.accept(target);
            if (target != entity && target instanceof MultipartEntity multipartEntity) {
                for (var subPart : multipartEntity.getSubParts()) {
                    if (!predicate.test(subPart)) continue;
                    list.add(subPart);
                }
            }
        });
    }
}
