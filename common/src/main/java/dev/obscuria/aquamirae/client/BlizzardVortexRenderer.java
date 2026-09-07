package dev.obscuria.aquamirae.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.fragmentum.content.util.color.Colors;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public final class BlizzardVortexRenderer {

    private static final int MAIN_ARMS = 4;
    private static final int SEC_ARMS = 3;
    private static final int SEGMENTS = 200;
    private static final float TURNS = 2.2f;

    private static final float MAX_RADIUS = 200f;
    private static final float MIN_RADIUS = 0f;
    private static final float SKY_Y = 64f;

    private static final float RIBBON_HALF = 14f;
    private static final float SEC_RIBBON_HALF = 7f;

    private static final float MAIN_RPM = 0.048f;
    private static final float SEC_RPM = 0.085f;

    private static final float MAIN_ALPHA = 0.33f;
    private static final float SEC_ALPHA = 0.14f;

    public static final ARGB COLOR = Colors.argbOf(1f, 0.78f, 0.89f, 1.00f);

    private static final float FADE_SEC = 5f;

    public static final BlizzardVortexRenderer INSTANCE = new BlizzardVortexRenderer();

    private float fadeAlpha = 0f;
    private long lastMs = -1L;

    private BlizzardVortexRenderer() {}

    public void render(float partialTick, Camera camera, ClientLevel level) {
        long now = System.currentTimeMillis();
        if (lastMs < 0) lastMs = now;
        float dt = Math.min((now - lastMs) / 1000f, 0.05f);
        lastMs = now;

        boolean active = isActive(level, camera.getBlockPosition());
        fadeAlpha = active
                ? Math.min(1f, fadeAlpha + dt / FADE_SEC)
                : Math.max(0f, fadeAlpha - dt / FADE_SEC);
        if (fadeAlpha < 0.002f) return;

        float t = (level.getGameTime() + partialTick) * 0.05f;

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE);
        RenderSystem.depthMask(false);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        PoseStack ps = new PoseStack();
        Matrix4f mat = ps.last().pose();

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        float mainRot = t * MAIN_RPM * Mth.TWO_PI;
        for (int a = 0; a < MAIN_ARMS; a++) {
            float phase = a * Mth.TWO_PI / MAIN_ARMS;
            emitArm(buf, mat, mainRot + phase, SKY_Y,
                    MIN_RADIUS, MAX_RADIUS, RIBBON_HALF,
                    MAIN_ALPHA * fadeAlpha,
                    COLOR.red(), COLOR.green(), COLOR.blue(), t, 1f);
        }

        float secRot = -t * SEC_RPM * Mth.TWO_PI;
        for (int a = 0; a < SEC_ARMS; a++) {
            float phase = a * Mth.TWO_PI / SEC_ARMS + 0.4f;
            emitArm(buf, mat, secRot + phase, SKY_Y * 0.85f,
                    MIN_RADIUS * 0.6f, MAX_RADIUS * 0.55f, SEC_RIBBON_HALF,
                    SEC_ALPHA * fadeAlpha,
                    Math.min(1f, COLOR.red() + 0.08f), Math.min(1f, COLOR.green() + 0.05f), COLOR.blue(), t, -1f);
        }

        BufferUploader.drawWithShader(buf.buildOrThrow());

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    private void emitArm(BufferBuilder buf, Matrix4f mat,
                         float phase, float skyY,
                         float minR, float maxR, float halfW,
                         float peakAlpha,
                         float cr, float cg, float cb,
                         float t, float rotSign) {

        int ri = f2i(cr), gi = f2i(cg), bi = f2i(cb);

        float[] cx = new float[SEGMENTS + 1];
        float[] cz = new float[SEGMENTS + 1];
        float[] ex = new float[SEGMENTS + 1];
        float[] ez = new float[SEGMENTS + 1];
        float[] alph = new float[SEGMENTS + 1];

        for (int si = 0; si <= SEGMENTS; si++) {
            float s = si / (float) SEGMENTS;
            float r = minR + (maxR - minR) * (float) Math.pow(s, 0.78);
            float theta = phase + s * TURNS * Mth.TWO_PI;

            cx[si] = r * Mth.cos(theta);
            cz[si] = r * Mth.sin(theta);

            ex[si] = -Mth.sin(theta) * halfW;
            ez[si] = Mth.cos(theta) * halfW;

            float fade = Mth.sin(s * Mth.PI);
            float streak = 0.55f + 0.45f * Mth.sin(s * Mth.PI * 9f + t * 2.8f * rotSign);

            float radialFade = (1f - s) * (1f - s) * (1f - s);

            alph[si] = Math.max(0f, peakAlpha * fade * streak * radialFade);
        }

        for (int si = 0; si < SEGMENTS; si++) {

            float a0 = alph[si];
            float a1 = alph[si + 1];

            int ac0 = f2i(a0);
            int ac1 = f2i(a1);

            buf.addVertex(mat, cx[si] - ex[si], skyY, cz[si] - ez[si]).setColor(ri, gi, bi, 0);
            buf.addVertex(mat, cx[si + 1] - ex[si + 1], skyY, cz[si + 1] - ez[si + 1]).setColor(ri, gi, bi, 0);
            buf.addVertex(mat, cx[si + 1], skyY, cz[si + 1]).setColor(ri, gi, bi, ac1);
            buf.addVertex(mat, cx[si], skyY, cz[si]).setColor(ri, gi, bi, ac0);

            buf.addVertex(mat, cx[si], skyY, cz[si]).setColor(ri, gi, bi, ac0);
            buf.addVertex(mat, cx[si + 1], skyY, cz[si + 1]).setColor(ri, gi, bi, ac1);
            buf.addVertex(mat, cx[si + 1] + ex[si + 1], skyY, cz[si + 1] + ez[si + 1]).setColor(ri, gi, bi, 0);
            buf.addVertex(mat, cx[si] + ex[si], skyY, cz[si] + ez[si]).setColor(ri, gi, bi, 0);
        }
    }

    private static int f2i(float v) {
        return (int) Mth.clamp(v * 255f, 0f, 255f);
    }

    private boolean isActive(ClientLevel level, BlockPos pos) {
        if (!level.isRaining()) return false;
        return level.getBiome(pos).is(Aquamirae.id("ice_maze"));
    }
}