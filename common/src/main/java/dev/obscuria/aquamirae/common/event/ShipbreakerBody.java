package dev.obscuria.aquamirae.common.event;

import dev.obscuria.aquamirae.common.SplineBody;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShipbreakerBody extends SplineBody {

    public List<Vec3> path = Collections.emptyList();

    private final int bodyLength;
    private float headFrac = 0f;
    private int headIdx = 0;

    public ShipbreakerBody(int bodyLength, int ringSegments, float halfWidth, float halfHeight) {
        super(ringSegments, halfWidth, halfHeight);
        this.bodyLength = bodyLength;
    }

    public void setPath(List<Vec3> newPath) {
        this.path = newPath;
        this.headIdx = 0;
        this.headFrac = 0f;
    }

    public void setHeadIndex(double headIndex) {
        if (path.isEmpty()) {
            headIdx = 0;
            headFrac = 0f;
            return;
        }
        double clamped = Math.max(0.0, Math.min(headIndex, path.size() - 1.0));
        headIdx = (int) clamped;
        headFrac = (float) (clamped - headIdx);
    }

    public int headIndex() {
        return headIdx;
    }

    public double getFractionalHeadIdx() {
        return headIdx + headFrac;
    }

    @Override
    protected List<Vector3f> collectPoints() {
        return pathSlice(path, (float) getFractionalHeadIdx(), bodyLength);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void clearPoints() {
        path = Collections.emptyList();
        headIdx = 0;
        headFrac = 0f;
    }

    public static List<Vector3f> pathSlice(List<Vec3> path, float tween, int length) {
        List<Vector3f> body = new ArrayList<>(length);
        if (path.size() < 2 || length == 0) return body;

        int maxIndex = path.size() - 1;
        tween = Math.max(0f, Math.min(tween, (float) maxIndex));

        int baseIndex = (int) Math.floor(tween);
        float frac = tween - baseIndex;

        for (int i = 0; i < length; i++) {
            int idx = baseIndex - i;

            Vec3 a = getClamped(path, idx, maxIndex);
            Vec3 b = getClamped(path, idx + 1, maxIndex);

            double x = a.x + (b.x - a.x) * frac;
            double y = a.y + (b.y - a.y) * frac;
            double z = a.z + (b.z - a.z) * frac;

            body.add(new Vector3f((float) x, (float) y, (float) z));
        }

        return body;
    }

    private static Vec3 getClamped(List<Vec3> path, int index, int maxIndex) {
        if (index < 0) index = 0;
        if (index > maxIndex) index = maxIndex;
        return path.get(index);
    }
}