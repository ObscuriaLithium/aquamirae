
package com.obscuria.aquamirae.registry;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.common.items.Logbook;
import com.obscuria.obscureapi.registry.ObscureAPIEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class AquamiraeCreativeTab {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Aquamirae.MODID);

	public static final RegistryObject<CreativeModeTab> AQUAMIRAE_TAB = REGISTRY.register("aquamirae", () ->
			new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
					.title(Component.translatable("itemGroup." + Aquamirae.MODID))
					.icon(() -> AquamiraeItems.RUNE_OF_THE_STORM.get().getDefaultInstance())
					//.withTabsImage(new ResourceLocation(Aquamirae.MODID, "textures/gui/creative_tabs.png"))
					//.withBackgroundLocation(new ResourceLocation(Aquamirae.MODID, "textures/gui/creative_table.png"))
					//.withSlotColor(0x8000ccff)
					//.withLabelColor(0xffdddddd)
					.build());

	@SubscribeEvent
	public static void registerTab(@NotNull BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == AQUAMIRAE_TAB.get()) {
			event.accept(AquamiraeItems.RUNE_OF_THE_STORM.get());
			event.accept(AquamiraeItems.GOLDEN_MOTH_SPAWN_EGG.get());
			event.accept(AquamiraeItems.TERRIBLE_SWORD.get());
			event.accept(AquamiraeItems.FIN_CUTTER.get());
			event.accept(AquamiraeItems.TERRIBLE_HELMET.get());
			event.accept(AquamiraeItems.TERRIBLE_CHESTPLATE.get());
			event.accept(AquamiraeItems.TERRIBLE_LEGGINGS.get());
			event.accept(AquamiraeItems.TERRIBLE_BOOTS.get());
			event.accept(AquamiraeItems.SHIP_GRAVEYARD_ECHO.get());

			event.accept(AquamiraeItems.MAW_SPAWN_EGG.get());
			event.accept(AquamiraeItems.ANGLERFISH_SPAWN_EGG.get());
			event.accept(AquamiraeItems.DIVIDER.get());
			event.accept(AquamiraeItems.WHISPER_OF_THE_ABYSS.get());
			event.accept(AquamiraeItems.ABYSSAL_HEAUME.get());
			event.accept(AquamiraeItems.ABYSSAL_BRIGANTINE.get());
			event.accept(AquamiraeItems.ABYSSAL_LEGGINGS.get());
			event.accept(AquamiraeItems.ABYSSAL_BOOTS.get());
			event.accept(AquamiraeItems.ABYSSAL_TIARA.get());

			event.accept(AquamiraeItems.MAZE_MOTHER_SPAWN_EGG.get());
			event.accept(AquamiraeItems.CAPTAIN_CORNELIA_SPAWN_EGG.get());
			event.accept(AquamiraeItems.REMNANTS_SABER.get());
			event.accept(AquamiraeItems.POISONED_BLADE.get());
			event.accept(AquamiraeItems.THREE_BOLT_HELMET.get());
			event.accept(AquamiraeItems.THREE_BOLT_SUIT.get());
			event.accept(AquamiraeItems.THREE_BOLT_LEGGINGS.get());
			event.accept(AquamiraeItems.THREE_BOLT_BOOTS.get());
			event.accept(AquamiraeItems.DEAD_SEA_SCROLL.get());

			event.accept(AquamiraeItems.PILLAGERS_PATROL_SPAWN_EGG.get());
			event.accept(AquamiraeItems.TORTURED_SOUL_SPAWN_EGG.get());
			event.accept(AquamiraeItems.CORAL_LANCE.get().getDefaultInstance());
			event.accept(AquamiraeItems.DAGGER_OF_GREED.get());
			event.accept(AquamiraeItems.SHELL_HORN.get());
			event.accept(AquamiraeItems.FROZEN_KEY.get());
            event.accept(Logbook.PILLAGER_SHIP.get());
            event.accept(Logbook.PILLAGER_OUTPOST.get());
            event.accept(Logbook.PILLAGER_SHELTER.get());

			event.accept(AquamiraeItems.EEL_SPAWN_EGG.get());
			event.accept(AquamiraeItems.SPINEFISH_SPAWN_EGG.get());
			if (Aquamirae.winterEvent()) event.accept(AquamiraeItems.SWEET_LANCE.get().getDefaultInstance());
			poisonedChakra().forEach(event::accept);
			mazeRose().forEach(event::accept);

			event.accept(AquamiraeItems.PIRATE_POUCH.get());
			event.accept(AquamiraeItems.TREASURE_POUCH.get());
			event.accept(AquamiraeItems.MUSIC_DISC_HORIZON.get());
			event.accept(AquamiraeItems.MUSIC_DISC_FORSAKEN_DROWNAGE.get());
			event.accept(AquamiraeItems.FIN.get());
			event.accept(AquamiraeItems.ESCA.get());
			event.accept(AquamiraeItems.ANGLERS_FANG.get());
			event.accept(AquamiraeItems.ABYSSAL_AMETHYST.get());
			event.accept(AquamiraeItems.SHARP_BONES.get());
			event.accept(AquamiraeItems.PAINTING_ANGLERFISH.get());
			event.accept(AquamiraeItems.PAINTING_OXYGELIUM.get());
			event.accept(AquamiraeItems.PAINTING_TORTURED_SOUL.get());
			event.accept(AquamiraeItems.PAINTING_AURORA.get());
			event.accept(AquamiraeItems.GOLDEN_MOTH_IN_A_JAR.get());
			event.accept(AquamiraeItems.OXYGELIUM.get());
			event.accept(AquamiraeItems.WISTERIA_NIVEIS.get());
			event.accept(AquamiraeItems.OXYGEN_TANK.get());
			event.accept(AquamiraeItems.FROZEN_CHEST.get());
			event.accept(AquamiraeItems.LUMINESCENT_BUBBLE.get());
			event.accept(AquamiraeItems.LUMINESCENT_LAMP.get());
			event.accept(AquamiraeItems.SEA_CASSEROLE.get());
			event.accept(AquamiraeItems.SPINEFISH_BUCKET.get());
			event.accept(AquamiraeItems.SPINEFISH.get());
			event.accept(AquamiraeItems.COOKED_SPINEFISH.get());
			event.accept(AquamiraeItems.SEA_STEW.get());
			event.accept(AquamiraeItems.POSEIDONS_BREAKFAST.get());
			event.accept(AquamiraeItems.ELODEA.get());
		}
	}

	public static @NotNull List<ItemStack> poisonedChakra() {
		final List<ItemStack> list = new ArrayList<>();
		final ItemStack stack1 = AquamiraeItems.POISONED_CHAKRA.get().getDefaultInstance();
		final ItemStack stack2 = AquamiraeItems.POISONED_CHAKRA.get().getDefaultInstance();
		stack1.enchant(ObscureAPIEnchantments.DISTANCE.get(), ObscureAPIEnchantments.DISTANCE.get().getMaxLevel());
		stack1.enchant(ObscureAPIEnchantments.FAST_SPIN.get(), ObscureAPIEnchantments.FAST_SPIN.get().getMaxLevel());
		stack2.enchant(ObscureAPIEnchantments.MIRROR.get(), ObscureAPIEnchantments.MIRROR.get().getMaxLevel());
		list.add(stack1); list.add(stack2);
		return list;
	}

	public static @NotNull List<ItemStack> mazeRose() {
		final List<ItemStack> list = new ArrayList<>();
		final ItemStack stack1 = AquamiraeItems.MAZE_ROSE.get().getDefaultInstance();
		final ItemStack stack2 = AquamiraeItems.MAZE_ROSE.get().getDefaultInstance();
		stack1.enchant(ObscureAPIEnchantments.DISTANCE.get(), ObscureAPIEnchantments.DISTANCE.get().getMaxLevel());
		stack1.enchant(ObscureAPIEnchantments.FAST_SPIN.get(), ObscureAPIEnchantments.FAST_SPIN.get().getMaxLevel());
		stack2.enchant(ObscureAPIEnchantments.MIRROR.get(), ObscureAPIEnchantments.MIRROR.get().getMaxLevel());
		list.add(stack1); list.add(stack2);
		return list;
	}
}
