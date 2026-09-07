package dev.obscuria.aquamirae.common.entity;

public enum ShellbackChestType {

    FRONT_LEFT("front_left_chest", false, 1, 1, 0.9f, 0.7f),
    FRONT_RIGHT("front_right_chest", true, -1, 1, 0.9f, 0.7f),
    BACK_LEFT("back_left_chest", false, 1, -1, 0.9f, 0.7f),
    BACK_RIGHT("back_right_chest", true, -1, -1, 0.9f, 0.7f);

    public final String name;
    public final boolean mirrored;
    public final int sideSign;
    public final int frontSign;
    public final float bbWidth;
    public final float bbHeight;

    public static final float SIDE_OFFSET = 1.1f;
    public static final float FRONT_OFFSET = 0.45f;

    ShellbackChestType(String name, boolean mirrored, int sideSign, int frontSign, float bbWidth, float bbHeight) {
        this.name = name;
        this.mirrored = mirrored;
        this.sideSign = sideSign;
        this.frontSign = frontSign;
        this.bbWidth = bbWidth;
        this.bbHeight = bbHeight;
    }
}