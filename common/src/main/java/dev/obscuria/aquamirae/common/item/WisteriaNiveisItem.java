
package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import dev.obscuria.aquamirae.common.block.WisteriaNiveisBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class WisteriaNiveisItem extends Item {

    public WisteriaNiveisItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final BlockPos pos = context.getClickedPos();
        if (context.getClickedFace() != Direction.UP)
            return InteractionResult.FAIL;
        if (!AquamiraeBlocks.WISTERIA_NIVEIS.get().canBePlacedOn(context.getLevel(), pos))
            return InteractionResult.FAIL;
        context.getLevel().playSound(context.getPlayer(), context.getClickedPos(), SoundEvents.WEEPING_VINES_PLACE, SoundSource.BLOCKS, 1F, 1F);
        context.getLevel().setBlock(pos.above(), AquamiraeBlocks.WISTERIA_NIVEIS.instantiate()
                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
                .setValue(WisteriaNiveisBlock.NATURAL, false), 3);
        context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
