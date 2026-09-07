package dev.obscuria.aquamirae.config.early;

public class EarlyConfigBuilder {

    public EarlyConfigValue<Boolean> defineBool(String name, boolean defaultValue) {
        EarlyConfigManager.SHARED.setBoolean(name, defaultValue);
        return new EarlyConfigValue<>() {

            @Override
            public String name() {
                return name;
            }

            @Override
            public Boolean get() {
                return EarlyConfigManager.SHARED.getBoolean(name, defaultValue);
            }

            @Override
            public Boolean getDefault() {
                return defaultValue;
            }

            @Override
            public void set(Boolean value) {
                EarlyConfigManager.SHARED.setBoolean(name, value);
            }

            @Override
            public void save() {}
        };
    }
}
