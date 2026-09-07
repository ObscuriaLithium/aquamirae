package dev.obscuria.aquamirae.registry;

import com.google.common.base.Suppliers;
import lombok.Getter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum AquamiraeTiers implements Tier {

    REMNANT_SABER(Tiers.STONE, 250, () -> Ingredient.of(AquamiraeItems.SHARP_BONES, AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    TERRIBLE_BLADE(Tiers.IRON, 750, () -> Ingredient.of(AquamiraeItems.ANGLER_FANG, AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    TERRIBLE_FANG(Tiers.IRON, 750, () -> Ingredient.of(AquamiraeItems.ANGLER_FANG, AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    TERRIBLE_CLEAVER(Tiers.IRON, 750, () -> Ingredient.of(AquamiraeItems.ANGLER_FANG, AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    DAGGER_OF_GREED(Tiers.DIAMOND, 0, () -> Ingredient.EMPTY),
    SHATTERBLADE(Tiers.DIAMOND, 2200, () -> Ingredient.of(AquamiraeItems.ABYSSAL_AMETHYST, AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    SHATTERSPIKE(Tiers.DIAMOND, 1400, () -> Ingredient.of(AquamiraeItems.ABYSSAL_AMETHYST, AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    TIDEPIERCER(Tiers.NETHERITE, 4400, () -> Ingredient.of(AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    JAWBREAKER(Tiers.NETHERITE, 4400, () -> Ingredient.of(AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    TEMPEST_TRIDENT(Tiers.NETHERITE, 4400, () -> Ingredient.of(AquamiraeItems.SHIP_GRAVEYARD_ECHO)),
    DREADWAKE(Tiers.NETHERITE, 4400, () -> Ingredient.of(AquamiraeItems.ABYSSAL_AMETHYST, AquamiraeItems.SHIP_GRAVEYARD_ECHO));

    @Getter private final int uses;
    private final Tier baseTier;
    private final Supplier<Ingredient> repairIngredient;

    AquamiraeTiers(Tier baseTier, int uses, Supplier<Ingredient> repairIngredient) {
        this.uses = uses;
        this.baseTier = baseTier;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public float getSpeed() {
        return baseTier.getSpeed();
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return baseTier.getIncorrectBlocksForDrops();
    }

    @Override
    public int getEnchantmentValue() {
        return baseTier.getEnchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public float getAttackDamageBonus() {
        return 0;
    }
}
