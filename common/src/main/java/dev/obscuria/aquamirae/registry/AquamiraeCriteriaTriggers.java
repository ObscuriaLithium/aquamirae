package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.advancement.ShipbreakerEncounterTrigger;
import dev.obscuria.aquamirae.common.advancement.ShipbreakerSurvivalTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;

public final class AquamiraeCriteriaTriggers {

    public static final ShipbreakerEncounterTrigger SHIPBREAKER_ENCOUNTER = register("shipbreaker_encounter", new ShipbreakerEncounterTrigger());
    public static final ShipbreakerSurvivalTrigger SHIPBREAKER_SURVIVAL = register("shipbreaker_survival", new ShipbreakerSurvivalTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T value) {
        AquamiraeRegistries.REGISTRAR.register(Registries.TRIGGER_TYPE, Aquamirae.id(name), () -> value);
        return value;
    }

    static void init() {}
}