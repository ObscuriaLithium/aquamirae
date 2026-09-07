package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.common.entity.projectile.MazeRose;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MazeRoseItem extends Item {

    public MazeRoseItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            var projectile = new MazeRose(AquamiraeEntityTypes.MAZE_ROSE.get(), level);
            projectile.setOwner(player);
            projectile.setPos(player.position().lerp(player.getEyePosition(), 0.5f));
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 1.5f, 0.5f);
            level.addFreshEntity(projectile);
            player.getCooldowns().addCooldown(this, 25 / stack.getCount());
        }
        level.playSound(
                null, player.blockPosition(),
                SoundEvents.TRIDENT_THROW.value(), player.getSoundSource(),
                1.0f, (float) level.getRandom().triangle(1f, 0.2f));

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
