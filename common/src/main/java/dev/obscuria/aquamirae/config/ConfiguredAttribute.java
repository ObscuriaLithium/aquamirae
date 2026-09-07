package dev.obscuria.aquamirae.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.serialization.Codec;
import dev.obscuria.aquamirae.config.data.DoubleRange;
import dev.obscuria.fragmentum.config.ConfigValue;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

public record ConfiguredAttribute(
        Holder<Attribute> attribute,
        Operation operation,
        DoubleRange value
) {
    public static final Codec<ConfiguredAttribute> CODEC;
    public static final ConfiguredAttribute INVALID;

    private static final LoadingCache<String, ConfiguredAttribute> CACHE;

    public boolean isValid() {
        return !equals(INVALID) && operation != Operation.INVALID;
    }

    public static void applyAttributes(LivingEntity entity, ConfigValue<List<? extends String>> configValue) {
        for (var attributeDefinition : configValue.get()) {
            var configured = CACHE.getUnchecked(attributeDefinition);
            if (!configured.isValid()) continue;
            @Nullable var instance = entity.getAttribute(configured.attribute());
            if (instance == null) continue;
            instance.setBaseValue(configured.value().resolve(entity.getRandom()));
        }
        entity.setHealth(entity.getMaxHealth());
    }

    public static ConfiguredAttribute parse(String input) {
        int length = input.length();
        int operationStart = -1;
        int openParen = -1;

        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (c == ' ') {
                operationStart = i + 1;
                break;
            }
        }

        if (operationStart <= 0) {
            throw new IllegalArgumentException("Missing operation: " + input);
        }

        for (int i = operationStart; i < length; i++) {
            if (input.charAt(i) == '(') {
                openParen = i;
                break;
            }
        }

        if (openParen < 0 || input.charAt(length - 1) != ')') {
            throw new IllegalArgumentException("Invalid attribute definition: " + input);
        }

        var attributeId = ResourceLocation.parse(input.substring(0, operationStart - 1));
        var attribute = BuiltInRegistries.ATTRIBUTE.getHolder(attributeId);
        if (attribute.isEmpty()) {
            throw new IllegalArgumentException("Invalid attribute: " + input);
        }

        var operation = Operation.parse(input.substring(operationStart, openParen));
        var value = DoubleRange.parse(input.substring(openParen + 1, length - 1));

        return new ConfiguredAttribute(attribute.get(), operation, value);
    }

    public static ConfiguredAttribute parseOrInvalid(String input) {
        try {
            return parse(input);
        } catch (Exception ignored) {
            return INVALID;
        }
    }

    @Override
    public String toString() {
        return "%s %s(%s)".formatted(
                attribute.unwrapKey().orElseThrow().location(),
                operation.name().toLowerCase(Locale.ROOT),
                value.toString());
    }

    static {
        CODEC = Codec.STRING.xmap(ConfiguredAttribute::parseOrInvalid, ConfiguredAttribute::toString);
        INVALID = new ConfiguredAttribute(Attributes.MAX_HEALTH, Operation.INVALID,  DoubleRange.ZERO);
        CACHE = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(20))
                .build(new Loader());
    }

    public enum Operation {
        SET(AttributeModifier.Operation.ADD_VALUE),
        ADD(AttributeModifier.Operation.ADD_VALUE),
        MULTIPLY(AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
        INVALID(AttributeModifier.Operation.ADD_VALUE);

        @Getter private final AttributeModifier.Operation modifierOperation;

        Operation(AttributeModifier.Operation modifierOperation) {
            this.modifierOperation = modifierOperation;
        }

        public static Operation parse(String input) {
            return switch (input) {
                case "set" -> SET;
                case "add" -> ADD;
                case "multiply" -> MULTIPLY;
                default -> throw new IllegalArgumentException("Invalid operation: " + input);
            };
        }
    }

    private static class Loader extends CacheLoader<String, ConfiguredAttribute> {

        @Override
        public ConfiguredAttribute load(String key) {
            try {
                return ConfiguredAttribute.parse(key);
            } catch (Exception ignored) {
                return new ConfiguredAttribute(Attributes.MAX_HEALTH, Operation.INVALID, DoubleRange.ZERO);
            }
        }
    }
}
