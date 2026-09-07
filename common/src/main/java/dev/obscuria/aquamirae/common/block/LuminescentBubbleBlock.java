package dev.obscuria.aquamirae.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LuminescentBubbleBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = box(3.3, 1, 3.3, 12.7, 15, 12.7);

    public LuminescentBubbleBlock() {
        super(Properties.of()
                .mapColor(MapColor.WATER)
                .sound(SoundType.CORAL_BLOCK)
                .strength(0.1f, 0.5f)
                .lightLevel(s -> 15)
                .offsetType(OffsetType.XYZ)
                .noCollission()
                .noOcclusion()
                .hasPostProcess((bs, br, bp) -> true)
                .emissiveRendering((bs, br, bp) -> true)
                .dynamicShape());
        this.registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.isWaterAt(pos);
    }

    @Override
    public void onPlace(BlockState blockstate, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(blockstate, level, pos, oldState, moving);
        level.scheduleTick(pos, this, 20);
    }

    @Override
    public void tick(BlockState blockstate, ServerLevel level, BlockPos pos, RandomSource random) {
        level.scheduleTick(pos, this, 20);
        for (var player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8), e -> true)) {
            if (!player.isInWater()) continue;
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 80, 1, false, true));
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var offset = state.getOffset(level, pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var flag = context.getLevel().isWaterAt(context.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
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
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return type == PathComputationType.WATER;
    }

    @Override
    public void animateTick(BlockState blockstate, Level level, BlockPos pos, RandomSource random) {
        for (int l = 0; l < 3; ++l) {
            var x0 = pos.getX() + random.nextFloat();
            var y0 = pos.getY() + random.nextFloat();
            var z0 = pos.getZ() + random.nextFloat();
            var dx = (random.nextFloat() - 0.5D) * 0.16D;
            var dy = (random.nextFloat() - 0.5D) * 0.16D;
            var dz = (random.nextFloat() - 0.5D) * 0.16D;
            level.addParticle(ParticleTypes.BUBBLE, x0, y0, z0, dx, dy, dz);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos,
            Player player, BlockHitResult hitResult
    ) {
        if (!player.getAbilities().instabuild && level instanceof ServerLevel serverLevel)
            for (var drop : getDrops(state, serverLevel, pos, null))
                if (!player.addItem(drop))
                    player.drop(drop, false);
        level.destroyBlock(pos, false);
        return InteractionResult.SUCCESS;
    }
}
