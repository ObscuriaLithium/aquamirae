package dev.obscuria.aquamirae.config.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public record DoubleRange(double min, double max) {

    public static final DoubleRange ZERO;
    public static final Codec<DoubleRange> CODEC;
    public static final String DELIMITER = "~";

    public double resolve(RandomSource random) {
        return Math.round(Mth.lerp(random.nextDouble(), min, max) * 100.0) / 100.0;
    }

    public static DoubleRange parse(String input) {
        if (!input.contains(DELIMITER)) {
            var value = Double.parseDouble(input);
            return new DoubleRange(value, value);
        } else {
            var parts = input.split(DELIMITER);
            var min = Double.parseDouble(parts[0]);
            var max = Double.parseDouble(parts[1]);
            return new DoubleRange(min, max);
        }
    }

    @Override
    public String toString() {
        return min == max
                ? String.valueOf(min)
                : min + DELIMITER + max;
    }

    static {
        CODEC = Codec.STRING.xmap(DoubleRange::parse, DoubleRange::toString);
        ZERO = new DoubleRange(0.0, 0.0);
    }
}
