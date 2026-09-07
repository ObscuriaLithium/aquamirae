package dev.obscuria.aquamirae.common.block;

import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class WisteriaNiveisBlock extends DoublePlantBlock {

    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");

    public WisteriaNiveisBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NATURAL, true));
    }

    public boolean canBePlacedOn(Level level, BlockPos pos) {
        return mayPlaceOn(level.getBlockState(pos), level, pos)
                && level.isEmptyBlock(pos.above(1))
                && level.isEmptyBlock(pos.above(2));
    }

    public boolean canBePlacedOn(WorldGenLevel level, BlockPos pos) {
        return mayPlaceOn(level.getBlockState(pos), level, pos)
                && level.isEmptyBlock(pos.above(1))
                && level.isEmptyBlock(pos.above(2));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return AquamiraeItems.WISTERIA_NIVEIS.instantiate();
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        if (state.getValue(HALF) != DoubleBlockHalf.LOWER || !world.isEmptyBlock(pos.above())) return;
        world.setBlock(pos.above(), AquamiraeBlocks.WISTERIA_NIVEIS.instantiate()
                .setValue(HALF, DoubleBlockHalf.UPPER)
                .setValue(NATURAL, state.getValue(NATURAL)), 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(NATURAL));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.UP)
                && (state.is(BlockTags.SNOW) || super.mayPlaceOn(state, level, pos));
    }
}