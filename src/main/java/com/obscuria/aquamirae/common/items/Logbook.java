package com.obscuria.aquamirae.common.items;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Locale;
import java.util.function.Supplier;

public enum Logbook implements StringRepresentable, Supplier<ItemStack> {
    PILLAGER_SHIP(new byte[]{0, 0}),
    PILLAGER_OUTPOST(new byte[]{0, 0, 1, 0, 1}),
    PILLAGER_SHELTER(new byte[]{0, 0, 0, 0});

    public static final Codec<Logbook> CODEC = StringRepresentable.fromEnum(Logbook::values);
    public static final String AUTHOR_TAG = "aquamirae:author";

    private final byte[] pageFlags;

    Logbook(byte[] pageFlags) {
        this.pageFlags = pageFlags;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public ItemStack get() {
        return build(getSerializedName() + "_logbook", Items.WRITTEN_BOOK.getDefaultInstance(), new ListTag());
    }

    private ItemStack build(String key, ItemStack stack, ListTag pages) {
        stack.setHoverName(Component.translatable("book.aquamirae.%s.title".formatted(key)));
        stack.getOrCreateTag().putString("title", "Logbook");
        stack.getOrCreateTag().putString("author", "Unknown");
        stack.getOrCreateTag().putString(AUTHOR_TAG, "book.aquamirae.%s.author".formatted(key));
        stack.getOrCreateTag().putInt("generation", 3);
        stack.getOrCreateTag().putBoolean("resolved", true);
        for (var i = 0; i < pageFlags.length; i++) {
            var page = i + 1;
            if (pageFlags[i] == 0) {
                pages.add(StringTag.valueOf(
                        "{\"translate\":\"book.aquamirae.%s.page_%s\"}".formatted(key, page)));
            } else {
                pages.add(StringTag.valueOf("{\"extra\":[" +
                        "{\"translate\":\"book.aquamirae.%s.page_%s.start\"},".formatted(key, page) +
                        "{\"color\":\"dark_green\",\"translate\":\"book.aquamirae.%s.page_%s.accent\"},".formatted(key, page) +
                        "{\"translate\":\"book.aquamirae.%s.page_%s.end\"}],\"text\":\"\"}".formatted(key, page)));
            }
        }
        stack.getOrCreateTag().put("pages", pages);
        return stack;
    }
}
