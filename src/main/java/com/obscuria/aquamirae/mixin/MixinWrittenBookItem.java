package com.obscuria.aquamirae.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.obscuria.aquamirae.common.items.Logbook;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.WrittenBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WrittenBookItem.class)
public abstract class MixinWrittenBookItem {

    @ModifyArg(method = "appendHoverText", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0), index = 1)
    private Object[] injectTranslatableAuthor(Object[] placeholders, @Local CompoundTag tag) {
        if (!tag.contains(Logbook.AUTHOR_TAG, Tag.TAG_STRING)) return placeholders;
        return new Object[]{Component.translatable(tag.getString(Logbook.AUTHOR_TAG))};
    }
}
