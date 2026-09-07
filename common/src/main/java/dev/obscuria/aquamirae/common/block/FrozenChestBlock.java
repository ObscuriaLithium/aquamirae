package dev.obscuria.aquamirae.common.block;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.registry.AquamiraeParticleTypes;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class FrozenChestBlock extends Block implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = box(0.9, 0, 0.9, 15.1, 14.1, 15.1);

    public FrozenChestBlock() {
        super(Properties.of()
                .strength(-1, 3600000)
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .noOcclusion());
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult
    ) {
        if (stack.is(AquamiraeItems.FROZEN_KEY.get())) {
            stack.shrink(1);
            var chest = Blocks.CHEST.defaultBlockState().setValue(FACING, state.getValue(FACING));
            level.setBlock(pos, chest, 3);
            if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity container) {
                container.setLootTable(
                        ResourceKey.create(Registries.LOOT_TABLE, Aquamirae.id("chests/frozen_chest")),
                        player.getRandom().nextInt());
            }
            level.playSound(player, pos, AquamiraeSounds.BLOCK_FROZEN_CHEST_UNLOCK, SoundSource.BLOCKS, 1f, 1f);
            for (int i = 0; i < 12; ++i) {
                var d0 = pos.getX() - 0.1D + 1.2D * player.getRandom().nextDouble();
                var d1 = pos.getY() - 0.1D + 1.2D * player.getRandom().nextDouble();
                var d2 = pos.getZ() - 0.1D + 1.2D * player.getRandom().nextDouble();
                level.addParticle(ParticleTypes.CLOUD, d0, d1, d2, 0, 0.01, 0);
            }
            for (int i = 0; i < 12; ++i) {
                var d0 = pos.getX() - 0.2D + 1.4D * player.getRandom().nextDouble();
                var d1 = pos.getY() - 0.2D + 1.4D * player.getRandom().nextDouble();
                var d2 = pos.getZ() - 0.2D + 1.4D * player.getRandom().nextDouble();
                var type = Objects.requireNonNull(AquamiraeParticleTypes.SHINE.simple());
                level.addParticle(type, d0, d1, d2, 0, 0.05, 0);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, flag);
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }
}
