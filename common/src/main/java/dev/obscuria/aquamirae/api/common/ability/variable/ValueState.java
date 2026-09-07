package dev.obscuria.aquamirae.api.common.ability.variable;

public enum ValueState {
    INCREASED("[color=green]"),
    DECREASED("[color=yellow]"),
    UNCHANGED("[color=white]");

    private final String tag;

    ValueState(String tag) {
        this.tag = tag;
    }

    public String wrap(String input) {
        return tag + input + "[/color]";
    }
}
