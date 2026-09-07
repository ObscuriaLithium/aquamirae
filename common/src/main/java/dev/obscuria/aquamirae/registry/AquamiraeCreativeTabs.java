package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.component.LogBookContents;
import dev.obscuria.fragmentum.content.registry.Deferred;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class AquamiraeCreativeTabs {

    public static final Deferred<CreativeModeTab, CreativeModeTab> AQUAMIRAE = register("aquamirae",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + Aquamirae.MOD_ID))
                    .icon(AquamiraeItems.RUNE_OF_THE_STORM::instantiate)
                    .displayItems(AquamiraeCreativeTabs::fillAquamiraeTab)
                    .build());

    private static Deferred<CreativeModeTab, CreativeModeTab> register(String name, Supplier<CreativeModeTab> supplier) {
        return AquamiraeRegistries.REGISTRAR.register(Registries.CREATIVE_MODE_TAB, Aquamirae.id(name), supplier);
    }

    private static void fillAquamiraeTab(
            CreativeModeTab.ItemDisplayParameters parameters,
            CreativeModeTab.Output output
    ) {
        output.accept(AquamiraeItems.RUNE_OF_THE_STORM);
        output.accept(AquamiraeItems.GOLDEN_MOTH_SPAWN_EGG);
        output.accept(AquamiraeItems.REMNANT_SABER);
        output.accept(AquamiraeItems.TERRIBLE_BLADE);
        output.accept(AquamiraeItems.SHIP_GRAVEYARD_ECHO);
        output.accept(AquamiraeItems.PIRATE_POUCH);
        output.accept(AquamiraeItems.TREASURE_POUCH);
        output.accept(AquamiraeItems.ECHO_COMPASS);
        output.accept(AquamiraeItems.CRACKED_RECORD);

        output.accept(AquamiraeItems.MAW_SPAWN_EGG);
        output.accept(AquamiraeItems.ANGLERFISH_SPAWN_EGG);
        output.accept(AquamiraeItems.TERRIBLE_CLEAVER);
        output.accept(AquamiraeItems.TERRIBLE_FANG);
        output.accept(AquamiraeItems.TERRIBLE_UPGRADE_SMITHING_TEMPLATE);
        output.accept(AquamiraeItems.TERRIBLE_HELMET);
        output.accept(AquamiraeItems.TERRIBLE_CHESTPLATE);
        output.accept(AquamiraeItems.TERRIBLE_LEGGINGS);
        output.accept(AquamiraeItems.TERRIBLE_BOOTS);

        output.accept(AquamiraeItems.MAZE_MOTHER_SPAWN_EGG);
        output.accept(AquamiraeItems.ABYSSAL_SCYPHOID_SPAWN_EGG);
        output.accept(AquamiraeItems.SHATTERBLADE);
        output.accept(AquamiraeItems.SHATTERSPIKE);
        output.accept(AquamiraeItems.ABYSSAL_UPGRADE_SMITHING_TEMPLATE);
        output.accept(AquamiraeItems.ABYSSAL_HEAUME);
        output.accept(AquamiraeItems.ABYSSAL_BRIGANDINE);
        output.accept(AquamiraeItems.ABYSSAL_LEGGINGS);
        output.accept(AquamiraeItems.ABYSSAL_BOOTS);

        output.accept(AquamiraeItems.CAPTAIN_CORNELIA_SPAWN_EGG);
        output.accept(AquamiraeItems.EEL_SPAWN_EGG);
        output.accept(AquamiraeItems.DAGGER_OF_GREED);
        output.accept(AquamiraeItems.TIDEPIERCER);
        output.accept(AquamiraeItems.SHELL_HORN);
        output.accept(AquamiraeItems.SALVAGER_HELMET);
        output.accept(AquamiraeItems.SALVAGER_SUIT);
        output.accept(AquamiraeItems.SALVAGER_WADERS);
        output.accept(AquamiraeItems.SALVAGER_BOOTS);

        output.accept(AquamiraeItems.TORTURED_SOUL_SPAWN_EGG);
        output.accept(AquamiraeItems.SPINEFISH_SPAWN_EGG);
        output.accept(AquamiraeItems.DREADWAKE);
        output.accept(AquamiraeItems.TERRIBLE_CHAKRAM);
        output.accept(AquamiraeItems.MAZE_ROSE);
        output.accept(logBook("pirate_ship"));
        output.accept(logBook("pirate_outpost"));
        output.accept(logBook("pirate_shelter"));
        output.accept(AquamiraeItems.ABYSSAL_TIARA);

        output.accept(AquamiraeItems.SCUTTLER_SPAWN_EGG);
        output.accept(AquamiraeItems.SHELLBACK_SPAWN_EGG);
        output.accept(AquamiraeItems.FROZEN_KEY);
        output.accept(AquamiraeItems.WISTERIA_NIVEIS);
        output.accept(AquamiraeItems.OXYHELIUM);
        output.accept(AquamiraeItems.MAELSTROM_VESTIGE);
        output.accept(AquamiraeItems.ABYSSAL_AMETHYST);
        output.accept(AquamiraeItems.ANGLER_FANG);
        output.accept(AquamiraeItems.FIN);
        output.accept(AquamiraeItems.ESCA);
        output.accept(AquamiraeItems.SPINEFISH);
        output.accept(AquamiraeItems.COOKED_SPINEFISH);
        output.accept(AquamiraeItems.SHARP_BONES);
        output.accept(AquamiraeItems.JELLYFISH_JELLY);
        output.accept(AquamiraeItems.VOID_SQUID);
        output.accept(AquamiraeItems.SEA_CASSEROLE);
        output.accept(AquamiraeItems.ESCA_SKEWER);
        output.accept(AquamiraeItems.SEA_STEW);
        output.accept(AquamiraeItems.POSEIDON_BREAKFAST);
        output.accept(AquamiraeItems.MUSIC_DISC_HORIZON);
        output.accept(AquamiraeItems.MUSIC_DISC_FORSAKEN_DROWNAGE);
        output.accept(AquamiraeItems.FROZEN_CHEST);
        output.accept(AquamiraeItems.GOLDEN_MOTH_JAR);
        output.accept(AquamiraeItems.LUMINESCENT_LAMP);
        output.accept(AquamiraeItems.LUMINESCENT_BUBBLE);
        output.accept(AquamiraeItems.PAINTING_ANGLERFISH);
        output.accept(AquamiraeItems.PAINTING_OXYHELIUM);
        output.accept(AquamiraeItems.PAINTING_TORTURED_SOUL);
        output.accept(AquamiraeItems.PAINTING_AURORA);
    }

    public static ItemStack logBook(String type) {
        var logbook = AquamiraeItems.LOGBOOK.instantiate();
        logbook.set(AquamiraeDataComponents.LOGBOOK_CONTENTS, new LogBookContents(
                Component.translatable("logbook.aquamirae.%s.topic".formatted(type)),
                Component.translatable("logbook.aquamirae.%s.content".formatted(type))));
        return logbook;
    }

    static void init() {}
}
