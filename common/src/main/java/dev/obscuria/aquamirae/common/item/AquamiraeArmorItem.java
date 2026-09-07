package dev.obscuria.aquamirae.common.item;

import com.google.common.base.Suppliers;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.renderer.AquamiraeArmorRendererProvider;
import dev.obscuria.fragmentum.content.registry.Deferred;
import dev.obscuria.fragmentum.content.registry.DeferredAttribute;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AquamiraeArmorItem extends ArmorItem implements GeoItem {

    protected final AnimatableInstanceCache cache;
    protected final boolean glowing;

    public AquamiraeArmorItem(
            boolean glowing, Deferred<ArmorMaterial, ArmorMaterial> material,
            Type type, AttributeBuilder builder, Properties properties
    ) {
        super(material.holder(), type, properties.stacksTo(1));
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.glowing = glowing;
        var superDefaultModifiers = super.defaultModifiers;
        this.defaultModifiers = Suppliers.memoize(() -> {
            var modifierId = Aquamirae.id("armor." + type.getName());
            var equipmentSlotGroup = EquipmentSlotGroup.bySlot(type.getSlot());
            return builder.build(superDefaultModifiers.get(), modifierId, equipmentSlotGroup);
        });
    }

    public boolean hasGlowingLayer() {
        return glowing;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {}

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new AquamiraeArmorRendererProvider(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static class AttributeBuilder {

        private final List<Entry> entries = new ArrayList<>();

        public AttributeBuilder addValue(DeferredAttribute attribute, double value) {
            return addValue(attribute.holder(), value);
        }

        public AttributeBuilder addValue(Holder<Attribute> attribute, double value) {
            return add(attribute, value, AttributeModifier.Operation.ADD_VALUE);
        }

        public AttributeBuilder addMultipliedBase(DeferredAttribute attribute, double value) {
            return addMultipliedBase(attribute.holder(), value);
        }

        public AttributeBuilder addMultipliedBase(Holder<Attribute> attribute, double value) {
            return add(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        }

        public AttributeBuilder add(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            this.entries.add(new Entry(attribute, value, operation));
            return this;
        }

        private ItemAttributeModifiers build(ItemAttributeModifiers modifiers, ResourceLocation modifierId, EquipmentSlotGroup group) {
            for (var entry : entries) modifiers = modifiers.withModifierAdded(entry.attribute, entry.buildModifier(modifierId), group);
            return modifiers;
        }

        private record Entry(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {

            public AttributeModifier buildModifier(ResourceLocation modifierId) {
                return new AttributeModifier(modifierId, value, operation);
            }
        }
    }
}