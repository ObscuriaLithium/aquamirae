package com.obscuria.aquamirae.common.lore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Central mapping of logbook ids to translation keys and page component JSON.
 */
public enum LogbookDefinition {
    PILLAGERS_SHIP("pillagers_ship_logbook", "Pillagers Ship Logbook", "Entry #12", List.of(
            translatedPage("book.aquamirae.pillagers_ship_logbook.page_1"),
            translatedPage("book.aquamirae.pillagers_ship_logbook.page_2")
    )),
    PILLAGERS_OUTPOST("pillagers_outpost_logbook", "Pillagers Outpost Logbook", "Entry #34", List.of(
            translatedPage("book.aquamirae.pillagers_outpost_logbook.page_1"),
            translatedPage("book.aquamirae.pillagers_outpost_logbook.page_2"),
            "{\"extra\":[{\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_3.start\"},{\"color\":\"dark_green\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"extra\":[{\"color\":\"green\",\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_3.tooltip_title\"},{\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_3.tooltip_body\"}],\"text\":\"\"}},\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_3.highlight\"},{\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_3.end\"}],\"text\":\"\"}",
            translatedPage("book.aquamirae.pillagers_outpost_logbook.page_4"),
            "{\"extra\":[{\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_5.start\"},{\"color\":\"dark_green\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"extra\":[{\"color\":\"green\",\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_5.tooltip_title\"},{\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_5.tooltip_body\"}],\"text\":\"\"}},\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_5.highlight\"},{\"translate\":\"book.aquamirae.pillagers_outpost_logbook.page_5.end\"}],\"text\":\"\"}"
    )),
    PILLAGERS_SHELTER("pillagers_shelter_logbook", "Pillagers Shelter Logbook", "Entry #41", List.of(
            translatedPage("book.aquamirae.pillagers_shelter_logbook.page_1"),
            translatedPage("book.aquamirae.pillagers_shelter_logbook.page_2"),
            translatedPage("book.aquamirae.pillagers_shelter_logbook.page_3"),
            translatedPage("book.aquamirae.pillagers_shelter_logbook.page_4")
    ));

    private final String id;
    private final String title;
    private final String legacyFirstPageMarker;
    private final List<String> pageComponents;

    LogbookDefinition(String id, String title, String legacyFirstPageMarker, List<String> pageComponents) {
        this.id = id;
        this.title = title;
        this.legacyFirstPageMarker = legacyFirstPageMarker;
        this.pageComponents = pageComponents;
    }

    public void applyTo(@NotNull ItemStack bookStack) {
        applyToBookTag(bookStack.getOrCreateTag());
    }

    public void applyToBookTag(@NotNull CompoundTag bookTag) {
        final ListTag pages = new ListTag();
        for (final String page : pageComponents) pages.add(StringTag.valueOf(page));

        bookTag.putString("title", title);
        bookTag.putString("author", "Unknown");
        bookTag.putInt("generation", 3);
        bookTag.putBoolean("resolved", true);
        bookTag.putString("aquamirae:logbook_id", id);
        bookTag.put("pages", pages);
    }

    public boolean matches(@NotNull CompoundTag bookTag) {
        if (id.equals(bookTag.getString("aquamirae:logbook_id"))) return true;
        if (title.equals(bookTag.getString("title"))) return true;
        if (!bookTag.contains("pages", Tag.TAG_LIST)) return false;
        final ListTag pages = bookTag.getList("pages", Tag.TAG_STRING);
        if (pages.isEmpty()) return false;
        final String page1 = pages.getString(0);
        return page1.contains(legacyFirstPageMarker) || page1.contains("book.aquamirae." + id + ".page_1");
    }

    public static boolean patchStructureBooks(@NotNull CompoundTag structureTag) {
        if (!structureTag.contains("blocks", Tag.TAG_LIST)) return false;
        boolean patched = false;
        for (Tag blockTag : structureTag.getList("blocks", Tag.TAG_COMPOUND)) {
            final CompoundTag block = (CompoundTag) blockTag;
            if (!block.contains("nbt", Tag.TAG_COMPOUND)) continue;
            final CompoundTag blockEntity = block.getCompound("nbt");
            final String id = blockEntity.getString("id");
            if ("minecraft:lectern".equals(id) && blockEntity.contains("Book", Tag.TAG_COMPOUND)) {
                patched |= patchItemStack(blockEntity.getCompound("Book"));
                continue;
            }
            if (("minecraft:chest".equals(id) || "minecraft:barrel".equals(id)) && blockEntity.contains("Items", Tag.TAG_LIST)) {
                for (Tag itemTag : blockEntity.getList("Items", Tag.TAG_COMPOUND)) {
                    patched |= patchItemStack((CompoundTag) itemTag);
                }
            }
        }
        return patched;
    }

    private static boolean patchItemStack(@NotNull CompoundTag itemStackTag) {
        if (!"minecraft:written_book".equals(itemStackTag.getString("id"))) return false;
        if (!itemStackTag.contains("tag", Tag.TAG_COMPOUND)) return false;
        final CompoundTag bookTag = itemStackTag.getCompound("tag");
        final LogbookDefinition definition = find(bookTag);
        if (definition == null) return false;
        definition.applyToBookTag(bookTag);
        return true;
    }

    private static @Nullable LogbookDefinition find(@NotNull CompoundTag bookTag) {
        for (final LogbookDefinition definition : values()) {
            if (definition.matches(bookTag)) return definition;
        }
        return null;
    }

    private static @NotNull String translatedPage(String key) {
        return "{\"translate\":\"" + key + "\"}";
    }
}