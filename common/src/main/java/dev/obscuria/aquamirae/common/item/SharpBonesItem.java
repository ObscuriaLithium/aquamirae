package dev.obscuria.aquamirae.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SharpBonesItem extends SeaFoodItem {

	public SharpBonesItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 24;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		entity.hurt(entity.damageSources().starve(), 1);
		return super.finishUsingItem(stack, level, entity);
	}
}
