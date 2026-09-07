package dev.obscuria.aquamirae.common.region.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.registry.AquamiraeCreativeTabs;
import dev.obscuria.aquamirae.registry.AquamiraeStructureProcessors;
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

public class LogbookProcessor extends StructureProcessor {

    public static final MapCodec<LogbookProcessor> CODEC;

    public final String logbookType;

    public LogbookProcessor(String logbookType) {
        this.logbookType = logbookType;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(
            LevelReader level,
            BlockPos offset,
            BlockPos pos,
            StructureTemplate.StructureBlockInfo blockInfo,
            StructureTemplate.StructureBlockInfo relativeBlockInfo,
            StructurePlaceSettings settings
    ) {
        if (!relativeBlockInfo.state().is(Blocks.LECTERN)) return relativeBlockInfo;
        var compoundTag = new CompoundTag();
        compoundTag.put("Book", AquamiraeCreativeTabs.logBook(logbookType).save(level.registryAccess()));
        return new StructureTemplate.StructureBlockInfo(
                relativeBlockInfo.pos(),
                relativeBlockInfo.state().setValue(LecternBlock.HAS_BOOK, true),
                compoundTag);
    }

    @Override
    protected StructureProcessorType<LogbookProcessor> getType() {
        return AquamiraeStructureProcessors.LOGBOOK;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.STRING.fieldOf("logbook_type").forGetter(it -> it.logbookType)
        ).apply(codec, LogbookProcessor::new));
    }
}