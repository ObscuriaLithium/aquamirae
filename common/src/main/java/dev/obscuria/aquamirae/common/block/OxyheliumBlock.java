package dev.obscuria.aquamirae.common.block;

import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Locale;
import java.util.Objects;

public class OxyheliumBlock extends Block implements SimpleWaterloggedBlock {

    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public OxyheliumBlock() {
        super(Properties.of()
                .mapColor(MapColor.WATER)
                .sound(SoundType.CORAL_BLOCK)
                .strength(0.8f, 1f)
                .randomTicks()
                .requiresCorrectToolForDrops()
                .noCollission()
                .speedFactor(0.8f)
                .jumpFactor(0.8f)
                .hasPostProcess((bs, br, bp) -> isGlowing(bs))
                .emissiveRendering((bs, br, bp) -> isGlowing(bs))
                .lightLevel(s -> isGlowing(s) ? 12 : 0));
        registerDefaultState(stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(TYPE, Type.STEM));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
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
        return type == PathComputationType.AIR || type == PathComputationType.WATER;
    }

    @Override
    public void neighborChanged(
            BlockState state, Level level, BlockPos pos,
            Block neighborBlock, BlockPos fromPos, boolean moving
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, fromPos, moving);
        var aboveState = level.getBlockState(pos.above());
        if (aboveState.canOcclude()) return;
        if (aboveState.hasProperty(TYPE) && aboveState.getValue(TYPE) == Type.STEM) return;
        level.destroyBlock(pos, true);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(TYPE) != Type.EMPTY_BUD) return;
        if (Math.random() < 0.1) {
            if (!world.isClientSide())
                world.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 5, 1);
            if (Math.random() <= 0.98) {
                world.sendParticles(ParticleTypes.BUBBLE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.2);
                world.setBlock(pos, AquamiraeBlocks.OXYHELIUM.get().defaultBlockState().setValue(TYPE, Type.BUD).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 3);
            } else {
                world.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.5, 0.5, 0.5, 0.2);
                world.setBlock(pos, AquamiraeBlocks.OXYHELIUM.get().defaultBlockState().setValue(TYPE, Type.RARE_BUD).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 3);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(TYPE) != Type.BUD && state.getValue(TYPE) != Type.RARE_BUD) return;
        for (int l = 0; l < 2; ++l) {
            var x = pos.getX() + 0.5 + (random.nextFloat() - 0.5) * 2D;
            var y = pos.getY() + 1.2 + (random.nextFloat() - 0.5) * 2D;
            var z = pos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 2D;
            level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player entity) {
        if (state.getValue(TYPE) == Type.BUD)
            useBud(state, level, pos, entity);
        else if (state.getValue(TYPE) == Type.RARE_BUD)
            useRareBud(state, level, pos, entity);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living && state.getValue(TYPE) == Type.BUD)
            useBud(state, level, pos, living);
        else if (entity instanceof LivingEntity living && state.getValue(TYPE) == Type.RARE_BUD)
            useRareBud(state, level, pos, living);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(TYPE) == Type.BUD)
            return useBud(state, level, pos, player);
        else if (state.getValue(TYPE) == Type.RARE_BUD)
            return useRareBud(state, level, pos, player);
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED);
    }

    private InteractionResult useBud(BlockState state, Level level, BlockPos pos, LivingEntity entity) {
        if (!level.isClientSide())
            level.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 5, 1);
        level.setBlock(pos, AquamiraeBlocks.OXYHELIUM.instantiate().setValue(TYPE, Type.EMPTY_BUD).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 3);
        if (level instanceof ServerLevel server) server.sendParticles(ParticleTypes.BUBBLE,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12, 0.5, 0.5, 0.5, 0.01);
        entity.setAirSupply(0);
        entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING,
                (entity.hasEffect(MobEffects.WATER_BREATHING) ? Objects.requireNonNull(entity.getEffect(MobEffects.WATER_BREATHING)).getDuration() : 0) + 200, 0));

        if (!level.isClientSide() && entity.hasEffect(MobEffects.WATER_BREATHING) && Objects.requireNonNull(entity.getEffect(MobEffects.WATER_BREATHING)).getDuration() > 1200) {
            level.playSound(null, pos, SoundEvents.AMBIENT_CAVE.value(), SoundSource.BLOCKS, 1, 1);
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult useRareBud(BlockState state, Level world, BlockPos pos, LivingEntity entity) {
        if (!world.isClientSide()) {
            world.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.6F, 1);
            world.addFreshEntity(new ExperienceOrb(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8));
        }
        world.setBlock(pos, AquamiraeBlocks.OXYHELIUM.get().defaultBlockState().setValue(TYPE, Type.EMPTY_BUD).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 3);
        return InteractionResult.SUCCESS;
    }

    private static boolean isGlowing(BlockState state) {
        return state.getValue(TYPE) == Type.BUD || state.getValue(TYPE) == Type.RARE_BUD;
    }

    public enum Type implements StringRepresentable {
        STEM,
        EMPTY_BUD,
        BUD,
        RARE_BUD;

        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}