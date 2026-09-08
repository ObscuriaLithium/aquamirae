package dev.obscuria.aquamirae.integration.hook;

import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.api.common.ability.capability.IDamageHandler;
import dev.obscuria.aquamirae.api.common.ability.capability.IDeathHandler;
import dev.obscuria.aquamirae.api.common.ability.capability.IEffectHandler;
import dev.obscuria.aquamirae.api.server.ServerLevelHelper;
import dev.obscuria.aquamirae.common.AquamiraeCombatRules;
import dev.obscuria.aquamirae.common.ability.weapon.RemnantSaberAbility;
import dev.obscuria.aquamirae.integration.DamageContext;
import dev.obscuria.aquamirae.registry.AquamiraeAttributes;
import dev.obscuria.aquamirae.registry.AquamiraeEffects;
import dev.obscuria.aquamirae.server.event.CrystallizationEvent;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.Consumer;

public final class HookLivingEntity {

    public static void createLivingAttributes(Consumer<Holder<Attribute>> consumer) {
        consumer.accept(AquamiraeAttributes.DEPTHS_FURY.holder());
        consumer.accept(AquamiraeAttributes.DEPTHS_RESILIENCE.holder());
    }

    public static void onHurt(LivingEntity self, DamageSource source) {
        AbilityHelper.forEachCapability(IDamageHandler.class, self,
                handler -> handler.onDamageTaken(self, source));
        if (source.getEntity() instanceof LivingEntity attacker) {
            RemnantSaberAbility.onDamageDealtCallback(attacker, self);
            AbilityHelper.forEachCapability(IDamageHandler.class, attacker,
                    handler -> handler.onDamageDealt(attacker, self, source));
        }
    }

    public static void onEffectAdded(LivingEntity entity, MobEffectInstance instance) {
        AbilityHelper.forEachCapability(IEffectHandler.class, entity,
                handler -> handler.onEffectAdded(entity, instance));
    }

    public static void onEffectRemoved(LivingEntity entity, MobEffectInstance instance) {
        AbilityHelper.forEachCapability(IEffectHandler.class, entity,
                handler -> handler.onEffectRemoved(entity, instance));
        if (!instance.getEffect().equals(AquamiraeEffects.CRYSTALLIZATION.holder())) return;
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;
        ServerLevelHelper.triggerEvent(serverLevel, new CrystallizationEvent(entity));
    }

    public static float modifyIncomingDamage(LivingEntity self, DamageSource source, float damage) {
        var context = new DamageContext(damage);

        AbilityHelper.forEachCapability(IDamageHandler.class, self,
                handler -> handler.modifyIncomingDamage(self, source, context));
        if (source.getEntity() instanceof LivingEntity attacker) {
            AbilityHelper.forEachCapability(IDamageHandler.class, attacker,
                    handler -> handler.modifyOutgoingDamage(attacker, self, context));
        }

        if (AquamiraeHelper.isAffectedByDepthsResilience(source.getEntity()))
            AquamiraeCombatRules.modifyDamageByDepthsResilience(self, source, context);
        if (AquamiraeHelper.isAffectedByDepthsFury(self))
            AquamiraeCombatRules.modifyDamageByDepthsFury(self, source, context);

        AquamiraeCombatRules.modifyDamageByRuneDamage(self, source, context);
        AquamiraeCombatRules.modifyDamageByRuneProtection(self, source, context);
        AquamiraeCombatRules.applyDevouringCurse(self, source, context);
        return context.result();
    }

    public static float modifyArmorValue(LivingEntity self, DamageSource source, float armor) {
        if (!(source.getEntity() instanceof LivingEntity attacker)) return armor;
        if (!AquamiraeHelper.isAffectedByDepthsResilience(attacker)) return armor;
        return AquamiraeCombatRules.modifyArmorByDepthsResilience(self, source, armor);
    }

    public static boolean cancelDeath(LivingEntity entity, DamageSource source) {
        final boolean[] result = {false};
        AbilityHelper.forEachCapability(IDeathHandler.class, entity, handler -> {
            if (!handler.cancelDeath(entity, source)) return;
            result[0] = true;
        });
        return result[0];
    }
}
