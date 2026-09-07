package dev.obscuria.aquamirae.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.DropdownStringControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.config.data.DoubleRange;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

record AttributeController(Option<String> option) implements Controller<String> {

    public static final String DEFAULT_ENTRY = "generic.max_health set(20)";

    @Override
    public Component formatValue() {
        var raw = option.pendingValue();
        if (raw == null || raw.isBlank()) {
            return ConfigScreenBuilder.translate("attribute_controller.not_set").withStyle(ChatFormatting.GRAY);
        }
        try {
            var configured = ConfiguredAttribute.parse(raw);
            if (!configured.isValid()) throw new IllegalArgumentException(raw);
            return summarize(configured);
        } catch (Exception e) {
            return Component.literal(raw).withStyle(ChatFormatting.RED);
        }
    }

    private static Component summarize(ConfiguredAttribute configured) {
        var name = Component.translatable(configured.attribute().value().getDescriptionId()).getString();
        var symbol = switch (configured.operation()) {
            case SET -> "=";
            case ADD -> configured.value().min() >= 0 && configured.value().max() >= 0 ? "+" : "";
            case MULTIPLY -> "\u00d7";
            default -> "?";
        };
        var range = configured.value();
        var valueText = range.min() == range.max()
                ? AquamiraeHelper.formatDouble(range.min())
                : AquamiraeHelper.formatDouble(range.min()) + "~" + AquamiraeHelper.formatDouble(range.max());
        return Component.literal(name + " " + symbol + valueText);
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new Element(this, screen, widgetDimension);
    }

    private static void openEditor(YACLScreen parent, AttributeController control) {
        var attributeOptions = collectAttributeOptions();
        var defaults = parseOrDefault(control.option().pendingValue(), attributeOptions);
        var state = new EditState(defaults.attributeLabel(), defaults.operation(), defaults.min(), defaults.max());

        var category = ConfigCategory.createBuilder()
                .name(ConfigScreenBuilder.translate("attribute_editor.title"))
                .option(Option.<String>createBuilder()
                        .name(ConfigScreenBuilder.translate("attribute_editor.attribute"))
                        .description(ConfigScreenBuilder.Opts.descriptionOf(ConfigScreenBuilder.translate("attribute_editor.attribute.desc")))
                        .binding(Binding.generic(state.attributeLabel, () -> state.attributeLabel, v -> state.attributeLabel = v))
                        .controller(opt -> DropdownStringControllerBuilder.create(opt)
                                .values(new ArrayList<>(attributeOptions.keySet()))
                                .allowAnyValue(false)
                                .allowEmptyValue(false))
                        .build())
                .option(Option.<OperationChoice>createBuilder()
                        .name(ConfigScreenBuilder.translate("attribute_editor.operation"))
                        .description(ConfigScreenBuilder.Opts.descriptionOf(ConfigScreenBuilder.translate("attribute_editor.operation.desc")))
                        .binding(Binding.generic(state.operation, () -> state.operation, v -> state.operation = v))
                        .controller(opt -> EnumControllerBuilder.create(opt).enumClass(OperationChoice.class))
                        .build())
                .option(Option.<Double>createBuilder()
                        .name(ConfigScreenBuilder.translate("attribute_editor.min"))
                        .description(ConfigScreenBuilder.Opts.descriptionOf(ConfigScreenBuilder.translate("attribute_editor.min.desc")))
                        .binding(Binding.generic(state.min, () -> state.min, v -> state.min = v))
                        .controller(DoubleFieldControllerBuilder::create)
                        .build())
                .option(Option.<Double>createBuilder()
                        .name(ConfigScreenBuilder.translate("attribute_editor.max"))
                        .description(ConfigScreenBuilder.Opts.descriptionOf(ConfigScreenBuilder.translate("attribute_editor.max.desc")))
                        .binding(Binding.generic(state.max, () -> state.max, v -> state.max = v))
                        .controller(DoubleFieldControllerBuilder::create)
                        .build())
                .build();

        Minecraft.getInstance().setScreen(YetAnotherConfigLib.createBuilder()
                .title(ConfigScreenBuilder.translate("attribute_editor.title"))
                .category(category)
                .save(() -> {
                    var attributeId = attributeOptions.get(state.attributeLabel);
                    if (attributeId == null) return;
                    var range = new DoubleRange(state.min, state.max);
                    control.option.requestSet("%s %s(%s)".formatted(attributeId, state.operation.keyword(), range));
                })
                .build()
                .generateScreen(parent));
    }

    private static Map<String, String> collectAttributeOptions() {
        Map<String, String> options = new TreeMap<>();
        for (var entry : BuiltInRegistries.ATTRIBUTE.entrySet()) {
            var location = entry.getKey().location();
            var displayName = Component.translatable(entry.getValue().getDescriptionId()).getString();
            var id = "minecraft".equals(location.getNamespace()) ? location.getPath() : location.toString();
            options.put("%s (%s)".formatted(displayName, id), id);
        }
        return options;
    }

    private static EditorDefaults parseOrDefault(String raw, Map<String, String> attributeOptions) {
        if (raw != null && !raw.isBlank()) {
            try {
                var configured = ConfiguredAttribute.parse(raw);
                if (configured.isValid()) {
                    var location = BuiltInRegistries.ATTRIBUTE.getKey(configured.attribute().value());
                    var id = location == null
                            ? null
                            : ("minecraft".equals(location.getNamespace()) ? location.getPath() : location.toString());
                    var label = attributeOptions.entrySet().stream()
                            .filter(e -> e.getValue().equals(id))
                            .map(Map.Entry::getKey)
                            .findFirst();
                    if (label.isPresent()) {
                        return new EditorDefaults(
                                label.get(),
                                OperationChoice.from(configured.operation()),
                                configured.value().min(),
                                configured.value().max());
                    }
                }
            } catch (Exception ignored) {}
        }
        return new EditorDefaults(fallbackLabel(attributeOptions), OperationChoice.ADD, 0.0, 0.0);
    }

    private static String fallbackLabel(Map<String, String> attributeOptions) {
        var fallbackId = BuiltInRegistries.ATTRIBUTE.getKey(Attributes.MAX_HEALTH.value());
        var fallbackIdString = fallbackId == null ? null
                : ("minecraft".equals(fallbackId.getNamespace()) ? fallbackId.getPath() : fallbackId.toString());
        return attributeOptions.entrySet().stream()
                .filter(e -> e.getValue().equals(fallbackIdString))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(() -> attributeOptions.keySet().stream().findFirst().orElse(""));
    }

    private static final class EditState {

        private String attributeLabel;
        private OperationChoice operation;
        private double min;
        private double max;

        private EditState(String attributeLabel, OperationChoice operation, double min, double max) {
            this.attributeLabel = attributeLabel;
            this.operation = operation;
            this.min = min;
            this.max = max;
        }
    }

    private record EditorDefaults(String attributeLabel, OperationChoice operation, double min, double max) {}

    public enum OperationChoice implements NameableEnum {
        SET(ConfiguredAttribute.Operation.SET, "set"),
        ADD(ConfiguredAttribute.Operation.ADD, "add"),
        MULTIPLY(ConfiguredAttribute.Operation.MULTIPLY, "multiply");

        private final ConfiguredAttribute.Operation operation;
        private final String keyword;

        OperationChoice(ConfiguredAttribute.Operation operation, String keyword) {
            this.operation = operation;
            this.keyword = keyword;
        }

        public String keyword() {
            return keyword;
        }

        public static OperationChoice from(ConfiguredAttribute.Operation operation) {
            for (var choice : values()) {
                if (choice.operation == operation) return choice;
            }
            return ADD;
        }

        @Override
        public Component getDisplayName() {
            return Component.literal(name());
        }
    }

    public static final class Factory implements ControllerBuilder<String> {

        private final Option<String> option;

        private Factory(Option<String> option) {
            this.option = option;
        }

        public static Factory create(Option<String> option) {
            return new Factory(option);
        }

        @Override
        public Controller<String> build() {
            return new AttributeController(option);
        }
    }

    public static class Element extends ControllerWidget<AttributeController> {

        public Element(AttributeController control, YACLScreen screen, Dimension<Integer> dim) {
            super(control, screen, dim);
        }

        @Override
        protected int getHoveredControlWidth() {
            return getUnhoveredControlWidth();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOver(mouseX, mouseY) && isAvailable()) {
                playDownSound();
                openEditor(screen, control);
                return true;
            }
            return false;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!focused) return false;
            if (keyCode == 257 || keyCode == 335 || keyCode == 32) {
                openEditor(screen, control);
                return true;
            }
            return false;
        }

        @Override
        public boolean canReset() {
            return true;
        }
    }
}