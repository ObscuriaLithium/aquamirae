package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.block.*;
import dev.obscuria.fragmentum.content.registry.DeferredBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public final class AquamiraeBlocks {

    public static final DeferredBlock<WisteriaNiveisBlock> WISTERIA_NIVEIS = register("wisteria_niveis",
            () -> new WisteriaNiveisBlock(BlockBehaviour.Properties.of()
                    .strength(1f, 10f)
                    .mapColor(MapColor.PLANT)
                    .pushReaction(PushReaction.DESTROY)
                    .offsetType(BlockBehaviour.OffsetType.XYZ)
                    .sound(SoundType.WEEPING_VINES)
                    .noCollission()));
    public static final DeferredBlock<StashBlock> PIRATE_POUCH = register("pirate_pouch",
            () -> new StashBlock(BlockBehaviour.Properties.of()
                    .instabreak()
                    .mapColor(MapColor.COLOR_BLACK)
                    .pushReaction(PushReaction.DESTROY)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .sound(SoundType.WOOL)
                    .noCollission()));
    public static final DeferredBlock<StashBlock> TREASURE_POUCH = register("treasure_pouch",
            () -> new StashBlock(BlockBehaviour.Properties.of()
                    .instabreak()
                    .mapColor(MapColor.COLOR_BLACK)
                    .pushReaction(PushReaction.DESTROY)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .sound(SoundType.WOOL)
                    .noCollission()));
    public static final DeferredBlock<OxyheliumBlock> OXYHELIUM = register("oxyhelium", OxyheliumBlock::new);
    public static final DeferredBlock<LuminescentBubbleBlock> LUMINESCENT_BUBBLE = register("luminescent_bubble", LuminescentBubbleBlock::new);
    public static final DeferredBlock<LuminescentLampBlock> LUMINESCENT_LAMP = register("luminescent_lamp", LuminescentLampBlock::new);
    public static final DeferredBlock<FrozenChestBlock> FROZEN_CHEST = register("frozen_chest", FrozenChestBlock::new);
    public static final DeferredBlock<GoldenMothJarBlock> GOLDEN_MOTH_JAR = register("golden_moth_jar", GoldenMothJarBlock::new);
    public static final DeferredBlock<CollectiblePaintingBlock> PAINTING_ANGLERFISH = register("painting_anglerfish", CollectiblePaintingBlock::new);
    public static final DeferredBlock<CollectiblePaintingBlock> PAINTING_OXYHELIUM = register("painting_oxyhelium", CollectiblePaintingBlock::new);
    public static final DeferredBlock<CollectiblePaintingBlock> PAINTING_TORTURED_SOUL = register("painting_tortured_soul", CollectiblePaintingBlock::new);
    public static final DeferredBlock<CollectiblePaintingBlock> PAINTING_AURORA = register("painting_aurora", CollectiblePaintingBlock::new);

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> supplier) {
        return AquamiraeRegistries.REGISTRAR.registerBlock(Aquamirae.id(name), supplier);
    }

    static void init() {}
}
