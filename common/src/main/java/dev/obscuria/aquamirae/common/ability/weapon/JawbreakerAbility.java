package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import net.minecraft.network.chat.Component;

public final class JawbreakerAbility extends SimpleAbility {

    public static final JawbreakerAbility SHARED = new JawbreakerAbility();

    public static final String VAR_DURABILITY = "durability";

    private JawbreakerAbility() {
        super(Component.translatable("ability.aquamirae.jawbreaker"), VariableMap.builder()
                .define(VAR_DURABILITY, new Constant(10), DecimalFormatter.SHARED));
    }
}
