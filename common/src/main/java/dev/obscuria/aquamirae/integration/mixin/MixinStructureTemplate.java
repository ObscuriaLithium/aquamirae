package dev.obscuria.aquamirae.integration.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureTemplate.class)
public abstract class MixinStructureTemplate {

    @Unique private static final String TAG_IGNORE_LIQUIDS = "IgnoreLiquids";
    @Unique private boolean aquamirae$ignoreLiquids = false;

    @Inject(method = "load", at = @At("TAIL"))
    private void load$onTail(HolderGetter<Block> level, CompoundTag tag, CallbackInfo info) {
        if (!tag.getBoolean(TAG_IGNORE_LIQUIDS)) return;
        this.aquamirae$ignoreLiquids = true;
    }

    @ModifyExpressionValue(method = "placeInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;shouldApplyWaterlogging()Z"))
    private boolean placeInWorld$modifyShouldApplyWaterlogging(boolean original) {
        return original && !aquamirae$ignoreLiquids;
    }

}
