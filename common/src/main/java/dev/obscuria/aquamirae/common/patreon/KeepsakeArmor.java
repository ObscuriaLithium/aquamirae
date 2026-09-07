package dev.obscuria.aquamirae.common.patreon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.component.Keepsake;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import dev.obscuria.aquamirae.registry.AquamiraeAttributes;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.fragmentum.content.util.color.RGB;
import dev.obscuria.fragmentum.content.world.tooltip.TooltipOptions;
import dev.obscuria.fragmentum.content.world.tooltip.Tooltips;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record KeepsakeArmor(
        String nickname,
        Optional<String> lore,
        Optional<RGB> dyedColor,
        Optional<ArmorTrimConfig> armorTrim,
        Optional<EnchantmentConfig> enchantments,
        List<ItemConfig> items
) {

    public static final Codec<KeepsakeArmor> CODEC;

    public ItemStack generateRandom(RandomSource randomSource, HolderGetter.Provider provider) {
        if (items.isEmpty()) return ItemStack.EMPTY;
        return items.get(randomSource.nextInt(items.size())).generate(this, randomSource, provider);
    }

    static {
        CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.STRING.fieldOf("nickname").forGetter(KeepsakeArmor::nickname),
                Codec.STRING.optionalFieldOf("lore").forGetter(KeepsakeArmor::lore),
                RGB.CODEC.optionalFieldOf("dyed_color").forGetter(KeepsakeArmor::dyedColor),
                ArmorTrimConfig.CODEC.optionalFieldOf("armor_trim").forGetter(KeepsakeArmor::armorTrim),
                EnchantmentConfig.CODEC.optionalFieldOf("enchantments").forGetter(KeepsakeArmor::enchantments),
                ItemConfig.CODEC.listOf().fieldOf("items").forGetter(KeepsakeArmor::items)
        ).apply(builder, KeepsakeArmor::new));
    }

    public record ItemConfig(
            Item item,
            String name,
            Optional<String> loreOverride,
            Optional<RGB> dyedColorOverride,
            Optional<ArmorTrimConfig> armorTrimOverride,
            Optional<EnchantmentConfig> enchantmentOverride,
            List<ConfiguredAttribute> attributes
    ) {

        public static final Codec<ItemConfig> CODEC;

        public ItemStack generate(KeepsakeArmor armor, RandomSource randomSource, HolderGetter.Provider provider) {
            var stack = item.getDefaultInstance();
            stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
            stack.set(AquamiraeDataComponents.KEEPSAKE, new Keepsake(armor.nickname));
            this.loreOverride.or(armor::lore).ifPresent(lore -> applyLore(stack, lore));
            this.dyedColorOverride.or(armor::dyedColor).ifPresent(color -> applyColor(stack, color.decimal()));
            this.armorTrimOverride.or(armor::armorTrim).ifPresent(trim -> applyTrim(stack, trim, provider));
            this.enchantmentOverride.or(armor::enchantments).ifPresent(enchantments -> applyEnchantments(stack, enchantments, provider));
            this.applyAttributes(stack, randomSource);
            return stack;
        }

        private static void applyLore(ItemStack stack, String lore) {
            stack.set(DataComponents.LORE, new ItemLore(Tooltips.process(Component.literal(lore), null, TooltipOptions.LORE)));
        }

        private static void applyColor(ItemStack stack, int color) {
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color, false));
        }

        private static void applyTrim(ItemStack stack, ArmorTrimConfig trim, HolderGetter.Provider provider) {
            var pattern = provider.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(trim.pattern);
            var material = provider.lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(trim.material);
            stack.set(DataComponents.TRIM, new ArmorTrim(material, pattern, false));
        }

        private static void applyEnchantments(ItemStack stack, EnchantmentConfig enchantments, HolderGetter.Provider provider) {
            var lookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
            for (var entry : enchantments.enchantments.entrySet()) {
                var enchantment = lookup.getOrThrow(entry.getKey());
                stack.enchant(enchantment, entry.getValue());
            }
        }

        private void applyAttributes(ItemStack stack, RandomSource randomSource) {
            if (!(stack.getItem() instanceof ArmorItem armorItem)) return;
            var builder = ItemAttributeModifiers.builder();
            var modifierId = Aquamirae.id("armor." + armorItem.getType().getName());
            var equipmentSlotGroup = EquipmentSlotGroup.bySlot(armorItem.getType().getSlot());
            for (var attribute : attributes) {
                if (!attribute.isValid()) continue;
                builder.add(
                        attribute.attribute(),
                        new AttributeModifier(modifierId,
                                attribute.value().resolve(randomSource),
                                attribute.operation().getModifierOperation()),
                        equipmentSlotGroup);
            }
            builder.add(AquamiraeAttributes.DEPTHS_RESILIENCE.holder(),
                    new AttributeModifier(modifierId, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    equipmentSlotGroup);
            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
        }

        static {
            CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemConfig::item),
                    Codec.STRING.fieldOf("name").forGetter(ItemConfig::name),
                    Codec.STRING.optionalFieldOf("lore").forGetter(ItemConfig::loreOverride),
                    RGB.CODEC.optionalFieldOf("dyed_color").forGetter(ItemConfig::dyedColorOverride),
                    ArmorTrimConfig.CODEC.optionalFieldOf("armor_trim").forGetter(ItemConfig::armorTrimOverride),
                    EnchantmentConfig.CODEC.optionalFieldOf("enchantments").forGetter(ItemConfig::enchantmentOverride),
                    ConfiguredAttribute.CODEC.listOf().fieldOf("attributes").forGetter(ItemConfig::attributes)
            ).apply(builder, ItemConfig::new));
        }
    }

    public record EnchantmentConfig(
            Map<ResourceKey<Enchantment>, Integer> enchantments
    ) {

        public static final Codec<EnchantmentConfig> CODEC;

        static {
            CODEC = Codec
                    .unboundedMap(ResourceKey.codec(Registries.ENCHANTMENT), Codec.INT)
                    .xmap(EnchantmentConfig::new, EnchantmentConfig::enchantments);
        }
    }

    public record ArmorTrimConfig(
            ResourceKey<TrimPattern> pattern,
            ResourceKey<TrimMaterial> material
    ) {

        public static final Codec<ArmorTrimConfig> CODEC;

        static {
            CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    ResourceKey.codec(Registries.TRIM_PATTERN).fieldOf("pattern").forGetter(ArmorTrimConfig::pattern),
                    ResourceKey.codec(Registries.TRIM_MATERIAL).fieldOf("material").forGetter(ArmorTrimConfig::material)
            ).apply(builder, ArmorTrimConfig::new));
        }
    }
}
