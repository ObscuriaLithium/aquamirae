package dev.obscuria.aquamirae.common.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class JawbreakerItem extends SwordItem {

    public JawbreakerItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        entity.eat(level, stack.copy());
        stack.hurtAndBreak(10, entity, LivingEntity.getSlotForHand(entity.getUsedItemHand()));
        level.playSound(
                null, entity.getX(), entity.getY(), entity.getZ(),
                entity.getEatingSound(stack), SoundSource.NEUTRAL,
                1.0F, (float) entity.getRandom().triangle(1.0, 0.2));
        return stack;
    }
}
