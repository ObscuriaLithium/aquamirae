package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.fragmentum.content.registry.Deferred;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;

public final class AquamiraeArmorMaterials {

    public static final Deferred<ArmorMaterial, ArmorMaterial> TERRIBLE = register("terrible", ArmorMaterials.IRON);
    public static final Deferred<ArmorMaterial, ArmorMaterial> SALVAGER_ANCIENT = register("salvager_ancient", ArmorMaterials.NETHERITE);
    public static final Deferred<ArmorMaterial, ArmorMaterial> SALVAGER_MODERN = register("salvager_modern", ArmorMaterials.GOLD);
    public static final Deferred<ArmorMaterial, ArmorMaterial> ABYSSAL_LIGHT = register("abyssal_light", ArmorMaterials.DIAMOND);
    public static final Deferred<ArmorMaterial, ArmorMaterial> ABYSSAL_HEAVY = register("abyssal_heavy", ArmorMaterials.DIAMOND);

    private static Deferred<ArmorMaterial, ArmorMaterial> register(String name, Holder<ArmorMaterial> base) {
        return AquamiraeRegistries.REGISTRAR.register(Registries.ARMOR_MATERIAL, Aquamirae.id(name),
                () -> new ArmorMaterial(
                        base.value().defense(),
                        base.value().enchantmentValue(),
                        base.value().equipSound(),
                        base.value().repairIngredient(),
                        base.value().layers(),
                        base.value().toughness(),
                        base.value().knockbackResistance()));
    }

    static void init() {}
}