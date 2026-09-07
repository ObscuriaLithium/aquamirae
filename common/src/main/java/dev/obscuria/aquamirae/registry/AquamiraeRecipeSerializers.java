package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.recipe.RuneSmithingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class AquamiraeRecipeSerializers {

    public static final RecipeSerializer<RuneSmithingRecipe> RUNE_SMITHING = register("rune_smithing", new RuneSmithingRecipe.Serializer());

    private static <T extends Recipe<?>> RecipeSerializer<T> register(String name, RecipeSerializer<T> value) {
        AquamiraeRegistries.REGISTRAR.register(Registries.RECIPE_SERIALIZER, Aquamirae.id(name), () -> value);
        return value;
    }

    static void init() {}
}
