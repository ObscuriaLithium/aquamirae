package dev.obscuria.aquamirae.registry.tags;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface AquamiraeBlockTags {

    TagKey<Block> SHIPBREAKER_BREAKABLES = create("shipbreaker_breakables");
    TagKey<Block> MAZE_MOTHER_BREAKABLES = create("maze_mother_breakables");
    TagKey<Block> SCUTTLER_SPAWNABLE_ON = create("scuttler_spawnable_on");
    TagKey<Block> EEL_DIGGABLE_ON = create("eel_diggable_on");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Aquamirae.id(name));
    }
}