package dev.obscuria.aquamirae.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class ShipbreakerEncounterTrigger extends SimpleCriterionTrigger<ShipbreakerEncounterTrigger.TriggerInstance> {

    static final ResourceLocation ID = Aquamirae.id("shipbreaker_encounter");

    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, TriggerInstance::matches);
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC;

        @Override
        public Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }

        private static boolean matches(TriggerInstance instance) {
            return true;
        }

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
            ).apply(codec, TriggerInstance::new));
        }
    }
}