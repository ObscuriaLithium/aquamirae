package dev.obscuria.aquamirae.integration.hook;

import dev.obscuria.aquamirae.integration.TagPostProcessors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class HookTagLoader {

    public static void build(String directory, Map<ResourceLocation, List<TagLoader.EntryWithSource>> builders) {
        for (var registration : TagPostProcessors.view().entrySet()) {
            var tagKey = registration.getKey();
            var expectedDirectory = TagPostProcessors.directoryFor(tagKey.registry());
            if (!expectedDirectory.equals(directory)) continue;

            var entries = builders.computeIfAbsent(tagKey.location(), id -> new ArrayList<>());

            for (var postprocessor : registration.getValue()) {
                try {
                    var additions = postprocessor.collect();
                    for (var addition : additions) {
                        if (addition instanceof TagPostProcessors.Entry.Element(ResourceLocation id)) {
                            entries.add(new TagLoader.EntryWithSource(
                                    TagEntry.optionalElement(id),
                                    "aquamirae:config"));
                        } else if (addition instanceof TagPostProcessors.Entry.Tag(ResourceLocation id)) {
                            entries.add(new TagLoader.EntryWithSource(
                                    TagEntry.optionalTag(id),
                                    "aquamirae:config"));
                        }
                    }
                } catch (Exception ignored) {}
            }
        }
    }
}
