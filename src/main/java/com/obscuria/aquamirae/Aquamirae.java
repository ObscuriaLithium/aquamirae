package com.obscuria.aquamirae;

import com.obscuria.aquamirae.common.items.armor.AbyssalArmorItem;
import com.obscuria.aquamirae.common.items.armor.TerribleArmorItem;
import com.obscuria.aquamirae.common.items.armor.ThreeBoltArmorItem;
import com.obscuria.aquamirae.common.items.weapon.CoralLanceItem;
import com.obscuria.aquamirae.common.items.weapon.FinCutterItem;
import com.obscuria.aquamirae.common.items.weapon.RemnantsSaberItem;
import com.obscuria.aquamirae.registry.*;
import com.obscuria.obscureapi.api.ClassManager;
import com.obscuria.obscureapi.api.common.classes.ObscureClass;
import com.obscuria.obscureapi.event.ObscureAPIEnchantmentsEvent;
import com.obscuria.obscureapi.registry.ObscureAPIAttributes;
import com.obscuria.obscureapi.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(Aquamirae.MODID)
public class Aquamirae {

    private static final String protocolVersion = "1";
    private static int messageID = 0;

    public static final String MODID = "aquamirae";
    public static final Logger LOGGER = LogManager.getLogger(Aquamirae.class);
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> protocolVersion, protocolVersion::equals, protocolVersion::equals);
    public static final ObscureClass SEA_WOLF = ClassManager.register(Aquamirae.MODID, "sea_wolf");
    public static final TagKey<Biome> ICE_MAZE = TagKey.create(Registries.BIOME, new ResourceLocation(Aquamirae.MODID, "ice_maze"));
    public static final TagKey<Structure> SHIP = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Aquamirae.MODID, "ship"));
    public static final TagKey<Structure> OUTPOST = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Aquamirae.MODID, "outpost"));
    public static final TagKey<Structure> SHELTER = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Aquamirae.MODID, "shelter"));
    public static final TagKey<Block> EEL_MOVE = BlockTags.create(new ResourceLocation(MODID, "eel_move"));
    public static final TagKey<Block> MAZE_MOTHER_DESTROY = BlockTags.create(new ResourceLocation(MODID, "maze_mother_destroy"));
    public static final TagKey<Block> SCROLL_DESTROY = BlockTags.create(new ResourceLocation(MODID, "scroll_destroy"));
    public static final ResourceKey<DamageType> CRYSTALLIZATION = ResourceKey.create(Registries.DAMAGE_TYPE, key("crystallization"));

    public static ResourceLocation key(String name) {
        return new ResourceLocation(MODID, name);
    }

    public Aquamirae() {
        var eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        AquamiraeRegistries.init(eventBus);
        AquamiraeConfig.register();

        eventBus.addListener(this::commonSetup);
        eventBus.addListener(EventPriority.HIGHEST, this::registerEnchantments);

        MinecraftForge.EVENT_BUS.addListener(this::onEntitySpawn);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityAttacked);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityHurt);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);

        if (FMLEnvironment.dist == Dist.CLIENT)
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, AquamiraeClient.getConfig());
    }

    private void registerEnchantments(final @NotNull ObscureAPIEnchantmentsEvent event) {
        event.registerMirror(true);
        event.registerDistance(true);
        event.registerFastSpin(true);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ItemUtils.addLore("aquamirae:sea_casserole");
        ItemUtils.addLore("aquamirae:sea_stew");
        ItemUtils.addLore("aquamirae:poseidons_breakfast");
        ItemUtils.addLore("aquamirae:ship_graveyard_echo");
        ItemUtils.addLore("aquamirae:pirate_pouch");
        ItemUtils.addLore("aquamirae:treasure_pouch");
        ItemUtils.addLore("aquamirae:luminescent_bubble");
        ItemUtils.addLore("aquamirae:luminescent_lamp");
        ItemUtils.addLore("aquamirae:shell_horn");
        ItemUtils.addLore("aquamirae:dead_sea_scroll");
        ItemUtils.addLore("aquamirae:frozen_key");
        ItemUtils.addLore("aquamirae:wisteria_niveis");
        ItemUtils.addLore("aquamirae:golden_moth_in_a_jar");
        ItemUtils.addLore("aquamirae:rune_of_the_storm");
        ItemUtils.addLore("aquamirae:oxygelium");
    }

    public static void loadFromConfig(@NotNull LivingEntity entity, Attribute attribute, double amount) {
        final AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance != null) attributeInstance.setBaseValue(amount);
        if (attribute == Attributes.MAX_HEALTH) entity.setHealth(entity.getMaxHealth());
    }

    public static ItemStack getStructureMap(TagKey<Structure> tag, @NotNull ServerLevel server, @NotNull Entity source) {
        BlockPos pos = server.findNearestMapStructure(tag, source.blockPosition(), 100, false);
        if (pos != null) {
            final Component name = tag == SHIP ? Component.translatable("filled_map.aquamirae.ship")
                    : tag == OUTPOST ? Component.translatable("filled_map.aquamirae.outpost")
                    : tag == SHELTER ? Component.translatable("filled_map.aquamirae.shelter")
                    : Component.translatable("filled_map.buried_treasure");
            final ItemStack map = MapItem.create(server, pos.getX(), pos.getZ(), (byte) 2, true, true);
            MapItem.renderBiomePreviewMap(server, map);
            MapItemSavedData.addTargetDecoration(map, pos, "+", MapDecoration.Type.RED_X);
            map.setHoverName(name);
            final CompoundTag display = map.getOrCreateTag().getCompound("display");
            final ListTag lore = new ListTag();
            lore.add(StringTag.valueOf("{\"text\":\"§7x: " + pos.getX() + "\"}"));
            lore.add(StringTag.valueOf("{\"text\":\"§7z: " + pos.getZ() + "\"}"));
            display.put("Lore", lore);
            map.addTagElement("display", display);
            return map;
        }
        return ItemStack.EMPTY;
    }

    public static boolean winterEvent() {
        return Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER || Calendar.getInstance().get(Calendar.MONTH) == Calendar.JANUARY;
    }

    private void onEntitySpawn(@NotNull MobSpawnEvent event) {
        final Mob mob = event.getEntity();
        if (mob instanceof Pillager) this.modifyLootTable(mob, "entities/maze_pillager");
        if (mob instanceof Vindicator) this.modifyLootTable(mob, "entities/maze_vindicator");
    }

    private void modifyLootTable(@NotNull Mob mob, String loot) {
        if (!AquamiraeUtils.isInIceMaze(mob) || mob.getLootTable().toString().endsWith("captain")) return;
        mob.getPersistentData().putString("DeathLootTable", Aquamirae.MODID + ":" + loot);
    }

    private void onPlayerTick(final TickEvent.@NotNull PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide || event.player.isCreative() || event.player.isSpectator())
            return;
        if (AquamiraeUtils.isInIceMaze(event.player))
            if (event.player.isInWaterOrBubble() && event.player.getTicksFrozen() <= event.player.getTicksRequiredToFreeze() * 3)
                if (ItemUtils.getArmorPieces(event.player, ThreeBoltArmorItem.class) < 4)
                    event.player.setTicksFrozen(event.player.getTicksFrozen() + 4);
    }

    private void onEntityAttacked(final @NotNull LivingHurtEvent event) {
        //Fin Cutter
        if (event.getSource().getEntity() instanceof LivingEntity source && source.getMainHandItem().getItem() instanceof FinCutterItem item) {
            final int emptyHP = (int) Math.floor((source.getMaxHealth() - source.getHealth()) / 2);
            event.setAmount(event.getAmount() + event.getAmount() *
                    Math.min(item.ABILITY.getVariable(source, 2) * 0.01F, emptyHP * item.ABILITY.getVariable(source, 1) * 0.01F));
        }
        //Terrible Armor
        if (event.getEntity() instanceof Player player) {
            final int TOTAL = ItemUtils.getArmorPieces(player, TerribleArmorItem.class);
            if (TOTAL >= 2) {
                final ItemStack piece = getArmor(player, TerribleArmorItem.class);
                if (player.isInWater() && !player.getCooldowns().isOnCooldown(piece.getItem()) && piece.getItem() instanceof TerribleArmorItem item) {
                    player.addEffect(new MobEffectInstance(AquamiraeMobEffects.SWIM_SPEED.get(), 20 * item.ABILITY_HALFSET.getVariable(player, 2),
                            Math.min(19, item.ABILITY_HALFSET.getVariable(player, 1) / 10 - 1), false, false));
                    final int cooldown = 20 * item.ABILITY_HALFSET.getCost(player);
                    cooldown(player, TerribleArmorItem.class, cooldown);
                }
                if (TOTAL >= 4 && event.getSource().getEntity() instanceof LivingEntity source && piece.getItem() instanceof TerribleArmorItem item) {
                    source.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * item.ABILITY_FULLSET.getVariable(player, 1), 1, false, false));
                }
            }
        }
    }

    private void onEntityHurt(final @NotNull LivingHurtEvent event) {
        //Remnants Saber
        if (event.getSource().getEntity() instanceof LivingEntity source && source.isInWater()
                && source.getMainHandItem().getItem() instanceof RemnantsSaberItem item)
            event.setAmount(event.getAmount() * (1F + item.ABILITY.getVariable(source, 1) * 0.01F));
        //Coral Lance
        if (event.getSource().getEntity() instanceof LivingEntity source && source.getMainHandItem().getItem() instanceof CoralLanceItem item
                && AquamiraeUtils.isShipGraveyardEntity(event.getEntity())) {
            event.setAmount(event.getAmount() * (1F + item.ABILITY.getVariable(source, 1) * 0.01F));
        }
    }

    private void onEntityDeath(final LivingDeathEvent event) {
        //Abyssal Armor
        if (event.getSource().is(Aquamirae.CRYSTALLIZATION)) return;

        final var entity = event.getEntity();
        final int armorPieces = ItemUtils.getArmorPieces(entity, AbyssalArmorItem.class);
        if (armorPieces < 4 || entity.hasEffect(AquamiraeMobEffects.CRYSTALLIZATION.get())) return;

        final var item = (AbyssalArmorItem) getArmor(entity, AbyssalArmorItem.class).getItem();

        entity.addEffect(new MobEffectInstance(AquamiraeMobEffects.CRYSTALLIZATION.get(), 20 * item.ABILITY_FULLSET_1.getVariable(entity, 1), 0, true, true));
        entity.setHealth(entity.getMaxHealth());

        entity.getItemBySlot(EquipmentSlot.HEAD).hurtAndBreak(50, entity, e -> e.broadcastBreakEvent(EquipmentSlot.HEAD));
        entity.getItemBySlot(EquipmentSlot.CHEST).hurtAndBreak(50, entity, e -> e.broadcastBreakEvent(EquipmentSlot.CHEST));
        entity.getItemBySlot(EquipmentSlot.LEGS).hurtAndBreak(50, entity, e -> e.broadcastBreakEvent(EquipmentSlot.LEGS));
        entity.getItemBySlot(EquipmentSlot.FEET).hurtAndBreak(50, entity, e -> e.broadcastBreakEvent(EquipmentSlot.FEET));

        if (entity.level() instanceof ServerLevel level) {
            level.playSound(null, entity.blockPosition().above(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1, 1);
        }

        event.setCanceled(true);
    }

    private ItemStack getArmor(@NotNull LivingEntity entity, @NotNull Class<?> armor) {
        return armor.isAssignableFrom(entity.getItemBySlot(EquipmentSlot.HEAD).getItem().getClass()) ?
                entity.getItemBySlot(EquipmentSlot.HEAD) :
                armor.isAssignableFrom(entity.getItemBySlot(EquipmentSlot.CHEST).getItem().getClass()) ?
                        entity.getItemBySlot(EquipmentSlot.CHEST) :
                        armor.isAssignableFrom(entity.getItemBySlot(EquipmentSlot.LEGS).getItem().getClass()) ?
                                entity.getItemBySlot(EquipmentSlot.LEGS) :
                                armor.isAssignableFrom(entity.getItemBySlot(EquipmentSlot.FEET).getItem().getClass()) ?
                                        entity.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY;
    }

    private void cooldown(@NotNull Player player, @NotNull Class<?> armor, int cooldown) {
        if (armor.isAssignableFrom(player.getItemBySlot(EquipmentSlot.HEAD).getItem().getClass()))
            player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.HEAD).getItem(), cooldown);
        if (armor.isAssignableFrom(player.getItemBySlot(EquipmentSlot.CHEST).getItem().getClass()))
            player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.CHEST).getItem(), cooldown);
        if (armor.isAssignableFrom(player.getItemBySlot(EquipmentSlot.LEGS).getItem().getClass()))
            player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.LEGS).getItem(), cooldown);
        if (armor.isAssignableFrom(player.getItemBySlot(EquipmentSlot.FEET).getItem().getClass()))
            player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.FEET).getItem(), cooldown);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    public static class SetBuilder {

        public static @NotNull List<ItemStack> common() {
            final int c1 = getColor(10, 220, 160);
            final int c2 = getColor(10, 190, 220);
            final int c3 = getColor(10, 130, 220);
            List<ItemStack> list = new ArrayList<>();
            add(list, Items.LEATHER_HELMET, EquipmentSlot.HEAD, 1, 1, c1, "dead_sea_hat", Attributes.ATTACK_DAMAGE);
            add(list, Items.LEATHER_HELMET, EquipmentSlot.HEAD, 1, 1, c2, "twilight_grotto_hat", ObscureAPIAttributes.PENETRATION.get());
            add(list, Items.LEATHER_HELMET, EquipmentSlot.HEAD, 1, 1, c3, "sea_tramps_hat", ObscureAPIAttributes.CRITICAL_HIT.get());
            add(list, Items.LEATHER_CHESTPLATE, EquipmentSlot.CHEST, 1, 3, c1, "dead_sea_doublet", Attributes.ATTACK_DAMAGE);
            add(list, Items.LEATHER_CHESTPLATE, EquipmentSlot.CHEST, 1, 3, c2, "twilight_grotto_doublet", ObscureAPIAttributes.PENETRATION.get());
            add(list, Items.LEATHER_CHESTPLATE, EquipmentSlot.CHEST, 1, 3, c3, "sea_tramps_doublet", ObscureAPIAttributes.CRITICAL_HIT.get());
            add(list, Items.LEATHER_LEGGINGS, EquipmentSlot.LEGS, 1, 2, c1, "dead_sea_pants", Attributes.ATTACK_DAMAGE);
            add(list, Items.LEATHER_LEGGINGS, EquipmentSlot.LEGS, 1, 2, c2, "twilight_grotto_pants", ObscureAPIAttributes.PENETRATION.get());
            add(list, Items.LEATHER_LEGGINGS, EquipmentSlot.LEGS, 1, 2, c3, "sea_tramps_pants", ObscureAPIAttributes.CRITICAL_HIT.get());
            add(list, Items.LEATHER_BOOTS, EquipmentSlot.FEET, 1, 1, c1, "dead_sea_boots", Attributes.ATTACK_DAMAGE);
            add(list, Items.LEATHER_BOOTS, EquipmentSlot.FEET, 1, 1, c2, "twilight_grotto_boots", ObscureAPIAttributes.PENETRATION.get());
            add(list, Items.LEATHER_BOOTS, EquipmentSlot.FEET, 1, 1, c3, "sea_tramps_boots", ObscureAPIAttributes.CRITICAL_HIT.get());
            return list;
        }

        public static @NotNull List<ItemStack> rare() {
            List<ItemStack> list = new ArrayList<>();
            add(list, Items.IRON_HELMET, EquipmentSlot.HEAD, 2, 2, 0, "dead_sea_helmet", Attributes.ATTACK_DAMAGE);
            add(list, Items.IRON_HELMET, EquipmentSlot.HEAD, 2, 2, 0, "twilight_grotto_helmet", ObscureAPIAttributes.PENETRATION.get());
            add(list, Items.IRON_HELMET, EquipmentSlot.HEAD, 2, 2, 0, "sea_tramps_helmet", ObscureAPIAttributes.CRITICAL_HIT.get());
            add(list, Items.IRON_CHESTPLATE, EquipmentSlot.CHEST, 2, 6, 0, "dead_sea_chestplate", Attributes.ATTACK_DAMAGE);
            add(list, Items.IRON_CHESTPLATE, EquipmentSlot.CHEST, 2, 6, 0, "twilight_grotto_chestplate", ObscureAPIAttributes.PENETRATION.get());
            add(list, Items.IRON_CHESTPLATE, EquipmentSlot.CHEST, 2, 6, 0, "sea_tramps_chestplate", ObscureAPIAttributes.CRITICAL_HIT.get());
            add(list, Items.IRON_LEGGINGS, EquipmentSlot.LEGS, 2, 5, 0, "dead_sea_leggings", Attributes.ATTACK_DAMAGE);
            add(list, Items.IRON_LEGGINGS, EquipmentSlot.LEGS, 2, 5, 0, "twilight_grotto_leggings", ObscureAPIAttributes.PENETRATION.get());
            add(list, Items.IRON_LEGGINGS, EquipmentSlot.LEGS, 2, 5, 0, "sea_tramps_leggings", ObscureAPIAttributes.CRITICAL_HIT.get());
            add(list, Items.IRON_BOOTS, EquipmentSlot.FEET, 2, 2, 0, "dead_sea_boots", Attributes.ATTACK_DAMAGE);
            add(list, Items.IRON_BOOTS, EquipmentSlot.FEET, 2, 2, 0, "twilight_grotto_boots", ObscureAPIAttributes.PENETRATION.get());
            add(list, Items.IRON_BOOTS, EquipmentSlot.FEET, 2, 2, 0, "sea_tramps_boots", ObscureAPIAttributes.CRITICAL_HIT.get());
            return list;
        }

        public static void add(List<ItemStack> list, Item item, EquipmentSlot slot, int mod, int armor, int color, String name, Attribute attribute) {
            for (int i = 1; i <= 5; i++) {
                final ItemStack stack = new ItemStack(item);
                stack.addAttributeModifier(Attributes.ARMOR, new AttributeModifier("base_armor", armor, AttributeModifier.Operation.ADDITION), slot);
                stack.addAttributeModifier(attribute, new AttributeModifier("base_bonus", mod * i * 0.01, AttributeModifier.Operation.MULTIPLY_TOTAL), slot);
                stack.setHoverName(Component.translatable("set.aquamirae." + name));
                if (i == 5) stack.enchant(Enchantments.UNBREAKING, mod);
                if (color > 0) stack.getOrCreateTagElement("display").putInt("color", color);
                list.add(stack);
            }
        }

        public static int getColor(int r, int g, int b) {
            return r * 65536 + g * 256 + b;
        }
    }
}
