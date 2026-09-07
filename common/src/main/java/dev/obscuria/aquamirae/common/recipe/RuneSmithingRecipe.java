package dev.obscuria.aquamirae.common.recipe;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.common.component.RuneDamage;
import dev.obscuria.aquamirae.common.component.RuneProtection;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.registry.AquamiraeRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

public class RuneSmithingRecipe implements SmithingRecipe {

    public static final RuneSmithingRecipe SHARED = new RuneSmithingRecipe();

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return stack.isEmpty();
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return (stack.getItem() instanceof TieredItem || stack.getItem() instanceof ArmorItem)
                && !stack.has(AquamiraeDataComponents.RUNE_OF_THE_STORM_DAMAGE)
                && !stack.has(AquamiraeDataComponents.RUNE_OF_THE_STORM_PROTECTION);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return stack.is(AquamiraeItems.RUNE_OF_THE_STORM.get());
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return isTemplateIngredient(input.template())
                && isBaseIngredient(input.base())
                && isAdditionIngredient(input.addition());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider provider) {
        var result = input.base().copy();
        if (result.getItem() instanceof ArmorItem) {
            result.set(AquamiraeDataComponents.RUNE_OF_THE_STORM_PROTECTION, RuneProtection.SHARED);
        } else {
            result.set(AquamiraeDataComponents.RUNE_OF_THE_STORM_DAMAGE, RuneDamage.SHARED);
        }
        return result;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<RuneSmithingRecipe> getSerializer() {
        return AquamiraeRecipeSerializers.RUNE_SMITHING;
    }

    public static class Serializer implements RecipeSerializer<RuneSmithingRecipe> {

        public static final MapCodec<RuneSmithingRecipe> CODEC = MapCodec.unit(SHARED);
        public static final StreamCodec<RegistryFriendlyByteBuf, RuneSmithingRecipe> STREAM_CODEC = StreamCodec.unit(SHARED);

        @Override
        public MapCodec<RuneSmithingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RuneSmithingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
