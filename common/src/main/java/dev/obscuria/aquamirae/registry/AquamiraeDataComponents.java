package dev.obscuria.aquamirae.registry;

import com.mojang.serialization.Codec;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.api.common.ability.AbilitySetComponent;
import dev.obscuria.aquamirae.common.component.*;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;

import java.util.function.Consumer;

public final class AquamiraeDataComponents {

    public static final DataComponentType<AbilitySetComponent> ABILITY_SET;
    public static final DataComponentType<LogBookContents> LOGBOOK_CONTENTS;
    public static final DataComponentType<GlobalPos> ECHO_TRACKER;
    public static final DataComponentType<ItemTooltip> TOOLTIP;
    public static final DataComponentType<RuneDamage> RUNE_OF_THE_STORM_DAMAGE;
    public static final DataComponentType<RuneProtection> RUNE_OF_THE_STORM_PROTECTION;
    public static final DataComponentType<Integer> ABYSSAL_CHARGES;
    public static final DataComponentType<Keepsake> KEEPSAKE;
    public static final DataComponentType<Unit> NO_SHADING;
    public static final DataComponentType<Unit> NO_GRAVITY;

    static {
        ABILITY_SET = register("ability_set", builder -> builder.persistent(AbilitySetComponent.CODEC).networkSynchronized(AbilitySetComponent.STREAM_CODEC).cacheEncoding());
        LOGBOOK_CONTENTS = register("logbook_contents", builder -> builder.persistent(LogBookContents.CODEC).networkSynchronized(LogBookContents.STREAM_CODEC).cacheEncoding());
        ECHO_TRACKER = register("echo_tracker", builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC).cacheEncoding());
        TOOLTIP = register("tooltip", builder -> builder.persistent(ItemTooltip.CODEC).networkSynchronized(ItemTooltip.STREAM_CODEC).cacheEncoding());
        RUNE_OF_THE_STORM_DAMAGE = register("rune_of_the_storm_damage", builder -> builder.persistent(RuneDamage.CODEC).networkSynchronized(RuneDamage.STREAM_CODEC));
        RUNE_OF_THE_STORM_PROTECTION = register("rune_of_the_storm_protection", builder -> builder.persistent(RuneProtection.CODEC).networkSynchronized(RuneProtection.STREAM_CODEC));
        ABYSSAL_CHARGES = register("abyssal_charges", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).cacheEncoding());
        KEEPSAKE = register("keepsake", builder -> builder.persistent(Keepsake.CODEC).networkSynchronized(Keepsake.STREAM_CODEC).cacheEncoding());
        NO_SHADING = register("no_shading", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
        NO_GRAVITY = register("no_gravity", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
    }

    private static <T> DataComponentType<T> register(String name, Consumer<DataComponentType.Builder<T>> consumer) {
        var builder = DataComponentType.<T>builder();
        consumer.accept(builder);
        var type = builder.build();
        AquamiraeRegistries.REGISTRAR.register(Registries.DATA_COMPONENT_TYPE, Aquamirae.id(name), () -> type);
        return type;
    }

    static void init() {}
}
