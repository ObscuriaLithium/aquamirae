package dev.obscuria.aquamirae.common.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class CollectiblePaintingBlock extends Block implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Map<Direction, VoxelShape> SHAPES;

    public CollectiblePaintingBlock() {
        super(Properties.of()
                .strength(1f)
                .requiresCorrectToolForDrops()
                .mapColor(MapColor.WOOD)
                .sound(new SoundType(1.0f, 1.0f,
                        SoundEvents.PAINTING_BREAK,
                        SoundEvents.WOOL_STEP,
                        SoundEvents.PAINTING_PLACE,
                        SoundEvents.WOOD_HIT,
                        SoundEvents.WOOD_FALL))
                .noCollission()
                .noOcclusion());
        registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

//    @Override
//    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flag) {
//        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
//    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        if (context.getClickedFace().getAxis() == Direction.Axis.Y)
            return this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, flag);
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, flag);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(
            BlockState state, Direction facing, BlockState facingState,
            LevelAccessor world, BlockPos currentPos, BlockPos facingPos
    ) {
        if (state.getValue(WATERLOGGED)) world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    static {
        SHAPES = Util.make(ImmutableMap.<Direction, VoxelShape>builder(), builder -> {
            builder.put(Direction.NORTH, box(-8, 0, 15, 24, 16, 16));
            builder.put(Direction.SOUTH, box(-8, 0, 0, 24, 16, 1));
            builder.put(Direction.EAST, box(0, 0, -8, 1, 16, 24));
            builder.put(Direction.WEST, box(15, 0, -8, 16, 16, 24));
        }).build();
    }
}