package dev.obscuria.aquamirae.common.patreon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.registry.AquamiraeLootPoolEntries;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.Consumer;

public class KeepsakeArmorLoot extends LootPoolSingletonContainer {

    public static final MapCodec<KeepsakeArmorLoot> CODEC;

    protected KeepsakeArmorLoot(
            Unit unit, int weight, int quality,
            List<LootItemCondition> conditions,
            List<LootItemFunction> functions
    ) {
        super(weight, quality, conditions, functions);
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        consumer.accept(KeepsakeManager.generateRandom(lootContext.getRandom(), lootContext.getResolver()));
    }

    @Override
    public LootPoolEntryType getType() {
        return AquamiraeLootPoolEntries.KEEPSAKE_ARMOR;
    }

    private Unit unit() {
        return Unit.INSTANCE;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                Unit.CODEC.optionalFieldOf("unit", Unit.INSTANCE).forGetter(KeepsakeArmorLoot::unit)
        ).and(singletonFields(builder)).apply(builder, KeepsakeArmorLoot::new));
    }
}
