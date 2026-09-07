package dev.obscuria.aquamirae.config.reference;

import com.mojang.serialization.Codec;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.fragmentum.config.ConfigValue;
import net.minecraft.util.StringRepresentable;

public enum IntConfigReference implements StringRepresentable {

    PIRATE_SHIP_PLACEMENT_SPACING(CommonConfig.PIRATE_SHIP_PLACEMENT_SPACING),
    PIRATE_SHIP_PLACEMENT_SEPARATION(CommonConfig.PIRATE_SHIP_PLACEMENT_SEPARATION),
    PIRATE_OUTPOST_PLACEMENT_SPACING(CommonConfig.PIRATE_OUTPOST_PLACEMENT_SPACING),
    PIRATE_OUTPOST_PLACEMENT_SEPARATION(CommonConfig.PIRATE_OUTPOST_PLACEMENT_SEPARATION),
    PIRATE_SHELTER_PLACEMENT_SPACING(CommonConfig.PIRATE_SHELTER_PLACEMENT_SPACING),
    PIRATE_SHELTER_PLACEMENT_SEPARATION(CommonConfig.PIRATE_SHELTER_PLACEMENT_SEPARATION);

    public static final Codec<IntConfigReference> CODEC = StringRepresentable.fromEnum(IntConfigReference::values);

    private final ConfigValue<Integer> value;

    IntConfigReference(ConfigValue<Integer> value) {
        this.value = value;
    }

    public int get() {
        return value.get();
    }

    @Override
    public String getSerializedName() {
        return "@" + name();
    }
}
