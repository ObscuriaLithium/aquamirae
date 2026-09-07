package dev.obscuria.aquamirae.common.item;

import com.google.common.base.Predicates;
import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.api.server.TickableEvent;
import dev.obscuria.aquamirae.api.server.ServerLevelHelper;
import dev.obscuria.aquamirae.common.entity.CaptainCorneliaBoss;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ShellHornItem extends Item {

    private static final Component FEEDBACK_NOTHING_HAPPENED;
    private static final Component FEEDBACK_SUMMON_CAPTAIN_CORNELIA;

    public ShellHornItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (!(player.level() instanceof ServerLevel serverLevel)) return InteractionResultHolder.success(stack);
        serverLevel.playSound(null, player.blockPosition().above(), AquamiraeSounds.ITEM_SHELL_HORN_USE, SoundSource.PLAYERS, 3, 1);
        player.getCooldowns().addCooldown(stack.getItem(), 20 * CommonConfig.SHELL_HORN_COOLDOWN.get());
        @Nullable var summonAt = findValidSummonPos(serverLevel, player);
        if (!player.getAbilities().instabuild && summonAt != null) stack.shrink(1);
        ServerLevelHelper.triggerEvent(serverLevel, new SummonEvent(player, summonAt));
        return InteractionResultHolder.consume(stack);
    }

    private static @Nullable BlockPos findValidSummonPos(ServerLevel level, Player player) {
        if (!CommonConfig.SHELL_HORN_USAGE_MODE.get().test(player)) return null;
        int waterSurfaceY = level.getSeaLevel() - 1;
        int spawnY = waterSurfaceY + CommonConfig.SHELL_HORN_SPAWN_OFFSET.get();
        int searchRadius = CommonConfig.SHELL_HORN_SEARCH_RADIUS.get();
        var pos = new BlockPos.MutableBlockPos();
        for (var x = player.getBlockX() - searchRadius; x <= player.getBlockX() + searchRadius; x++) {
            for (var z = player.getBlockZ() - searchRadius; z <= player.getBlockZ() + searchRadius; z++) {
                if (!level.isWaterAt(pos.set(x, spawnY, z))) continue;
                if (!level.isWaterAt(pos.set(x, waterSurfaceY, z))) continue;
                if (!level.isWaterAt(pos.set(x + 1, waterSurfaceY, z))) continue;
                if (!level.isWaterAt(pos.set(x - 1, waterSurfaceY, z))) continue;
                if (!level.isWaterAt(pos.set(x, waterSurfaceY, z + 1))) continue;
                if (!level.isWaterAt(pos.set(x, waterSurfaceY, z - 1))) continue;
                return pos.set(x, spawnY, z).immutable();
            }
        }
        return null;
    }

    static {
        FEEDBACK_NOTHING_HAPPENED = Component.translatable("feedback.aquamirae.nothing_happened");
        FEEDBACK_SUMMON_CAPTAIN_CORNELIA = Component.translatable("feedback.aquamirae.summon_captain_cornelia");
    }

    public enum UsageMode {
        EVERYWHERE(Predicates.alwaysTrue()),
        ICE_MAZE(AquamiraeHelper::isInIceMaze);

        private final Predicate<Player> predicate;

        UsageMode(Predicate<Player> predicate) {
            this.predicate = predicate;
        }

        public boolean test(Player player) {
            return predicate.test(player);
        }
    }

    private static class SummonEvent implements TickableEvent {

        private final int summonDelay;
        private final Player summoner;
        private final @Nullable BlockPos summonAt;
        private int tickCount;

        public SummonEvent(Player summoner, @Nullable BlockPos summonAt) {
            this.summonDelay = 20 * CommonConfig.SHELL_HORN_SUMMON_DELAY.get();
            this.summoner = summoner;
            this.summonAt = summonAt;
        }

        @Override
        public void tick(ServerLevel level) {
            if (tickCount == summonDelay) {
                if (summonAt == null) {
                    summoner.sendSystemMessage(FEEDBACK_NOTHING_HAPPENED.copy().withStyle(ChatFormatting.RED));
                } else {
                    var difficulty = summoner.level().getCurrentDifficultyAt(summonAt);
                    var captainCornelia = new CaptainCorneliaBoss(AquamiraeEntityTypes.CAPTAIN_CORNELIA.get(), level);
                    captainCornelia.moveTo(summonAt.getCenter());
                    captainCornelia.finalizeSpawn(level, difficulty, MobSpawnType.MOB_SUMMONED, null);
                    level.addFreshEntity(captainCornelia);
                    summoner.sendSystemMessage(FEEDBACK_SUMMON_CAPTAIN_CORNELIA.copy().withStyle(ChatFormatting.LIGHT_PURPLE));
                }
            }
            this.tickCount++;
        }

        @Override
        public boolean isFinished() {
            return tickCount > summonDelay;
        }
    }
}