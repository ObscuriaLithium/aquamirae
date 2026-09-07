package dev.obscuria.aquamirae.config.early;

public final class EarlyConfig {

    public static final EarlyConfigValue<Boolean> THEMED_MAIN_MENU;

    static {
        var builder = new EarlyConfigBuilder();
        THEMED_MAIN_MENU = builder.defineBool("themed_main_menu", true);
        EarlyConfigManager.SHARED.load();
    }
}
