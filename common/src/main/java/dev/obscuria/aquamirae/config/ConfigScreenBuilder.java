package dev.obscuria.aquamirae.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.common.item.ShellHornItem;
import dev.obscuria.aquamirae.config.early.EarlyConfig;
import dev.obscuria.aquamirae.config.early.EarlyConfigManager;
import dev.obscuria.aquamirae.config.early.EarlyConfigValue;
import dev.obscuria.aquamirae.server.spawner.BiomeSpawnerMode;
import dev.obscuria.fragmentum.config.ConfigValue;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class ConfigScreenBuilder {

    private static final ResourceLocation IMAGE_ICE_MAZE = Aquamirae.id("textures/gui/config/ice_maze.webp");
    private static final ResourceLocation IMAGE_PIRATE_SHIP = Aquamirae.id("textures/gui/config/structure/pirate_ship.webp");
    private static final ResourceLocation IMAGE_PIRATE_OUTPOST = Aquamirae.id("textures/gui/config/structure/pirate_outpost.webp");
    private static final ResourceLocation IMAGE_PIRATE_SHELTER = Aquamirae.id("textures/gui/config/structure/pirate_shelter.webp");

    private static final String NAME_ENABLE_SPAWNING = "enable_spawning";
    private static final String NAME_SPAWN_WEIGHT = "spawn_weight";
    private static final String NAME_SPAWN_MIN_COUNT = "spawn_min_count";
    private static final String NAME_SPAWN_MAX_COUNT = "spawn_max_count";
    private static final String NAME_PLACEMENT_SPACING = "placement_spacing";
    private static final String NAME_PLACEMENT_SEPARATION = "placement_separation";

    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Aquamirae Options"))
                .category(categoryBuilder("world")
                        .option(Opts.biomeIdList(CommonConfig.ICE_MAZE_BIOMES, "#aquamirae:ice_maze").webpImage(IMAGE_ICE_MAZE).worldReloadHint().build())
                        .group(groupBuilder("pirate_ship", IMAGE_PIRATE_SHIP)
                                .option(Opts.intField(CommonConfig.PIRATE_SHIP_PLACEMENT_SPACING, 1, 512).key(NAME_PLACEMENT_SPACING).anyReloadHint().build())
                                .option(Opts.intField(CommonConfig.PIRATE_SHIP_PLACEMENT_SEPARATION, 1, 64).key(NAME_PLACEMENT_SEPARATION).anyReloadHint().build())
                                .build())
                        .group(groupBuilder("pirate_outpost", IMAGE_PIRATE_OUTPOST)
                                .option(Opts.intField(CommonConfig.PIRATE_OUTPOST_PLACEMENT_SPACING, 1, 512).key(NAME_PLACEMENT_SPACING).anyReloadHint().build())
                                .option(Opts.intField(CommonConfig.PIRATE_OUTPOST_PLACEMENT_SEPARATION, 1, 64).key(NAME_PLACEMENT_SEPARATION).anyReloadHint().build())
                                .build())
                        .group(groupBuilder("pirate_shelter", IMAGE_PIRATE_SHELTER)
                                .option(Opts.intField(CommonConfig.PIRATE_SHELTER_PLACEMENT_SPACING, 1, 512).key(NAME_PLACEMENT_SPACING).anyReloadHint().build())
                                .option(Opts.intField(CommonConfig.PIRATE_SHELTER_PLACEMENT_SEPARATION, 1, 64).key(NAME_PLACEMENT_SEPARATION).anyReloadHint().build())
                                .build())
                        .build())
                .category(categoryBuilder("spawning")
                        .option(Opts.enumCycle(CommonConfig.BIOME_SPAWNER_MODE, BiomeSpawnerMode.class).build())
                        .group(groupBuilderLiteral("entity.aquamirae.scuttler")
                                .option(Opts.bool(CommonConfig.ENABLE_SCUTTLER_SPAWNING).key(NAME_ENABLE_SPAWNING).build())
                                .option(Opts.intField(CommonConfig.SCUTTLER_SPAWN_WEIGHT, 1, 1000).key(NAME_SPAWN_WEIGHT).build())
                                .option(Opts.intField(CommonConfig.SCUTTLER_SPAWN_MIN_COUNT, 1, 10).key(NAME_SPAWN_MIN_COUNT).build())
                                .option(Opts.intField(CommonConfig.SCUTTLER_SPAWN_MAX_COUNT, 1, 10).key(NAME_SPAWN_MAX_COUNT).build())
                                .build())
                        .group(groupBuilderLiteral("entity.aquamirae.maw")
                                .option(Opts.bool(CommonConfig.ENABLE_MAW_SPAWNING).key(NAME_ENABLE_SPAWNING).build())
                                .option(Opts.intField(CommonConfig.MAW_SPAWN_WEIGHT, 1, 1000).key(NAME_SPAWN_WEIGHT).build())
                                .option(Opts.intField(CommonConfig.MAW_SPAWN_MIN_COUNT, 1, 10).key(NAME_SPAWN_MIN_COUNT).build())
                                .option(Opts.intField(CommonConfig.MAW_SPAWN_MAX_COUNT, 1, 10).key(NAME_SPAWN_MAX_COUNT).build())
                                .build())
                        .group(groupBuilderLiteral("entity.aquamirae.anglerfish")
                                .option(Opts.bool(CommonConfig.ENABLE_ANGLERFISH_SPAWNING).key(NAME_ENABLE_SPAWNING).build())
                                .option(Opts.intField(CommonConfig.ANGLERFISH_SPAWN_WEIGHT, 1, 1000).key(NAME_SPAWN_WEIGHT).build())
                                .option(Opts.intField(CommonConfig.ANGLERFISH_SPAWN_MIN_COUNT, 1, 10).key(NAME_SPAWN_MIN_COUNT).build())
                                .option(Opts.intField(CommonConfig.ANGLERFISH_SPAWN_MAX_COUNT, 1, 10).key(NAME_SPAWN_MAX_COUNT).build())
                                .build())
                        .group(groupBuilderLiteral("entity.aquamirae.spinefish")
                                .option(Opts.bool(CommonConfig.ENABLE_SPINEFISH_SPAWNING).key(NAME_ENABLE_SPAWNING).build())
                                .option(Opts.intField(CommonConfig.SPINEFISH_SPAWN_WEIGHT, 1, 1000).key(NAME_SPAWN_WEIGHT).build())
                                .option(Opts.intField(CommonConfig.SPINEFISH_SPAWN_MIN_COUNT, 1, 10).key(NAME_SPAWN_MIN_COUNT).build())
                                .option(Opts.intField(CommonConfig.SPINEFISH_SPAWN_MAX_COUNT, 1, 10).key(NAME_SPAWN_MAX_COUNT).build())
                                .build())
                        .group(groupBuilderLiteral("entity.aquamirae.abyssal_scyphoid")
                                .option(Opts.bool(CommonConfig.ENABLE_ABYSSAL_SCYPHOID_SPAWNING).key(NAME_ENABLE_SPAWNING).build())
                                .option(Opts.intField(CommonConfig.ABYSSAL_SCYPHOID_SPAWN_WEIGHT, 1, 1000).key(NAME_SPAWN_WEIGHT).build())
                                .option(Opts.intField(CommonConfig.ABYSSAL_SCYPHOID_SPAWN_MIN_COUNT, 1, 10).key(NAME_SPAWN_MIN_COUNT).build())
                                .option(Opts.intField(CommonConfig.ABYSSAL_SCYPHOID_SPAWN_MAX_COUNT, 1, 10).key(NAME_SPAWN_MAX_COUNT).build())
                                .build())
                        .group(groupBuilderLiteral("entity.aquamirae.maze_mother")
                                .option(Opts.bool(CommonConfig.ENABLE_MAZE_MOTHER_SPAWNING).key(NAME_ENABLE_SPAWNING).build())
                                .option(Opts.doubleSlider(CommonConfig.MAZE_MOTHER_SPAWN_CHANCE, 0, 1, 0.001, ValueFormat.PERCENT).build())
                                .build())
                        .group(groupBuilderLiteral("entity.aquamirae.shipbreaker")
                                .option(Opts.bool(CommonConfig.ENABLE_SHIPBREAKER_SPAWNING).key(NAME_ENABLE_SPAWNING).build())
                                .option(Opts.doubleSlider(CommonConfig.SHIPBREAKER_SPAWN_COOLDOWN, 1, 300, 0.5, ValueFormat.MINUTES).build())
                                .option(Opts.doubleSlider(CommonConfig.SHIPBREAKER_SPAWN_COOLDOWN_VARIANCE, 0, 300, 0.5, ValueFormat.MINUTES).build())
                                .option(Opts.doubleSlider(CommonConfig.SHIPBREAKER_SPAWN_RETRY_DELAY, 1, 60, 0.5, ValueFormat.MINUTES).build())
                                .build())
                        .build())
                .category(categoryBuilder("items")
                        .group(groupBuilderLiteral("item.aquamirae.shell_horn")
                                .option(Opts.enumCycle(CommonConfig.SHELL_HORN_USAGE_MODE, ShellHornItem.UsageMode.class).build())
                                .option(Opts.intSlider(CommonConfig.SHELL_HORN_SEARCH_RADIUS, 6, 64, 1, ValueFormat.BLOCKS).build())
                                .option(Opts.intSlider(CommonConfig.SHELL_HORN_SPAWN_OFFSET, -16, 0, 1, ValueFormat.BLOCKS).build())
                                .option(Opts.intSlider(CommonConfig.SHELL_HORN_SUMMON_DELAY, 0, 60, 1, ValueFormat.SECONDS).build())
                                .option(Opts.intSlider(CommonConfig.SHELL_HORN_COOLDOWN, 0, 60, 1, ValueFormat.SECONDS).build())
                                .build())
                        .group(groupBuilderLiteral("item.aquamirae.remnant_saber")
                                .option(Opts.doubleSlider(CommonConfig.REMNANT_SABER_BASE_BONUS, 0, 1, 0.01, ValueFormat.PERCENT).build())
                                .option(Opts.doubleSlider(CommonConfig.REMNANT_SABER_PER_KILL_BONUS, 0, 0.01, 0.0001, ValueFormat.PERCENT).build())
                                .option(Opts.doubleSlider(CommonConfig.REMNANT_SABER_MAX_BONUS, 0, 10, 0.1, ValueFormat.PERCENT).build())
                                .build())
                        .group(groupBuilderLiteral("item.aquamirae.terrible_blade")
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_BLADE_POISON_AMPLIFIER, 0, 9, 1, ValueFormat.POTENCY).build())
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_BLADE_POISON_DURATION, 0, 60, 1, ValueFormat.SECONDS).build())
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_BLADE_REGENERATION_AMPLIFIER, 0, 9, 1, ValueFormat.POTENCY).build())
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_BLADE_REGENERATION_DURATION, 0, 60, 1, ValueFormat.SECONDS).build())
                                .build())
                        .group(groupBuilderLiteral("item.aquamirae.terrible_cleaver")
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_CLEAVER_MIN_BONUS_DAMAGE, 0, 1, 0.01, ValueFormat.PERCENT).build())
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_CLEAVER_MAX_BONUS_DAMAGE, 0, 1, 0.01, ValueFormat.PERCENT).build())
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_CLEAVER_CRIT_CHANCE, 0, 1, 0.01, ValueFormat.PERCENT).build())
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_CLEAVER_MIN_CRIT_BONUS, 0, 3, 0.01, ValueFormat.PERCENT).build())
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_CLEAVER_MAX_CRIT_BONUS, 0, 3, 0.01, ValueFormat.PERCENT).build())
                                .build())
                        .group(groupBuilderLiteral("item.aquamirae.terrible_fang")
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_FANG_PER_HEART_BONUS, 0, 3, 0.01, ValueFormat.PERCENT).build())
                                .option(Opts.doubleSlider(CommonConfig.TERRIBLE_FANG_MAX_BONUS, 0, 3, 0.01, ValueFormat.PERCENT).build())
                                .build())
                        .build())
                .category(categoryBuilder("attributes")
                        .group(Opts.attributes(CommonConfig.SCUTTLER_ATTRIBUTES).name(Component.translatable("entity.aquamirae.scuttler")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.SHELLBACK_ATTRIBUTES).name(Component.translatable("entity.aquamirae.shellback")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.GOLDEN_MOTH_ATTRIBUTES).name(Component.translatable("entity.aquamirae.golden_moth")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.SPINEFISH_ATTRIBUTES).name(Component.translatable("entity.aquamirae.spinefish")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.MAW_ATTRIBUTES).name(Component.translatable("entity.aquamirae.maw")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.ANGLERFISH_ATTRIBUTES).name(Component.translatable("entity.aquamirae.anglerfish")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.EEL_ATTRIBUTES).name(Component.translatable("entity.aquamirae.eel")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.TORTURED_SOUL_ATTRIBUTES).name(Component.translatable("entity.aquamirae.tortured_soul")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.MAZE_MOTHER_ATTRIBUTES).name(Component.translatable("entity.aquamirae.maze_mother")).desc("attributes").build())
                        .group(Opts.attributes(CommonConfig.CAPTAIN_CORNELIA_ATTRIBUTES).name(Component.translatable("entity.aquamirae.captain_cornelia")).desc("attributes").build())
                        .build())
                .category(categoryBuilder("blizzard")
                        .option(Opts.bool(ClientConfig.BLIZZARD_CUSTOM_FOG).build())
                        .option(Opts.doubleSlider(ClientConfig.BLIZZARD_FOG_START, 0, 256, 1, ValueFormat.RAW).build())
                        .option(Opts.doubleSlider(ClientConfig.BLIZZARD_FOG_END, 0, 256, 1, ValueFormat.RAW).build())
                        .option(Opts.bool(ClientConfig.BLIZZARD_CUSTOM_PARTICLES).build())
                        .option(Opts.doubleSlider(ClientConfig.BLIZZARD_WIND_OFFSET, 0, 100, 1, ValueFormat.RAW).build())
                        .option(Opts.doubleSlider(ClientConfig.BLIZZARD_SPEED_MULTIPLIER, 0, 100, 1, ValueFormat.RAW).build())
                        .build())
                .category(categoryBuilder("extras")
                        .option(Opts.missingConfigHint())
                        .option(Opts.bool(EarlyConfig.THEMED_MAIN_MENU).build())
                        .option(Opts.bool(CommonConfig.ENABLE_SHIPBREAKER_GRIEFING).build())
                        .build())
                .save(ConfigScreenBuilder::saveAll)
                .build().generateScreen(parent);
    }

    private static void saveAll() {
        ClientConfig.VALUES.forEach(ConfigValue::save);
        CommonConfig.VALUES.forEach(ConfigValue::save);
        EarlyConfigManager.SHARED.save();
    }

    private static ConfigCategory.Builder categoryBuilder(String key) {
        return ConfigCategory.createBuilder().name(translate("category." + key));
    }

    private static ConfigCategory.Builder categoryBuilderLiteral(String key) {
        return ConfigCategory.createBuilder().name(Component.translatable(key));
    }

    private static OptionGroup.Builder groupBuilder(String key) {
        return OptionGroup.createBuilder().name(translate("group." + key));
    }

    private static OptionGroup.Builder groupBuilder(String key, ResourceLocation image) {
        return groupBuilder(key).description(OptionDescription.createBuilder()
                .text(translate("group." + key + ".desc").withStyle(ChatFormatting.GRAY))
                .webpImage(image)
                .build());
    }

    private static OptionGroup.Builder groupBuilderLiteral(String key) {
        return OptionGroup.createBuilder().name(Component.translatable(key));
    }

    static MutableComponent translate(String key) {
        return Component.translatable("config.aquamirae." + key);
    }

    static MutableComponent translate(String key, Object... args) {
        return Component.translatable("config.aquamirae." + key, args);
    }

    public static final class Opts {

        private Opts() {}

        public static OptionDescription descriptionOf(Component text) {
            return OptionDescription.of(text.copy().withStyle(ChatFormatting.GRAY));
        }

        public static <T> OptionSpec<T> opt(ConfigValue<T> value) {
            return new OptionSpec<>(new ConfigValueAdapter<>(value));
        }

        public static <T> OptionSpec<T> opt(EarlyConfigValue<T> value) {
            return new OptionSpec<>(new EarlyValueAdapter<>(value));
        }

        public static OptionSpec<Boolean> bool(ConfigValue<Boolean> value) {
            return opt(value).controller(TickBoxControllerBuilder::create);
        }

        public static OptionSpec<Boolean> bool(EarlyConfigValue<Boolean> value) {
            return opt(value).controller(TickBoxControllerBuilder::create);
        }

        public static <E extends Enum<E>> OptionSpec<E> enumCycle(ConfigValue<E> value, Class<E> enumClass) {
            return opt(value).controller(o -> EnumControllerBuilder.create(o).enumClass(enumClass));
        }

        public static OptionSpec<Double> doubleSlider(ConfigValue<Double> value, double min, double max, double step, ValueFormat format) {
            return opt(value).controller(o -> DoubleSliderControllerBuilder.create(o)
                    .range(min, max)
                    .step(step)
                    .formatValue(format.formatter));
        }

        public static OptionSpec<Integer> intSlider(ConfigValue<Integer> value, int min, int max, int step, ValueFormat format) {
            return opt(value).controller(o -> IntegerSliderControllerBuilder.create(o)
                    .range(min, max)
                    .step(step)
                    .formatValue(v -> format.formatter.format(v.doubleValue())));
        }

        public static OptionSpec<Integer> intField(ConfigValue<Integer> value, int min, int max) {
            return opt(value).controller(o -> IntegerFieldControllerBuilder.create(o).range(min, max));
        }

        public static OptionSpec<Double> doubleField(ConfigValue<Double> value, double min, double max) {
            return opt(value).controller(o -> DoubleFieldControllerBuilder.create(o).range(min, max));
        }

        public static OptionSpec<String> string(ConfigValue<String> value) {
            return opt(value).controller(StringControllerBuilder::create);
        }

        public static OptionSpec<String> easing(ConfigValue<Easing> value) {
            return new OptionSpec<>(new EasingValueAdapter(value))
                    .controller(o -> DropdownStringControllerBuilder.create(o)
                            .values(Arrays.stream(Easing.values()).map(Easing::name).toList()));
        }

        public static OptionSpec<String> sound(ConfigValue<String> value) {
            return opt(value).controller(o -> DropdownStringControllerBuilder.create(o)
                    .values(BuiltInRegistries.SOUND_EVENT.stream()
                            .map(SoundEvent::getLocation)
                            .map(ResourceLocation::toString)
                            .toList()));
        }

        public static ListOptionSpec<String> stringList(ConfigValue<List<? extends String>> value, String initialEntry) {
            return listOpt(value).controller(StringControllerBuilder::create).initial(initialEntry);
        }

        public static ListOptionSpec<String> attributes(ConfigValue<List<? extends String>> value) {
            return listOpt(value)
                    .key("attributes")
                    .controller(AttributeController.Factory::create)
                    .initial(AttributeController.DEFAULT_ENTRY);
        }

        public static ListOptionSpec<String> biomeIdList(ConfigValue<List<? extends String>> value, String exclude) {
            try {
                @Nullable var level = Minecraft.getInstance().level;
                if (level == null) return stringList(value, "");
                var biomeLookup = level.registryAccess().lookupOrThrow(Registries.BIOME);
                return dropdownList(value, () -> Stream.concat(
                        biomeLookup.listTagIds()
                                .map(TagKey::location)
                                .map(ResourceLocation::toString)
                                .map("#%s"::formatted)
                                .filter(Predicate.not(exclude::equals))
                                .sorted(),
                        biomeLookup.listElements()
                                .map(Holder.Reference::key)
                                .map(ResourceKey::location)
                                .map(ResourceLocation::toString)
                                .sorted()
                ).toList());
            } catch (Exception ignored) {
                return stringList(value, "");
            }
        }

        public static ListOptionSpec<String> itemIdList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.keySet().stream()
                    .map(ResourceLocation::toString)
                    .sorted()
                    .toList());
        }

        public static ListOptionSpec<String> itemTagList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.getTagNames()
                    .map(TagKey::location)
                    .map(ResourceLocation::toString)
                    .sorted()
                    .toList());
        }

        public static ListOptionSpec<String> modIdList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.keySet().stream()
                    .map(ResourceLocation::getNamespace)
                    .distinct()
                    .sorted()
                    .toList());
        }

        private static ListOptionSpec<String> dropdownList(ConfigValue<List<? extends String>> value, Supplier<List<String>> valuesSupplier) {
            return listOpt(value)
                    .controller(o -> DropdownStringControllerBuilder.create(o)
                            .values(valuesSupplier.get())
                            .allowAnyValue(false)
                            .allowEmptyValue(false))
                    .initial("");
        }

        public static ButtonOption innerScreen(String key, Consumer<ConfigCategory.Builder> builder) {
            return ButtonOption.createBuilder()
                    .name(Component.translatable(key))
                    .text(translate("button.configure"))
                    .action((screen, option) -> {
                        var categoryBuilder = categoryBuilderLiteral(key);
                        builder.accept(categoryBuilder);
                        Minecraft.getInstance().setScreen(YetAnotherConfigLib.createBuilder()
                                .title(Component.translatable(key))
                                .category(categoryBuilder.build())
                                .save(ConfigScreenBuilder::saveAll)
                                .build().generateScreen(screen));
                    }).build();
        }

        public static ButtonOption screenButton(String key, Function<Screen, Screen> screenFactory) {
            return ButtonOption.createBuilder()
                    .name(translate("option." + key))
                    .text(translate("option." + key + ".button"))
                    .description(descriptionOf(translate("option." + key + ".desc")))
                    .action((screen, option) -> Minecraft.getInstance().setScreen(screenFactory.apply(screen)))
                    .build();
        }

        public static LabelOption missingConfigHint() {
            return LabelOption.create(translate("label.missing_config_hint",
                    Component.literal("Obscuria Collection").withStyle(style -> style
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://dsc.gg/obscuria"))
                            .withColor(ChatFormatting.BLUE)
                            .withUnderlined(true))
            ).withStyle(ChatFormatting.GRAY));
        }

        private static <T> ListOptionSpec<T> listOpt(ConfigValue<List<? extends T>> value) {
            return new ListOptionSpec<>(new ListConfigValueAdapter<>(value));
        }

        static <T> Binding<T> binding(Value<T> value) {
            return Binding.generic(value.getDefault(), value::get, value::set);
        }
    }

    private static abstract class AbstractSpec<T, SELF extends AbstractSpec<T, SELF>> {

        String nameKey;
        @Nullable Component nameOverride;
        String descKey;
        @Nullable String descWrapper;
        @Nullable ResourceLocation descImage;
        boolean noDesc;
        @Nullable Function<Option<T>, ControllerBuilder<T>> controllerFactory;

        AbstractSpec(String defaultKey) {
            this.nameKey = defaultKey;
            this.descKey = defaultKey;
        }

        @SuppressWarnings("unchecked")
        private SELF self() {
            return (SELF) this;
        }

        public SELF key(String key) {
            this.nameKey = key;
            this.descKey = key;
            return self();
        }

        public SELF name(String key) {
            this.nameKey = key;
            return self();
        }

        public SELF name(Component component) {
            this.nameOverride = component;
            return self();
        }

        public SELF desc(String key) {
            this.descKey = key;
            return self();
        }

        public SELF webpImage(ResourceLocation texture) {
            this.descImage = texture;
            return self();
        }

        public SELF noDesc() {
            this.noDesc = true;
            return self();
        }

        public SELF anyReloadHint() {
            this.descWrapper = "config.aquamirae.descWithAnyReload";
            return self();
        }

        public SELF worldReloadHint() {
            this.descWrapper = "config.aquamirae.descWithWorldReload";
            return self();
        }

        public SELF controller(Function<Option<T>, ControllerBuilder<T>> factory) {
            this.controllerFactory = factory;
            return self();
        }

        Component resolvedName() {
            return nameOverride != null ? nameOverride : translate("option." + nameKey);
        }

        @Nullable OptionDescription resolvedDesc() {
            if (noDesc) return null;
            var builder = OptionDescription.createBuilder();
            var description = translate("option." + descKey + ".desc").withStyle(ChatFormatting.GRAY);
            builder.text(descWrapper != null
                    ? Component.translatable(descWrapper, description)
                    : description);
            if (descImage != null) builder.webpImage(descImage);
            return builder.build();
        }

        Function<Option<T>, ControllerBuilder<T>> requireController() {
            assert controllerFactory != null;
            return controllerFactory;
        }
    }

    public static final class OptionSpec<T> extends AbstractSpec<T, OptionSpec<T>> {

        private final Value<T> value;

        private OptionSpec(Value<T> value) {
            super(value.name());
            this.value = value;
        }

        public Option<T> build() {
            var builder = Option.<T>createBuilder()
                    .flag()
                    .name(resolvedName())
                    .binding(Opts.binding(value));
            @Nullable var desc = resolvedDesc();
            if (desc != null) builder.description(desc);
            return builder.controller(requireController()).build();
        }
    }

    public static final class ListOptionSpec<T> extends AbstractSpec<T, ListOptionSpec<T>> {

        private final Value<List<T>> value;
        private T initial;

        private ListOptionSpec(Value<List<T>> value) {
            super(value.name());
            this.value = value;
        }

        public ListOptionSpec<T> initial(T initial) {
            this.initial = initial;
            return this;
        }

        public ListOption<T> build() {
            var builder = ListOption.<T>createBuilder()
                    .name(resolvedName())
                    .binding(Opts.binding(value))
                    .controller(requireController());
            @Nullable var desc = resolvedDesc();
            if (desc != null) builder.description(desc);
            if (initial != null) builder.initial(initial);
            return builder.build();
        }
    }

    private interface Value<T> {

        String name();

        T get();

        T getDefault();

        void set(T value);
    }

    private record ConfigValueAdapter<T>(ConfigValue<T> configValue) implements Value<T> {

        @Override
        public String name() {
            return configValue.name();
        }

        @Override
        public T get() {
            return configValue.get();
        }

        @Override
        public T getDefault() {
            return configValue.getDefault();
        }

        @Override
        public void set(T value) {
            configValue.set(value);
        }
    }

    private record ListConfigValueAdapter<T>(ConfigValue<List<? extends T>> configValue) implements Value<List<T>> {

        @Override
        public String name() {
            return configValue.name();
        }

        @Override
        public List<T> get() {
            return (List<T>) configValue.get();
        }

        @Override
        public List<T> getDefault() {
            return (List<T>) configValue.getDefault();
        }

        @Override
        public void set(List<T> value) {
            configValue.set(value);
        }
    }

    private record EarlyValueAdapter<T>(EarlyConfigValue<T> configValue) implements Value<T> {

        @Override
        public String name() {
            return configValue.name();
        }

        @Override
        public T get() {
            return configValue.get();
        }

        @Override
        public T getDefault() {
            return configValue.getDefault();
        }

        @Override
        public void set(T value) {
            configValue.set(value);
        }
    }

    private record EasingValueAdapter(ConfigValue<Easing> configValue) implements Value<String> {

        @Override
        public String name() {
            return configValue.name();
        }

        @Override
        public String get() {
            return configValue.get().name();
        }

        @Override
        public String getDefault() {
            return configValue.getDefault().name();
        }

        @Override
        public void set(String value) {
            configValue.set(Easing.valueOf(value));
        }
    }

    public enum ValueFormat {
        RAW(v -> Component.literal(AquamiraeHelper.formatDouble(v))),
        POTENCY(v -> translate("format.potency.%s".formatted(Math.round(v)))),
        BLOCKS(v -> translate("format.blocks", v.intValue())),
        CHUNKS(v -> translate("format.chunks", v.intValue())),
        MINUTES(v -> translate("format.minutes", AquamiraeHelper.formatDouble(v))),
        SECONDS(v -> translate("format.seconds", AquamiraeHelper.formatDouble(v))),
        PERCENT(v -> translate("format.percent", AquamiraeHelper.formatDouble(v * 100))),
        PIXELS(v -> translate("format.pixels", v.intValue())),
        ENTRIES(v -> translate("format.entries", v.intValue()));

        final ValueFormatter<Double> formatter;

        ValueFormat(ValueFormatter<Double> formatter) {
            this.formatter = formatter;
        }
    }
}