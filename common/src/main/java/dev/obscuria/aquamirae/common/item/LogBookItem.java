package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.client.AquamiraeClient;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;

public class LogBookItem extends Item {

    public LogBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var state = level.getBlockState(pos);
        return state.is(Blocks.LECTERN)
                && LecternBlock.tryPlaceBook(context.getPlayer(), level, pos, state, context.getItemInHand())
                ? InteractionResult.sidedSuccess(level.isClientSide)
                : InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (level.isClientSide) {
            AquamiraeClient.openLogbookScreen(stack);
            return InteractionResultHolder.success(stack);
        } else return InteractionResultHolder.pass(stack);
    }
}
