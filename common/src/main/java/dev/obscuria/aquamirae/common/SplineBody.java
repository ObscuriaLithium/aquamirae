package dev.obscuria.aquamirae.common;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SplineBody {

    public final int ringSegments;
    public final float halfWidth;
    public final float halfHeight;

    protected List<Vector3f> snapshotOld = Collections.emptyList();
    protected List<Vector3f> snapshotNew = Collections.emptyList();

    protected final List<BakedRing> bakedOld = new ArrayList<>();
    protected final List<BakedRing> bakedNew = new ArrayList<>();

    protected boolean dirty = true;

    protected SplineBody(int ringSegments, float halfWidth, float halfHeight) {
        this.ringSegments = ringSegments;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    protected abstract List<Vector3f> collectPoints();

    public void addPoint(Vec3 point) {}

    public void addPoints(List<Vec3> points) {
        points.forEach(this::addPoint);
    }

    public void removePoint(int index) {}

    public void clearPoints() {}

    public void tick() {
        snapshotOld = snapshotNew;
        snapshotNew = copyPoints(collectPoints());
        dirty = true;
    }

    public List<BakedRing> getBakedRings(float partialTick) {
        if (dirty) {
            rebake(snapshotOld, bakedOld);
            rebake(snapshotNew, bakedNew);
            dirty = false;
        }
        return lerpBaked(bakedOld, bakedNew, partialTick);
    }

    protected void rebake(List<Vector3f> spine, List<BakedRing> out) {
        out.clear();
        if (spine.size() < 3) return;
        int last = spine.size() - 1;

        for (int i = 0; i <= last; i++) {

            Vector3f self = spine.get(i);
            Vector3f prev = i > 0 ? spine.get(i - 1) : mirror(self, spine.get(1));
            Vector3f next = i < last ? spine.get(i + 1) : mirror(self, spine.get(last - 1));
            Vector3f tangent = new Vector3f(next).sub(prev).normalize();

            Vector3f worldUp = Math.abs(tangent.y) < 0.999f
                    ? new Vector3f(0f, 1f, 0f)
                    : new Vector3f(0f, 0f, 1f);
            Vector3f right = new Vector3f(tangent).cross(worldUp).normalize();
            Vector3f up = new Vector3f(right).cross(tangent).normalize();
            Matrix4f ringMatrix = new Matrix4f(
                    right.x, right.y, right.z, 0f,
                    up.x, up.y, up.z, 0f,
                    tangent.x, tangent.y, tangent.z, 0f,
                    0f, 0f, 0f, 1f
            );

            float tween = (float) i / last;
            List<Vector3f> ring = buildRing(ringMatrix, halfWidth, halfHeight);
            out.add(new BakedRing(new Matrix4f(ringMatrix), self, ring, tween));
        }
    }

    private List<Vector3f> buildRing(Matrix4f matrix, float halfW, float halfH) {
        List<Vector3f> ring = new ArrayList<>(ringSegments);
        double step = Math.PI * 2.0 / ringSegments;
        double start = -Math.PI / 2.0;
        for (int p = 0; p < ringSegments; p++) {
            double a = start + step * p;
            float x = halfW * (float) Math.cos(a);
            float y = halfH * (float) Math.sin(a);
            ring.add(new Vector3f(x, y, 0f).mulDirection(matrix));
        }
        return ring;
    }

    private static Vector3f mirror(Vector3f self, Vector3f neighbour) {
        return new Vector3f(self).mul(2f).sub(neighbour);
    }

    public static List<BakedRing> lerpBaked(List<BakedRing> from, List<BakedRing> to, float delta) {
        int n = Math.min(from.size(), to.size());
        List<BakedRing> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(from.get(i).lerp(to.get(i), delta));
        }
        return result;
    }

    private static List<Vector3f> copyPoints(List<Vector3f> src) {
        if (src.isEmpty()) return Collections.emptyList();
        var copy = new ArrayList<Vector3f>(src.size());
        for (var vector : src) copy.add(new Vector3f(vector));
        return copy;
    }

    public static final class BakedRing {

        private final Matrix4f orientation;
        private final Vector3f position;
        private final List<Vector3f> ring;
        private final float tween;

        public BakedRing(Matrix4f orientation, Vector3f position, List<Vector3f> ring, float tween) {
            this.orientation = orientation;
            this.position = position;
            this.ring = ring;
            this.tween = tween;
        }

        public Matrix4f getOrientation() {return orientation;}

        public Vector3f getPosition() {return position;}

        public List<Vector3f> getRing() {return ring;}

        public float getTween() {return tween;}

        public BakedRing lerp(BakedRing other, float delta) {
            List<Vector3f> lerpedRing = new ArrayList<>(ring.size());
            for (int i = 0; i < ring.size(); i++) {
                lerpedRing.add(new Vector3f(ring.get(i)).lerp(other.ring.get(i), delta));
            }
            return new BakedRing(
                    new Matrix4f(orientation).lerp(other.orientation, delta),
                    new Vector3f(position).lerp(other.position, delta),
                    lerpedRing,
                    Mth.lerp(delta, tween, other.tween));
        }
    }
}
