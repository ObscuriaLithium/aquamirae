package dev.obscuria.aquamirae.common.ability.armor;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public final class AbyssalArmorFullSetWithHeaumeAbility extends SimpleAbility implements IDamageHandler {

    public static final AbyssalArmorFullSetWithHeaumeAbility SHARED = new AbyssalArmorFullSetWithHeaumeAbility();

    private AbyssalArmorFullSetWithHeaumeAbility() {
        super(Component.translatable("ability.aquamirae.abyssal_armor.full_heaume"), VariableMap.builder());
    }

    @Override
    public void onDamageDealt(LivingEntity self, LivingEntity victim, DamageSource source) {
        for (var mob : self.level().getEntitiesOfClass(Mob.class, self.getBoundingBox().inflate(32f))) {
            if (!(mob.getTarget() instanceof Player player)) continue;
            if (self != player) mob.setTarget(self);
        }
    }
}
