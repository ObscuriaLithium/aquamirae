package dev.obscuria.aquamirae.client.renderer.spline;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.SplineBody;
import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.fragmentum.content.util.color.Colors;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import dev.obscuria.fragmentum.content.util.easing.EasingFunction;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class SplineBodyRenderer<T extends SplineBody, D extends BodyDecoration> {

    private static final int MIN_LIGHT = LightTexture.pack(0, 3);

    protected final Map<Integer, D> decorations = new HashMap<>();

    private float bodyWidth = 1f;
    private EasingFunction widthEasing = Easing.LINEAR;
    private ResourceLocation bodyTexture = Aquamirae.id("empty");
    private float bodyTextureRepeat = 1f;
    private boolean isUnshaded;
    private @Nullable CrestVisual crestVisual = null;

    private final List<BodyRenderLayer> renderLayers = new ArrayList<>();

    private final VertexScratch vs = new VertexScratch();
    private final NormalScratch ns = new NormalScratch();
    private final UvScratch uvs = new UvScratch();
    private final List<Vector3f> cachedFaceNormals = new ArrayList<>();
    private int cachedRingSegments = -1;

    public void unshaded(boolean value) {
        this.isUnshaded = value;
    }

    public void defineWidth(float width, EasingFunction easing) {
        this.bodyWidth = width;
        this.widthEasing = easing;
    }

    public void defineBodyTexture(ResourceLocation texture, float repeat) {
        this.bodyTexture = texture;
        this.bodyTextureRepeat = repeat;
    }

    public void defineCrestVisual(CrestVisual crest) {
        this.crestVisual = crest;
    }

    public void addRenderLayer(BodyRenderLayer layer) {
        this.renderLayers.add(layer);
    }

    public void addDecoration(int bodyPosition, D decoration) {
        decorations.put(bodyPosition, decoration);
    }

    protected void pushToOrigin(PoseStack pose, Vec3 origin, Runnable body) {
        pose.pushPose();
        pose.translate(-origin.x, -origin.y, -origin.z);
        body.run();
        pose.popPose();
    }

    protected void pushToRing(PoseStack pose, SplineBody.BakedRing ring, Runnable body) {
        pose.pushPose();
        pose.translate(ring.getPosition().x, ring.getPosition().y, ring.getPosition().z);
        var orientation = ring.getOrientation();
        var rotation = new Matrix4f(
                -orientation.m00(), -orientation.m01(), -orientation.m02(), 0f,
                orientation.m10(), orientation.m11(), orientation.m12(), 0f,
                orientation.m20(), orientation.m21(), orientation.m22(), 0f,
                0f, 0f, 0f, 1f
        ).getUnnormalizedRotation(new Quaternionf()).normalize();
        pose.mulPose(rotation);
        body.run();
        pose.popPose();
    }

    protected float widthAt(float tween) {
        return bodyWidth * widthEasing.compute(tween);
    }

    protected int packedLightAt(Level level, Vector3f position) {
        BlockPos bp = BlockPos.containing(position.x, isUnshaded ? 999f : position.y, position.z);
        int block = level.getBrightness(LightLayer.BLOCK, bp);
        int sky = level.getBrightness(LightLayer.SKY, bp);
        return LightTexture.pack(block, sky);
    }

    protected void renderSegment(
            Level level,
            PoseStack pose,
            T body,
            SplineBody.BakedRing first,
            SplineBody.BakedRing second,
            MultiBufferSource buffer,
            int packedOverlay
    ) {
        Matrix4f transform = pose.last().pose();
        int light1 = Math.max(MIN_LIGHT, packedLightAt(level, first.getPosition()));
        int light2 = Math.max(MIN_LIGHT, packedLightAt(level, second.getPosition()));
        ARGB white = Colors.argbOf(0xFFFFFFFF);

        VertexConsumer baseConsumer = buffer.getBuffer(RenderType.entitySolid(bodyTexture));
        emitTubeQuads(body, first, second, baseConsumer, transform,
                packedOverlay, light1, light2, bodyTextureRepeat, white);

        if (crestVisual != null && first.getPosition() != null) {
            renderCrestPass(first, second, transform, buffer, packedOverlay, light1, light2, white);
        }

        for (BodyRenderLayer layer : renderLayers) {
            ARGB layerColor = Colors.argbOf(layer.getColor());
            float layerRepeat = layer.getTextureRepeat();
            VertexConsumer layerConsumer = buffer.getBuffer(layer.getRenderType(layer.getTexture()));
            emitTubeQuads(body, first, second, layerConsumer, transform,
                    packedOverlay, light1, light2, layerRepeat, layerColor);
        }
    }

    private void renderCrestPass(
            SplineBody.BakedRing first, SplineBody.BakedRing second,
            Matrix4f transform, MultiBufferSource buffer,
            int overlay, int light1, int light2, ARGB color
    ) {
        var crestConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(crestVisual.texture()));
        emitCrestQuad(first, second, true, crestConsumer, transform, overlay, light1, light2, bodyTextureRepeat, color);
        emitCrestQuad(first, second, false, crestConsumer, transform, overlay, light1, light2, bodyTextureRepeat, color);
    }

    private Vector3f computeCrestTip(SplineBody.BakedRing ring, boolean top) {
        float tween = ring.getTween();
        float w = widthAt(tween);
        float crestW = w * crestVisual.crestEasing().compute(tween);

        int edgeIdx = top ? ring.getRing().size() / 2 : 0;
        Vector3f edgeWorld = new Vector3f(ring.getRing().get(edgeIdx)).mul(w).add(ring.getPosition());

        Vector3f localUp = ring.getOrientation().transformDirection(new Vector3f(0f, 1f, 0f)).normalize();
        float height = crestW * (top ? crestVisual.topHeight() : -crestVisual.bottomHeight());

        return new Vector3f(edgeWorld).add(new Vector3f(localUp).mul(height));
    }

    private void emitCrestQuad(
            SplineBody.BakedRing first, SplineBody.BakedRing second,
            boolean top,
            VertexConsumer consumer, Matrix4f transform,
            int overlay, int light1, int light2,
            float uRepeat, ARGB color
    ) {
        float fW = widthAt(first.getTween());
        float sW = widthAt(second.getTween());
        int edgeIdx = top ? first.getRing().size() / 2 : 0;

        Vector3f posA = first.getPosition();
        Vector3f posB = second.getPosition();
        Vector3f edgeA = new Vector3f(first.getRing().get(edgeIdx)).mul(fW).add(posA);
        Vector3f edgeB = new Vector3f(second.getRing().get(edgeIdx)).mul(sW).add(posB);
        Vector3f tipA = computeCrestTip(first, top);
        Vector3f tipB = computeCrestTip(second, top);

        {
            float timeA = Mth.abs(posA.x) + Mth.abs(posA.z);
            float timeB = Mth.abs(posB.x) + Mth.abs(posB.z);
            float waveA = 5f * widthAt(first.getTween()) * Mth.cos(timeA * 0.6f);
            float waveB = 5f * widthAt(second.getTween()) * Mth.cos(timeB * 0.6f);

            Vector3f rightA = new Vector3f(1, 0, 0);
            first.getOrientation().transformDirection(rightA);

            Vector3f rightB = new Vector3f(1, 0, 0);
            second.getOrientation().transformDirection(rightB);

            tipA.fma(waveA, rightA);
            tipB.fma(waveB, rightB);
        }

        float tA = first.getTween() * uRepeat;
        float tB = second.getTween() * uRepeat;

        int faceIndex = top ? first.getRing().size() / 2 : 0;
        var normal = first.getOrientation().transformDirection(new Vector3f(faceNormals(first.getRing().size()).get(faceIndex)));
        ns.x = normal.x;
        ns.y = normal.y;
        ns.z = normal.z;

        emitVertex(consumer, transform, ns, edgeA.x, edgeA.y, edgeA.z, tA, 0f, overlay, light1, color);
        emitVertex(consumer, transform, ns, edgeB.x, edgeB.y, edgeB.z, tB, 0f, overlay, light2, color);
        emitVertex(consumer, transform, ns, tipB.x, tipB.y, tipB.z, tB, 1f, overlay, light2, color);
        emitVertex(consumer, transform, ns, tipA.x, tipA.y, tipA.z, tA, 1f, overlay, light1, color);
    }

    private void emitTubeQuads(
            T body,
            SplineBody.BakedRing first, SplineBody.BakedRing second,
            VertexConsumer consumer, Matrix4f transform,
            int overlay, int light1, int light2,
            float uRepeat, ARGB color
    ) {
        int q = body.ringSegments;
        for (int i1 = 0; i1 < q; i1++) {
            int i2 = (i1 + 1) % q;
            float wF = widthAt(first.getTween());
            float wS = widthAt(second.getTween());

            fillVertexScratch(
                    first.getRing().get(i1), wF, first.getPosition(),
                    second.getRing().get(i1), wS, second.getPosition(),
                    second.getRing().get(i2), wS,
                    first.getRing().get(i2), wF);

            fillNormalScratch(body, first.getOrientation(), i1);
            fillUvScratch(uRepeat, first.getTween(), second.getTween(), 1f / q, i1);
            emitQuad(consumer, transform, ns, vs, uvs, overlay, light1, light2, color);
        }
    }

    private void fillVertexScratch(
            Vector3f r1, float w1, Vector3f p1,
            Vector3f r2, float w2, Vector3f p2,
            Vector3f r3, float w3,
            Vector3f r4, float w4
    ) {
        vs.x1 = r1.x * w1 + p1.x;
        vs.y1 = r1.y * w1 + p1.y;
        vs.z1 = r1.z * w1 + p1.z;

        vs.x2 = r2.x * w2 + p2.x;
        vs.y2 = r2.y * w2 + p2.y;
        vs.z2 = r2.z * w2 + p2.z;

        vs.x3 = r3.x * w3 + p2.x;
        vs.y3 = r3.y * w3 + p2.y;
        vs.z3 = r3.z * w3 + p2.z;

        vs.x4 = r4.x * w4 + p1.x;
        vs.y4 = r4.y * w4 + p1.y;
        vs.z4 = r4.z * w4 + p1.z;
    }

    private void fillNormalScratch(SplineBody body, Matrix4f orientation, int faceIndex) {
        List<Vector3f> normals = faceNormals(body.ringSegments);
        Vector3f n = orientation.transformDirection(new Vector3f(normals.get(faceIndex)));
        ns.x = n.x;
        ns.y = n.y;
        ns.z = n.z;
    }

    private void fillUvScratch(float uSize, float uStart, float uEnd, float vStep, float vIdx) {
        uvs.u1 = uSize * uStart;
        uvs.u2 = uSize * uEnd;
        uvs.u3 = uSize * uEnd;
        uvs.u4 = uSize * uStart;
        uvs.v1 = vIdx * vStep;
        uvs.v2 = vIdx * vStep;
        uvs.v3 = vIdx * vStep + vStep;
        uvs.v4 = vIdx * vStep + vStep;
    }

    private static void emitQuad(
            VertexConsumer c, Matrix4f t, NormalScratch n,
            VertexScratch v, UvScratch uv,
            int overlay, int light1, int light2, ARGB color
    ) {
        emitVertex(c, t, n, v.x1, v.y1, v.z1, uv.u1, uv.v1, overlay, light1, color);
        emitVertex(c, t, n, v.x2, v.y2, v.z2, uv.u2, uv.v2, overlay, light2, color);
        emitVertex(c, t, n, v.x3, v.y3, v.z3, uv.u3, uv.v3, overlay, light2, color);
        emitVertex(c, t, n, v.x4, v.y4, v.z4, uv.u4, uv.v4, overlay, light1, color);
    }

    private static void emitVertex(
            VertexConsumer c, Matrix4f t, NormalScratch n,
            float x, float y, float z, float u, float v,
            int overlay, int light, ARGB color
    ) {
        c.addVertex(t, x, y, z)
                .setColor(color.decimal())
                .setUv(u, v)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(n.x, n.y, n.z);
    }

    private List<Vector3f> faceNormals(int ringSegments) {
        if (cachedRingSegments == ringSegments) return cachedFaceNormals;
        cachedFaceNormals.clear();
        double step = Math.PI * 2.0 / ringSegments;
        double start = -Math.PI / 2.0 + step / 2.0;
        for (int i = 0; i < ringSegments; i++) {
            double a = start + i * step;
            cachedFaceNormals.add(new Vector3f((float) Math.cos(a), (float) Math.sin(a), 0f));
        }
        cachedRingSegments = ringSegments;
        return cachedFaceNormals;
    }

    static final class VertexScratch {

        float x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4;
    }

    static final class NormalScratch {

        float x, y, z;
    }

    static final class UvScratch {

        float u1, u2, u3, u4, v1, v2, v3, v4;
    }

    public record CrestVisual(
            ResourceLocation texture,
            float topHeight,
            float bottomHeight,
            EasingFunction crestEasing
    ) {

        public static CrestVisual symmetric(ResourceLocation texture, float height) {
            return new CrestVisual(texture, height, height, Easing.CEIL);
        }

        public static CrestVisual symmetric(ResourceLocation texture, float height,
                                            EasingFunction crestEasing) {
            return new CrestVisual(texture, height, height, crestEasing);
        }
    }

    public interface BodyRenderLayer {

        ResourceLocation getTexture();

        RenderType getRenderType(ResourceLocation texture);

        default float getTextureRepeat() {return 1f;}

        default int getColor() {return 0xFFFFFFFF;}
    }

    public static BodyRenderLayer renderLayer(
            ResourceLocation texture,
            Function<ResourceLocation, RenderType> renderTypeFactory,
            float repeat,
            int color
    ) {
        return new BodyRenderLayer() {
            @Override
            public ResourceLocation getTexture() {return texture;}

            @Override
            public RenderType getRenderType(ResourceLocation t) {return renderTypeFactory.apply(t);}

            @Override
            public float getTextureRepeat() {return repeat;}

            @Override
            public int getColor() {return color;}
        };
    }
}