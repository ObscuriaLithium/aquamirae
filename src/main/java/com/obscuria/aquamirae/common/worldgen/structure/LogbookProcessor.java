package com.obscuria.aquamirae.common.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.obscuria.aquamirae.common.items.Logbook;
import com.obscuria.aquamirae.registry.AquamiraeStructureProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public final class LogbookProcessor extends StructureProcessor {

    public static final Codec<LogbookProcessor> CODEC;

    public final Logbook logbookType;

    public LogbookProcessor(Logbook logbook) {
        this.logbookType = logbook;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(
            LevelReader world,
            BlockPos templateOrigin,
            BlockPos worldOrigin,
            StructureTemplate.StructureBlockInfo rawBlockInfo,
            StructureTemplate.StructureBlockInfo blockInfo,
            StructurePlaceSettings placementSettings,
            @Nullable StructureTemplate template
    ) {
        if (!blockInfo.state().is(Blocks.LECTERN)) return blockInfo;
        var tag = new CompoundTag();
        tag.put("Book", logbookType.get().save(new CompoundTag()));
        return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), blockInfo.state().setValue(LecternBlock.HAS_BOOK, true), tag);
    }

    @Override
    protected StructureProcessorType<LogbookProcessor> getType() {
        return AquamiraeStructureProcessors.LOGBOOK.get();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Logbook.CODEC.fieldOf("logbook_type").forGetter(it -> it.logbookType)
        ).apply(codec, LogbookProcessor::new));
    }
}
