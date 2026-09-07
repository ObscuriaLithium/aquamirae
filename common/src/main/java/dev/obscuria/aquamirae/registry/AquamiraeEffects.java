package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.effect.DevouringCurseEffect;
import dev.obscuria.aquamirae.common.effect.PoisonImmunityEffect;
import dev.obscuria.fragmentum.content.registry.DeferredMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.function.Supplier;

public final class AquamiraeEffects {

    public static final DeferredMobEffect<MobEffect> FURY_OF_THE_DEPTHS = register("fury_of_the_depths",
            () -> new MobEffect(MobEffectCategory.BENEFICIAL, 16762624)
                    .addAttributeModifier(AquamiraeAttributes.DEPTHS_FURY.holder(), Aquamirae.id("modifier"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static final DeferredMobEffect<MobEffect> RESILIENCE_OF_THE_DEPTHS = register("resilience_of_the_depths",
            () -> new MobEffect(MobEffectCategory.BENEFICIAL, 16762624)
                    .addAttributeModifier(AquamiraeAttributes.DEPTHS_RESILIENCE.holder(), Aquamirae.id("modifier"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static final DeferredMobEffect<MobEffect> HEALTH_SHATTER = register("health_shatter",
            () -> new MobEffect(MobEffectCategory.HARMFUL, -6750055)
                    .addAttributeModifier(Attributes.MAX_HEALTH, Aquamirae.id("modifier"), -0.05D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredMobEffect<MobEffect> ARMOR_SHATTER = register("armor_shatter",
            () -> new MobEffect(MobEffectCategory.HARMFUL, -6750055)
                    .addAttributeModifier(Attributes.ARMOR, Aquamirae.id("modifier"), -0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredMobEffect<MobEffect> DEVOURING_CURSE = register("devouring_curse",
            () -> new DevouringCurseEffect(MobEffectCategory.HARMFUL, -6750055));
    public static final DeferredMobEffect<MobEffect> CRYSTALLIZATION = register("crystallization",
            () -> new MobEffect(MobEffectCategory.HARMFUL, -6750055)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, Aquamirae.id("modifier"), -0.8D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, Aquamirae.id("modifier"), -0.8D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredMobEffect<MobEffect> POISON_IMMUNITY = register("poison_immunity",
            () -> new PoisonImmunityEffect(MobEffectCategory.BENEFICIAL, -6750055));

    private static DeferredMobEffect<MobEffect> register(String name, Supplier<MobEffect> supplier) {
        return AquamiraeRegistries.REGISTRAR.registerMobEffect(Aquamirae.id(name), supplier);
    }

    static void init() {}
}
