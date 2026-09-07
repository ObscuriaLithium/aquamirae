package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.fragmentum.content.registry.DeferredAttribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public final class AquamiraeAttributes {

    public static final DeferredAttribute DEPTHS_FURY = register("depths_fury", 1.0, 1.0, 2.0);
    public static final DeferredAttribute DEPTHS_RESILIENCE = register("depths_resilience", 1.0, 1.0, 2.0);

    private static DeferredAttribute register(String name, double baseValue, double min, double max) {
        var attribute = new RangedAttribute("attribute.aquamirae." + name, baseValue, min, max).setSyncable(true);
        return AquamiraeRegistries.REGISTRAR.registerAttribute(Aquamirae.id(name), () -> attribute);
    }

    static void init() {}
}
