package dev.obscuria.aquamirae.integration.hook;

import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.common.ability.armor.SalvagerArmorFullSetAbility;
import dev.obscuria.aquamirae.registry.AquamiraeEffects;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.registry.AquamiraeAbilitySets;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class HookPlayer {

    public static boolean isInvulnerableTo(Player player, DamageSource source) {
        if (source.is(DamageTypes.STARVE)) return false;
        if (player.getFoodData().getFoodLevel() > 0) return false;
        return player.hasEffect(AquamiraeEffects.DEVOURING_CURSE.holder());
    }

    public static void tick(Player player) {
        tickSalvagerArmor(player);
        tickIceMazeAmbience(player);
    }

    private static void tickSalvagerArmor(Player player) {
        if (AbilityHelper.isActive(
                AquamiraeAbilitySets.SALVAGER_ARMOR.holder(),
                SalvagerArmorFullSetAbility.SHARED, player)) {
            if (player.getAirSupply() > 0) return;
            player.setAirSupply(280);
            player.playSound(AquamiraeSounds.EVENT_AIR_SUPPLY);
        } else {
            if (player.level().isClientSide || player.isCreative() || player.isSpectator()) return;
            if (!player.isInWaterOrBubble()) return;
            if (player.getVehicle() != null) return;
            if (player.getTicksFrozen() > player.getTicksRequiredToFreeze() * 3) return;
            if (!AquamiraeHelper.isInIceMaze(player)) return;
            player.setTicksFrozen(player.getTicksFrozen() + 4);
        }
    }

    private static void tickIceMazeAmbience(Player player) {
        if (player.level().isClientSide
                && AquamiraeHelper.isInIceMaze(player)
                && player.level().random.nextFloat() < 0.01f
                && player.level().random.nextFloat() < 0.05f) {
            var pos = selectAmbientSoundPos(player.level().random);
            AquamiraeClient.playRelativeSound(AquamiraeSounds.AMBIENT_SHIP_HORN, pos, 1f, 1f);
        }
    }

    private static Vec3 selectAmbientSoundPos(RandomSource random) {
        var x = random.nextBoolean() ? -3 - random.nextDouble() * 7 : 3 + random.nextDouble() * 7;
        var y = random.nextDouble() * 4 - 2;
        var z = random.nextBoolean() ? -3 - random.nextDouble() * 7 : 3 + random.nextDouble() * 7;
        return new Vec3(x, y, z);
    }
}
