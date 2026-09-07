package dev.obscuria.aquamirae.common.ability.weapon;

import dev.obscuria.aquamirae.api.common.ability.SimpleAbility;
import dev.obscuria.aquamirae.api.common.ability.variable.VariableMap;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.variable.Constant;
import dev.obscuria.aquamirae.api.common.ability.variable.formatter.PercentageFormatter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.Items;

public final class DaggerOfGreedAbility extends SimpleAbility implements IDamageHandler {

    public static final DaggerOfGreedAbility SHARED = new DaggerOfGreedAbility();

    public static final String VAR_VILLAGER_CHANCE = "villager_chance";
    public static final String VAR_PILLAGER_CHANCE = "pillager_chance";
    public static final String VAR_BREAK_CHANCE = "break_chance";

    private DaggerOfGreedAbility() {
        super(Component.translatable("ability.aquamirae.dagger_of_greed"), VariableMap.builder()
                .define(VAR_VILLAGER_CHANCE, new Constant(0.5f), PercentageFormatter.SHARED)
                .define(VAR_PILLAGER_CHANCE, new Constant(1.0f), PercentageFormatter.SHARED)
                .define(VAR_BREAK_CHANCE, new Constant(0.01f), PercentageFormatter.SHARED));
    }

    @Override
    public void onDamageDealt(LivingEntity self, LivingEntity victim, DamageSource source) {
        if (self.getRandom().nextFloat() < computeEmeraldChance(self, victim))
            victim.spawnAtLocation(Items.EMERALD);
        if (self.getRandom().nextFloat() < computeVariable(VAR_BREAK_CHANCE, self)) {
            self.onEquippedItemBroken(self.getMainHandItem().getItem(), EquipmentSlot.MAINHAND);
            self.getMainHandItem().shrink(1);
        }
    }

    private float computeEmeraldChance(LivingEntity self, LivingEntity victim) {
        if (victim instanceof AbstractVillager) return computeVariable(VAR_VILLAGER_CHANCE, self);
        if (victim instanceof AbstractIllager) return computeVariable(VAR_PILLAGER_CHANCE, self);
        return 0f;
    }
}
