package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import net.minecraft.network.chat.Component;

public final class TerribleFangFullAbility extends SimpleAbility {

    public static final TerribleFangFullAbility SHARED = new TerribleFangFullAbility();

    private TerribleFangFullAbility() {
        super(Component.translatable("ability.aquamirae.terrible_fang.full"), VariableMap.builder());
    }
}
