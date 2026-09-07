package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.DecimalFormatter;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public final class DreadwakeBaseAbility extends SimpleAbility implements IDamageHandler {

    public static final DreadwakeBaseAbility SHARED = new DreadwakeBaseAbility();

    public static final String VAR_DURABILITY = "durability";

    private DreadwakeBaseAbility() {
        super(Component.translatable("ability.aquamirae.dreadwake.base"), VariableMap.builder()
                .define(VAR_DURABILITY, new Constant(10), DecimalFormatter.SHARED));
    }

    @Override
    public void onDamageDealt(LivingEntity self, LivingEntity victim, DamageSource source) {
        if (!victim.isDeadOrDying()) return;
        var weapon = self.getMainHandItem();
        var currentCharges = weapon.getOrDefault(AquamiraeDataComponents.ABYSSAL_CHARGES, 0);
        weapon.set(AquamiraeDataComponents.ABYSSAL_CHARGES, currentCharges + 1);
    }
}
