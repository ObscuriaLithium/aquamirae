package dev.obscuria.aquamirae.integration.hook.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.client.CameraShake;

public final class HookGameRenderer {

    public static void bobView(PoseStack poseStack, float partialTicks) {
        float x = CameraShake.offsetX;
        float y = CameraShake.offsetY;
        if (Math.abs(x) < 1e-5f && Math.abs(y) < 1e-5f) return;
        poseStack.mulPose(Axis.XP.rotationDegrees(x));
        poseStack.mulPose(Axis.YP.rotationDegrees(y));
    }
}
