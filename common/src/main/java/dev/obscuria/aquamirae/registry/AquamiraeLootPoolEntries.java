
package dev.obscuria.aquamirae.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.patreon.KeepsakeArmorLoot;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

public final class AquamiraeLootPoolEntries {

    public static final LootPoolEntryType KEEPSAKE_ARMOR = register("keepsake_armor", KeepsakeArmorLoot.CODEC);

    private static LootPoolEntryType register(String name, MapCodec<? extends LootPoolEntryContainer> codec) {
        var type = new LootPoolEntryType(codec);
        AquamiraeRegistries.REGISTRAR.register(Registries.LOOT_POOL_ENTRY_TYPE, Aquamirae.id(name), () -> type);
        return type;
    }

    static void init() {}
}