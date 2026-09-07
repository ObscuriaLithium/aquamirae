package dev.obscuria.aquamirae.common.ability.armor;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import net.minecraft.network.chat.Component;

public final class SalvagerArmorFullSetAbility extends SimpleAbility {

    public static final SalvagerArmorFullSetAbility SHARED = new SalvagerArmorFullSetAbility();

    private SalvagerArmorFullSetAbility() {
        super(Component.translatable("ability.aquamirae.salvager_armor.full"), VariableMap.builder());
    }
}
