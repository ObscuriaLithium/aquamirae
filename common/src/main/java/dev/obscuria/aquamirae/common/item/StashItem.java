package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.common.block.StashBlock;
import dev.obscuria.fragmentum.content.registry.DeferredBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.function.Consumer;

public class StashItem extends BlockItem {

    private final SoundEvent openSound;

    public StashItem(DeferredBlock<? extends StashBlock> block, SoundEvent openSound, Properties properties) {
        super(block.get(), properties);
        this.openSound = openSound;
    }

    @SuppressWarnings("deprecation")
    public ResourceLocation getLootTableId(ItemStack stack) {
        return builtInRegistryHolder().key().location().withPrefix("stashes/");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 10);
        player.swing(hand);
        if (!level.isClientSide()) {
            level.playSound(null, player, openSound, SoundSource.PLAYERS, 1.0F, 1.0F);
            generateLoot(getLootTableId(stack), player, loot -> {
                if (player.addItem(loot)) return;
                player.drop(loot, true);
            });
            if (!player.getAbilities().instabuild)
                stack.shrink(1);
        }
        return InteractionResultHolder.consume(stack);
    }

    public static void generateLoot(ResourceLocation lootTableId, Player player, Consumer<ItemStack> consumer) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;
        var lootTableKey = ResourceKey.create(Registries.LOOT_TABLE, lootTableId);
        var lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableKey);
        var params = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .create(LootContextParamSets.GIFT);
        lootTable.getRandomItems(params, consumer);
    }
}