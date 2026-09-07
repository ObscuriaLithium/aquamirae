package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.api.common.entity.SubPartEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ShellbackChestPart extends SubPartEntity<Shellback> {

    public static final Component MENU_TITLE = Component.translatable("container.chest");

    public final MenuProvider menuProvider;
    public final ShellbackChestType partType;
    public final EntityDimensions size;
    public final int chestIndex;

    public ShellbackChestPart(Shellback shellback, ShellbackChestType partType) {
        super(shellback);
        this.menuProvider = new SimpleMenuProvider(this::createMenu, MENU_TITLE);
        this.partType = partType;
        this.chestIndex = partType.ordinal();
        this.size = EntityDimensions.scalable(
                partType.bbWidth,
                partType.bbHeight);
        refreshDimensions();
    }

    public boolean attachChest(Player player, InteractionHand hand) {
        if (getParent().hasChest(chestIndex)) return false;

        var stack = player.getItemInHand(hand);
        if (stack.isEmpty() || !stack.is(Items.CHEST)) return false;

        if (!level().isClientSide()) {
            getParent().setChest(chestIndex, true);
            playSound(SoundEvents.ARMOR_EQUIP_IRON.value());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return true;
    }

    public boolean detachChest(Player player, InteractionHand hand) {
        if (!getParent().hasChest(chestIndex)) return false;

        if (!level().isClientSide()) {
            getParent().dropChestContents(chestIndex);
            spawnAtLocation(Blocks.CHEST);
            getParent().setChest(chestIndex, false);
            playSound(SoundEvents.SHEEP_SHEAR, 0.8f, 1.0f);
            var shears = player.getItemInHand(hand);
            if (!player.getAbilities().instabuild) {
                shears.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
        }
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return getParent().interact(player, hand);

        var stack = player.getItemInHand(hand);
        boolean hasChest = getParent().hasChest(chestIndex);

        if (!hasChest && stack.is(Items.CHEST)) {
            return attachChest(player, hand)
                    ? InteractionResult.sidedSuccess(level().isClientSide())
                    : InteractionResult.PASS;
        }

        if (hasChest && stack.is(Items.SHEARS)) {
            return detachChest(player, hand)
                    ? InteractionResult.sidedSuccess(level().isClientSide())
                    : InteractionResult.PASS;
        }

        if (hasChest) {
            if (level().isClientSide()) return InteractionResult.SUCCESS;
            if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;
            serverPlayer.openMenu(menuProvider);
            return InteractionResult.CONSUME;
        }

        return getParent().interact(player, hand);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return size;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public ItemStack getPickResult() {
        return Items.CHEST.getDefaultInstance();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            playSound(SoundEvents.SHIELD_BLOCK);
            return false;
        }
        return getParent().hurt(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return getParent().hasChest(chestIndex);
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || getParent() == entity;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    protected ChestMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        var chestView = getParent().getInventory().createChestView(this, chestIndex);
        return ChestMenu.threeRows(containerId, playerInventory, chestView);
    }
}