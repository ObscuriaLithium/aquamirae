package dev.obscuria.aquamirae.config.early;

public interface EarlyConfigValue<T> {

    String name();

    T get();

    T getDefault();

    void set(T value);

    void save();
}
