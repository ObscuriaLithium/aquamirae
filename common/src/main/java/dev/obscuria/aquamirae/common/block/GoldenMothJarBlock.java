package dev.obscuria.aquamirae.common.block;

import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class GoldenMothJarBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE;

    public GoldenMothJarBlock() {
        super(Properties.of()
                .strength(1f, 10f)
                .mapColor(MapColor.GOLD)
                .sound(SoundType.GLASS)
                .lightLevel(s -> 8)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .noLootTable());
        registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, waterlogged);
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
            LevelAccessor level, BlockPos currentPos, BlockPos facingPos
    ) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void setPlacedBy(
            Level level, BlockPos pos, BlockState state,
            @Nullable LivingEntity entity, ItemStack stack
    ) {
        super.setPlacedBy(level, pos, state, entity, stack);
        if (level instanceof ServerLevel serverLevel) {
            @Nullable var goldenMoth = AquamiraeEntityTypes.GOLDEN_MOTH.get().create(serverLevel);
            if (goldenMoth == null) return;
            goldenMoth.moveTo(pos.getCenter().add(0, -0.2, 0));
            goldenMoth.setPersistenceRequired();
            var difficulty = level.getCurrentDifficultyAt(goldenMoth.blockPosition());
            goldenMoth.finalizeSpawn(serverLevel, difficulty, MobSpawnType.MOB_SUMMONED, null);
            level.addFreshEntity(goldenMoth);
        }
    }

    static {
        SHAPE = Shapes.join(
                Shapes.or(
                        box(3, 0, 3, 13, 12, 13),
                        box(4, 12, 4, 12, 14, 12)),
                box(3.1, 0.1, 3.1, 12.9, 10.5, 12.9),
                BooleanOp.ONLY_FIRST);
    }
}
