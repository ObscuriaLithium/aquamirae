package dev.obscuria.aquamirae.integration;

import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.registry.tags.AquamiraeBiomeTags;
import dev.obscuria.fragmentum.config.ConfigValue;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public final class TagPostProcessors {

    private static final Map<TagKey<?>, List<Postprocessor>> POSTPROCESSORS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, String> IRREGULAR_DIRECTORIES;

    public static void register(TagKey<?> tagKey, Postprocessor postprocessor) {
        POSTPROCESSORS.computeIfAbsent(tagKey, key -> new CopyOnWriteArrayList<>()).add(postprocessor);
    }

    public static void register(TagKey<?> tagKey, ConfigValue<List<? extends String>> configValue) {
        register(tagKey, () -> configValue.get().stream().map(Entry::parse).collect(Collectors.toList()));
    }

    public static void registerStatic(TagKey<?> tagKey, Collection<Entry> entries) {
        register(tagKey, () -> entries);
    }

    public static void registerStaticRaw(TagKey<?> tagKey, Collection<String> rawStrings) {
        register(tagKey, () -> parseAll(rawStrings));
    }

    public static List<Entry> parseAll(Collection<String> rawStrings) {
        return rawStrings.stream().map(Entry::parse).collect(Collectors.toList());
    }

    public static Map<TagKey<?>, List<Postprocessor>> view() {
        return POSTPROCESSORS;
    }

    public static String directoryFor(ResourceKey<? extends Registry<?>> registryKey) {
        String irregular = IRREGULAR_DIRECTORIES.get(registryKey.location());
        if (irregular != null) {
            return irregular;
        }
        return "tags/" + registryKey.location().getPath();
    }

    static {
        IRREGULAR_DIRECTORIES = Map.of(
                ResourceLocation.withDefaultNamespace("block"), "tags/blocks",
                ResourceLocation.withDefaultNamespace("item"), "tags/items",
                ResourceLocation.withDefaultNamespace("entity_type"), "tags/entity_types",
                ResourceLocation.withDefaultNamespace("fluid"), "tags/fluids",
                ResourceLocation.withDefaultNamespace("game_event"), "tags/game_events");

        register(AquamiraeBiomeTags.ICE_MAZE, CommonConfig.ICE_MAZE_BIOMES);
        register(AquamiraeBiomeTags.HAS_BLIZZARD, () -> List.of(Entry.tag(AquamiraeBiomeTags.ICE_MAZE.location())));
    }

    @FunctionalInterface
    public interface Postprocessor {

        Collection<Entry> collect();
    }

    public sealed interface Entry {

        record Element(ResourceLocation id) implements Entry {}

        record Tag(ResourceLocation id) implements Entry {}

        static Entry element(ResourceLocation id) {
            return new Element(id);
        }

        static Entry tag(ResourceLocation id) {
            return new Tag(id);
        }

        static Entry parse(String raw) {
            return raw.startsWith("#")
                    ? tag(ResourceLocation.parse(raw.substring(1)))
                    : element(ResourceLocation.parse(raw));
        }
    }
}