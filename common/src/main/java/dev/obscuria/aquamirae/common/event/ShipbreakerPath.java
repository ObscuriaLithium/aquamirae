package dev.obscuria.aquamirae.common.event;

import dev.obscuria.aquamirae.registry.AquamiraeRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ShipbreakerPath {

    public static final float POINT_SPACING = 1.0f;
    public static final int BODY_LENGTH = 350;

    private final List<Vec3> points;

    private ShipbreakerPath(List<Vec3> points) {
        this.points = Collections.unmodifiableList(points);
    }

    public List<Vec3> points() {
        return points;
    }

    public int size() {
        return points.size();
    }

    public static ShipbreakerPath buildAround(ServerLevel level, Vec3 playerPos) {
        var registry = level.registryAccess().lookupOrThrow(AquamiraeRegistries.Key.SHIPBREAKER_PATH);
        var path = registry.getOrThrow(PathDefinition.DEFAULT);
        var controlPoints = path.value().resolve(level, playerPos);
        return new ShipbreakerPath(densify(controlPoints));
    }

    private static List<Vec3> densify(List<Vec3> ctrl) {
        if (ctrl.size() < 2) return ctrl;

        double chordLength = 0;
        for (int i = 0; i < ctrl.size() - 1; i++) {
            chordLength += ctrl.get(i).distanceTo(ctrl.get(i + 1));
        }

        int minSamplesForResolution = (int) Math.ceil(chordLength / POINT_SPACING) * 8;
        int fineSamples = Math.max(ctrl.size() * 32, minSamplesForResolution);
        List<Vec3> fine = catmullRomSample(ctrl, fineSamples);

        double[] cumDist = new double[fine.size()];
        cumDist[0] = 0;
        for (int i = 1; i < fine.size(); i++) {
            cumDist[i] = cumDist[i - 1] + fine.get(i - 1).distanceTo(fine.get(i));
        }

        double totalLength = cumDist[fine.size() - 1];
        int pointCount = (int) Math.ceil(totalLength / POINT_SPACING) + 1;

        List<Vec3> result = new ArrayList<>(pointCount);
        int j = 0;
        for (int i = 0; i < pointCount; i++) {
            double targetDist = i * POINT_SPACING;
            if (targetDist >= totalLength) {
                result.add(fine.get(fine.size() - 1));
                break;
            }
            while (j + 1 < fine.size() - 1 && cumDist[j + 1] < targetDist) j++;
            double segStart = cumDist[j], segEnd = cumDist[j + 1];
            double t = (segEnd > segStart) ? (targetDist - segStart) / (segEnd - segStart) : 0;
            result.add(lerpVec(fine.get(j), fine.get(j + 1), t));
        }
        return result;
    }

    private static List<Vec3> catmullRomSample(List<Vec3> pts, int totalSamples) {
        List<Vec3> result = new ArrayList<>(totalSamples);
        int n = pts.size();
        int segments = n - 1;
        for (int i = 0; i < totalSamples; i++) {
            if (i == totalSamples - 1) {
                result.add(pts.get(n - 1));
                continue;
            }
            double globalT = (double) i / (totalSamples - 1) * segments;
            int seg = Math.min((int) globalT, segments - 1);
            double localT = globalT - seg;
            Vec3 p0 = pts.get(Math.max(seg - 1, 0));
            Vec3 p1 = pts.get(seg);
            Vec3 p2 = pts.get(Math.min(seg + 1, n - 1));
            Vec3 p3 = pts.get(Math.min(seg + 2, n - 1));
            result.add(catmullRom(p0, p1, p2, p3, localT));
        }
        return result;
    }

    private static Vec3 catmullRom(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, double t) {
        double t2 = t * t, t3 = t2 * t;
        return new Vec3(
                crComp(p0.x, p1.x, p2.x, p3.x, t, t2, t3),
                crComp(p0.y, p1.y, p2.y, p3.y, t, t2, t3),
                crComp(p0.z, p1.z, p2.z, p3.z, t, t2, t3));
    }

    private static double crComp(double a, double b, double c, double d, double t, double t2, double t3) {
        return 0.5 * (2 * b + (c - a) * t + (2 * a - 5 * b + 4 * c - d) * t2 + (-a + 3 * b - 3 * c + d) * t3);
    }

    private static Vec3 lerpVec(Vec3 a, Vec3 b, double t) {
        return new Vec3(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t, a.z + (b.z - a.z) * t);
    }
}