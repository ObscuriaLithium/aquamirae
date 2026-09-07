package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LuminescentBubbleItem extends Item {

    public LuminescentBubbleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (level.isWaterAt(player.blockPosition().above())) {
            stack.shrink(1);
            var state = AquamiraeBlocks.LUMINESCENT_BUBBLE.instantiate().setValue(BlockStateProperties.WATERLOGGED, true);
            level.setBlock(player.blockPosition().above(), state, Block.UPDATE_ALL);
            if (level instanceof ServerLevel serverLevel)
                serverLevel.playSound(null,
                        player.blockPosition().above(),
                        SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,
                        SoundSource.BLOCKS, 2, 1);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }
}
