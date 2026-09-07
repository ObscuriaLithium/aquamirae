package dev.obscuria.aquamirae.api.common.ability;

import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.aquamirae.registry.AquamiraeRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public final class AbilityHelper {

    public static int countPieces(Holder<AbilitySet> abilitySet, LivingEntity self) {
        return abilitySet.value().resolver().countPieces(abilitySet, self);
    }

    public static int countPiecesForTooltip(Holder<AbilitySet> abilitySet, LivingEntity self, ItemStack stack) {
        return abilitySet.value().resolver().countPiecesForTooltip(abilitySet, self, stack);
    }

    public static void forEachActiveAbility(Holder<AbilitySet> abilitySet, LivingEntity self, Consumer<Ability> consumer) {
        var pieces = countPieces(abilitySet, self);
        for (var tier : abilitySet.value().tiers()) {
            if (!tier.shouldActivate(abilitySet, self)) continue;
            if (pieces < tier.requiredPieces()) continue;
            consumer.accept(tier.ability());
        }
    }

    public static <T> void forEachCapability(Class<T> ofType, LivingEntity self, Consumer<T> consumer) {
        AquamiraeRegistries.ABILITY_SET.holders().forEach(abilitySet -> {
            forEachActiveAbility(abilitySet, self, ability -> {
                if (ofType.isInstance(ability)) {
                    consumer.accept(ofType.cast(ability));
                }
            });
        });
    }

    public static void animateCritEffect(LivingEntity attacker, LivingEntity victim) {
        if (victim.level() instanceof ServerLevel level) {
            level.getChunkSource().broadcastAndSend(attacker, new ClientboundAnimatePacket(victim, 4));
            level.playSound(null, victim, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1f, 1f);
        } else {
            AquamiraeClient.animateCritEffect(victim);
        }
    }

    public static boolean isActive(Holder<AbilitySet> abilitySet, Ability ability, LivingEntity self) {
        for (var tier : abilitySet.value().tiers()) {
            if (tier.ability() != ability) continue;
            if (!tier.shouldActivate(abilitySet, self)) return false;
            return countPieces(abilitySet, self) >= tier.requiredPieces();
        }
        return false;
    }

    public static boolean hasAbilitySet(ItemStack stack, Holder<AbilitySet> abilitySet) {
        return isSame(abilitySet, stack.get(AquamiraeDataComponents.ABILITY_SET));
    }

    public static boolean isSame(Holder<AbilitySet> abilitySet, @Nullable AbilitySetComponent component) {
        return component != null && Objects.equals(abilitySet, component.abilitySet());
    }
}
