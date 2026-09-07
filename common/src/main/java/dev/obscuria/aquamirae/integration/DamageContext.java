package dev.obscuria.aquamirae.integration;

public final class DamageContext {

    private final float baseDamage;
    private float flatBonus = 0.0f;
    private float multiplier = 1.0f;

    public DamageContext(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public DamageContext add(float damage) {
        this.flatBonus += damage;
        return this;
    }

    public DamageContext multiply(float multiplier) {
        this.multiplier *= multiplier;
        return this;
    }

    public float result() {
        return (baseDamage + flatBonus) * multiplier;
    }
}