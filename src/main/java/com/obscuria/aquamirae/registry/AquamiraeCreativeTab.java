
package com.obscuria.aquamirae.registry;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.obscureapi.registry.ObscureAPIEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
			logBooks().forEach(event::accept);

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

			//Aquamirae.SetBuilder.common().forEach(event::accept);
			//Aquamirae.SetBuilder.rare().forEach(event::accept);
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

	public static @NotNull List<ItemStack> logBooks() {
		final List<ItemStack> list = new ArrayList<>();
		final ItemStack book1 = Items.WRITTEN_BOOK.getDefaultInstance();
		final ItemStack book2 = Items.WRITTEN_BOOK.getDefaultInstance();
		final ItemStack book3 = Items.WRITTEN_BOOK.getDefaultInstance();
		final ListTag pages1 = new ListTag();
		final ListTag pages2 = new ListTag();
		final ListTag pages3 = new ListTag();

		pages1.add(StringTag.valueOf("{\"text\":\"Entry #12\n\nLast afternoon, Captain Cornelia went to the seabed in search of any additional resources. There are rumors among the crew that the real subject of her expedition is something else.\"}"));
		pages1.add(StringTag.valueOf("{\"text\":\"More than a day has passed since the captain's dive. She was wearing the only diving suit we had, so we couldn't help her in any way. Worst of all, she had the master key with her. As our hopes of rescue dwindle, the threat of rebellion grows.\"}"));
		book1.getOrCreateTag().putString("title", "Pillagers Ship Logbook");
		book1.getOrCreateTag().putString("author", "Unknown");
		book1.getOrCreateTag().putInt("generation", 3);
		book1.getOrCreateTag().putBoolean("resolved", true);
		book1.getOrCreateTag().put("pages", pages1);

		pages2.add(StringTag.valueOf("{\"text\":\"Entry #34\n\nThese beasts from the depths prowl for someone to devour... One of those critters has already eaten our midshipman... Swallowed him whole along with his crossbow.\"}"));
		pages2.add(StringTag.valueOf("{\"text\":\"Entry #35\n\nWe are waiting in vain for rescue... No one will come to help us, we have to survive on our own. Today the construction of the fort from parts of the ship was completed. In any case, she could no longer plow the seas ever again.\"}"));
		pages2.add(StringTag.valueOf("{\"extra\":[{\"text\":\"Entry #36\n\nWe are no longer able to heat the fort, resources are running out. \"},{\"color\":\"dark_green\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"extra\":[{\"color\":\"green\",\"text\":\"Underground shelter\"},{\"text\":\"\n  There are more structures\n  underneath the ice... Looks\n  like I have to go down to\n  these terrible fish...\"}],\"text\":\"\"}},\"text\":\"Part of the crew began to dig a shelter underground\"},{\"text\":\". It is the only way we have a chance not to freeze to death.\"}],\"text\":\"\"}"));
		pages2.add(StringTag.valueOf("{\"text\":\"Entry #37\n\nWe hid our supplies and valuable cargo underground. But Poseidon cursed us, and during the storm the lieutenant washed away into the sea... Along with master keys.\"}"));
		pages2.add(StringTag.valueOf("{\"extra\":[{\"text\":\"The hope remains that we could possibly find \"},{\"color\":\"dark_green\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"extra\":[{\"color\":\"green\",\"text\":\"Cargo keys\"},{\"text\":\"\n  Without the keys, I will not\n  be able to receive the\n  most valuable cargo. I need\n  to find other ships nearby.\"}],\"text\":\"\"}},\"text\":\"similar keys on other ships of our fleet\"},{\"text\":\", which have suffered the same fate as us.\"}],\"text\":\"\"}"));
		book2.getOrCreateTag().putString("title", "Pillagers Outpost Logbook");
		book2.getOrCreateTag().putString("author", "Unknown");
		book2.getOrCreateTag().putInt("generation", 3);
		book2.getOrCreateTag().putBoolean("resolved", true);
		book2.getOrCreateTag().put("pages", pages2);

		pages3.add(StringTag.valueOf("{\"text\":\"Entry #41\n\nIn these cursed lands there you can't find shelter even under the ice and the thickness of the earth... Some creatures that look like eels crawled out of the depths of the underworld, depriving us of the opportunity \"}"));
		pages3.add(StringTag.valueOf("{\"text\":\"to return back to the surface.\n\nOnly the mistress of the moon knows how long shall we last... At all events, our hopes of seeing sunlight had all but vanished. This place will become our grave, our eternal tomb. For us and that damned rune...\"}"));
		pages3.add(StringTag.valueOf("{\"text\":\"Last Entry\n\nIt whispers to me, whispers inside my head the secrets of ice and snow... Ice... Now we are chained in ice forever...\n\nI suppose a similar fate awaits the crew that set out in search of the fire rune... I wonder if they made it\"}"));
		pages3.add(StringTag.valueOf("{\"text\":\"to the Great Dark Forest valley. Though it doesn't really matter now...\"}"));
		book3.getOrCreateTag().putString("title", "Pillagers Shelter Logbook");
		book3.getOrCreateTag().putString("author", "Unknown");
		book3.getOrCreateTag().putInt("generation", 3);
		book3.getOrCreateTag().putBoolean("resolved", true);
		book3.getOrCreateTag().put("pages", pages3);

		list.add(book1); list.add(book2); list.add(book3);
		return list;
	}
}
