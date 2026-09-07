package dev.obscuria.aquamirae.client.animation;

import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import dev.obscuria.fragmentum.content.util.easing.EasingFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class AnimPipeline<E extends Entity> {

    public record Context(
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            float partialTick
    ) {

        public Context(
                float limbSwing, float limbSwingAmount, float ageInTicks,
                float netHeadYaw, float headPitch
        ) {
            this(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, AquamiraeClient.partialTicks());
        }
    }

    public enum ContextVar {
        LIMB_SWING(Context::limbSwing),
        LIMB_SWING_AMOUNT(Context::limbSwingAmount),
        AGE_IN_TICKS(Context::ageInTicks),
        NET_HEAD_YAW(Context::netHeadYaw),
        HEAD_PITCH(Context::headPitch),
        PARTIAL_TICK(Context::partialTick);

        private final Function<Context, Float> getter;

        ContextVar(Function<Context, Float> getter) {
            this.getter = getter;
        }

        public float get(Context context) {
            return getter.apply(context);
        }
    }

    public static final class Mod {

        public static final WeightFn ALWAYS = context -> 1f;

        public static WeightFn idle(float sensitivity) {
            return context -> Math.max(0f, 1f - context.limbSwingAmount() * sensitivity);
        }

        public static WeightFn walk(float sensitivity) {
            return context -> Math.min(1f, context.limbSwingAmount() * sensitivity);
        }
    }

    @FunctionalInterface
    public interface WeightFn {

        float get(Context ctx);
    }

    public enum Mode {
        DEFINE,
        ADD,
        BLEND
    }

    private final List<ModelPart> resetParts = new ArrayList<>();
    private final List<AmbientLayer> ambientLayers = new ArrayList<>();
    private final List<ClipLayer<E>> clipLayers = new ArrayList<>();

    public AnimPipeline<E> resetParts(ModelPart... parts) {
        this.resetParts.addAll(Arrays.asList(parts));
        return this;
    }

    public AmbientBuilder ambient(ContextVar timer, float speed, WeightFn weight, Mode mode) {
        var layer = new AmbientLayer(timer, speed, weight, mode);
        this.ambientLayers.add(layer);
        return new AmbientBuilder(layer);
    }

    public AmbientBuilder ambientIdle(float speed, float sensitivity) {
        return ambient(ContextVar.AGE_IN_TICKS, speed, Mod.idle(sensitivity), Mode.DEFINE);
    }

    public AmbientBuilder ambientWalk(float speed, float sensitivity) {
        return ambient(ContextVar.LIMB_SWING, speed, Mod.walk(sensitivity), Mode.ADD);
    }

    public ClipBuilder clip(
            Function<E, AnimState> stateExtractor,
            int fadeInTicks, int fadeOutTicks,
            EasingFunction fadeIn, EasingFunction fadeOut
    ) {
        var layer = new ClipLayer<>(stateExtractor, fadeInTicks, fadeOutTicks, fadeIn, fadeOut);
        this.clipLayers.add(layer);
        return new ClipBuilder(layer);
    }

    public void evaluate(E entity, Context ctx) {
        for (var part : resetParts) {
            part.setRotation(0f, 0f, 0f);
            part.xScale = 1f;
            part.yScale = 1f;
            part.zScale = 1f;
        }
        for (var ambientLayer : ambientLayers) {
            ambientLayer.evaluate(ctx);
        }
        for (var clipLayer : clipLayers) {
            clipLayer.evaluate(entity, ctx);
        }
    }

    private static final class AmbientLayer {

        private final ContextVar timerVar;
        private final float speed;
        private final WeightFn weightFn;
        private final Mode mode;

        private final List<ModelPart> parts = new ArrayList<>();
        private final List<AmbientKeyframe> kfs = new ArrayList<>();

        AmbientLayer(ContextVar timerVar, float speed, WeightFn weightFn, Mode mode) {
            this.timerVar = timerVar;
            this.speed = speed;
            this.weightFn = weightFn;
            this.mode = mode;
        }

        void evaluate(Context ctx) {
            float weight = weightFn.get(ctx);
            if (weight <= 0f) return;
            float timer = timerVar.get(ctx);
            for (int i = 0; i < parts.size(); i++) apply(parts.get(i), kfs.get(i), timer, weight);
        }

        private void apply(ModelPart part, AmbientKeyframe k, float timer, float weight) {
            part.xRot = computeRot(mode, part.xRot, -k.xSwing, -k.xBase, k.xSpeed, k.xOffset, timer, speed, weight);
            part.yRot = computeRot(mode, part.yRot, -k.ySwing, -k.yBase, k.ySpeed, k.yOffset, timer, speed, weight);
            part.zRot = computeRot(mode, part.zRot, k.zSwing, k.zBase, k.zSpeed, k.zOffset, timer, speed, weight);
            if (k.hasScale) {
                part.xScale = computeScale(mode, part.xScale, k.xScaleSwing, k.xScaleBase, k.xScaleSpeed, k.xScaleOffset, timer, speed, weight);
                part.yScale = computeScale(mode, part.yScale, k.yScaleSwing, k.yScaleBase, k.yScaleSpeed, k.yScaleOffset, timer, speed, weight);
                part.zScale = computeScale(mode, part.zScale, k.zScaleSwing, k.zScaleBase, k.zScaleSpeed, k.zScaleOffset, timer, speed, weight);
            }
        }

        private static float computeRot(
                Mode mode, float old, float swing, float base, float axisSpeed,
                float offset, float timer, float speed, float weight
        ) {
            float rad = (float) Math.toRadians(swing * weight * Math.cos(timer * speed * axisSpeed + offset * 6.2832f) + base * weight);
            return switch (mode) {
                case DEFINE -> rad;
                case ADD -> old + rad;
                case BLEND -> old * (1f - weight) + rad;
            };
        }

        private static float computeScale(
                Mode mode, float old, float swing, float base, float axisSpeed,
                float offset, float timer, float speed, float weight
        ) {
            float val = swing * weight * (float) Math.cos(timer * speed * axisSpeed + offset * 6.2832f) + base * weight;
            return switch (mode) {
                case DEFINE -> val;
                case ADD -> old + val;
                case BLEND -> old * (1f - weight) + val;
            };
        }
    }

    public static final class AmbientKeyframe {

        float xSwing, xBase, xSpeed = 1, xOffset;
        float ySwing, yBase, ySpeed = 1, yOffset;
        float zSwing, zBase, zSpeed = 1, zOffset;

        boolean hasScale;
        float xScaleSwing, xScaleBase = 1, xScaleSpeed = 1, xScaleOffset;
        float yScaleSwing, yScaleBase = 1, yScaleSpeed = 1, yScaleOffset;
        float zScaleSwing, zScaleBase = 1, zScaleSpeed = 1, zScaleOffset;

        public AmbientKeyframe rotation(float xs, float x, float ys, float y, float zs, float z) {
            return rotation(xs, x, ys, y, zs, z, 1, 0);
        }

        public AmbientKeyframe rotation(float xs, float x, float ys, float y, float zs, float z, float offset) {
            return rotation(xs, x, ys, y, zs, z, 1, offset);
        }

        public AmbientKeyframe rotation(float xs, float x, float ys, float y, float zs, float z, float sp, float offset) {
            xRot(xs, x, sp, offset);
            yRot(ys, y, sp, offset);
            zRot(zs, z, sp, offset);
            return this;
        }

        public AmbientKeyframe xRot(float b) {
            xSwing = 0;
            xBase = b;
            xSpeed = 1;
            xOffset = 0;
            return this;
        }

        public AmbientKeyframe xRot(float s, float b) {
            xSwing = s;
            xBase = b;
            xSpeed = 1;
            xOffset = 0;
            return this;
        }

        public AmbientKeyframe xRot(float s, float b, float o) {
            xSwing = s;
            xBase = b;
            xSpeed = 1;
            xOffset = o;
            return this;
        }

        public AmbientKeyframe xRot(float s, float b, float sp, float o) {
            xSwing = s;
            xBase = b;
            xSpeed = sp;
            xOffset = o;
            return this;
        }

        public AmbientKeyframe yRot(float b) {
            ySwing = 0;
            yBase = b;
            ySpeed = 1;
            yOffset = 0;
            return this;
        }

        public AmbientKeyframe yRot(float s, float b) {
            ySwing = s;
            yBase = b;
            ySpeed = 1;
            yOffset = 0;
            return this;
        }

        public AmbientKeyframe yRot(float s, float b, float o) {
            ySwing = s;
            yBase = b;
            ySpeed = 1;
            yOffset = o;
            return this;
        }

        public AmbientKeyframe yRot(float s, float b, float sp, float o) {
            ySwing = s;
            yBase = b;
            ySpeed = sp;
            yOffset = o;
            return this;
        }

        public AmbientKeyframe zRot(float b) {
            zSwing = 0;
            zBase = b;
            zSpeed = 1;
            zOffset = 0;
            return this;
        }

        public AmbientKeyframe zRot(float s, float b) {
            zSwing = s;
            zBase = b;
            zSpeed = 1;
            zOffset = 0;
            return this;
        }

        public AmbientKeyframe zRot(float s, float b, float o) {
            zSwing = s;
            zBase = b;
            zSpeed = 1;
            zOffset = o;
            return this;
        }

        public AmbientKeyframe zRot(float s, float b, float sp, float o) {
            zSwing = s;
            zBase = b;
            zSpeed = sp;
            zOffset = o;
            return this;
        }

        public AmbientKeyframe scale(float s) {
            hasScale = true;
            xScaleBase = yScaleBase = zScaleBase = s;
            return this;
        }

        public AmbientKeyframe scale(float x, float y, float z) {
            hasScale = true;
            xScaleBase = x;
            yScaleBase = y;
            zScaleBase = z;
            return this;
        }

        public AmbientKeyframe xScale(float b) {
            hasScale = true;
            xScaleBase = b;
            return this;
        }

        public AmbientKeyframe yScale(float b) {
            hasScale = true;
            yScaleBase = b;
            return this;
        }

        public AmbientKeyframe zScale(float b) {
            hasScale = true;
            zScaleBase = b;
            return this;
        }
    }

    private static final class ClipLayer<E extends Entity> {

        private final Function<E, AnimState> stateExtractor;
        private final int fadeInTicks;
        private final int fadeOutTicks;
        private final EasingFunction fadeIn;
        private final EasingFunction fadeOut;
        private final List<PoseEntry> poses = new ArrayList<>();

        ClipLayer(
                Function<E, AnimState> stateExtractor, int fadeInTicks,
                int fadeOutTicks, EasingFunction fadeIn, EasingFunction fadeOut
        ) {
            this.stateExtractor = stateExtractor;
            this.fadeInTicks = fadeInTicks;
            this.fadeOutTicks = fadeOutTicks;
            this.fadeIn = fadeIn;
            this.fadeOut = fadeOut;
        }

        void evaluate(E entity, Context ctx) {
            var state = stateExtractor.apply(entity);
            if (!state.isPlaying()) return;

            final int tick = state.getTick();
            final int duration = state.getDuration();
            final float pt = ctx.partialTick();
            final float gw = globalWeight(tick, duration, pt);

            @Nullable PoseEntry lastEntry = null;
            for (PoseEntry entry : poses) {
                if (tick >= entry.start && tick < entry.end) {
                    if (lastEntry != null) {
                        float w = Math.max(0f, Math.min(1f, lastEntry.easing.compute(1f))) * gw;
                        applyPose(lastEntry, w, ctx);
                    }
                    float lp = ((float) (tick - entry.start) + pt) / (float) (entry.end - entry.start);
                    float w = Math.max(0f, Math.min(1f, entry.easing.compute(lp))) * gw;
                    applyPose(entry, w, ctx);
                }
                lastEntry = entry;
            }
        }

        private float globalWeight(int tick, int duration, float pt) {
            if (tick < fadeInTicks)
                return point(tick, fadeInTicks, fadeIn, pt);
            if (tick < duration - fadeOutTicks)
                return 1f;
            return 1f - point(tick - (duration - fadeOutTicks), fadeOutTicks, fadeOut, pt);
        }

        private static float point(int tick, int dur, EasingFunction fn, float pt) {
            return dur == 0 ? 1f : fn.compute(((float) tick + pt) / (float) dur);
        }

        private static void applyPose(PoseEntry entry, float weight, Context ctx) {
            float timer = entry.timerVar.get(ctx);
            float speed = entry.speed;
            for (int i = 0; i < entry.parts.size(); i++) {
                ModelPart part = entry.parts.get(i);
                AnimPose.Builder b = entry.builders.get(i);
                part.xRot += (evalRot(-b.xBase, -b.xSwing, b.xSpeed, b.xOffset, timer, speed) - part.xRot) * weight;
                part.yRot += (evalRot(-b.yBase, -b.ySwing, b.ySpeed, b.yOffset, timer, speed) - part.yRot) * weight;
                part.zRot += (evalRot(b.zBase, b.zSwing, b.zSpeed, b.zOffset, timer, speed) - part.zRot) * weight;
                part.xScale += (evalScale(b.xScaleBase, b.xScaleSwing, b.xScaleSpeed, b.xScaleOffset, timer, speed) - part.xScale) * weight;
                part.yScale += (evalScale(b.yScaleBase, b.yScaleSwing, b.yScaleSpeed, b.yScaleOffset, timer, speed) - part.yScale) * weight;
                part.zScale += (evalScale(b.zScaleBase, b.zScaleSwing, b.zScaleSpeed, b.zScaleOffset, timer, speed) - part.zScale) * weight;
            }
        }

        private static float evalRot(float base, float swing, float lsp, float off, float timer, float speed) {
            return (float) ((swing * Math.cos(timer * speed * lsp + off * 6.2832f) + base) * AnimPose.DEG2RAD);
        }

        private static float evalScale(float base, float swing, float lsp, float off, float timer, float speed) {
            return swing * (float) Math.cos(timer * speed * lsp + off * 6.2832f) + base;
        }
    }

    private static final class PoseEntry {

        final int start;
        final int end;
        final EasingFunction easing;
        final ContextVar timerVar;
        final float speed;
        final List<ModelPart> parts = new ArrayList<>();
        final List<AnimPose.Builder> builders = new ArrayList<>();

        PoseEntry(int start, int end, EasingFunction easing, ContextVar timerVar, float speed) {
            this.start = start;
            this.end = end;
            this.easing = easing;
            this.timerVar = timerVar;
            this.speed = speed;
        }
    }

    public final class AmbientBuilder {

        private final AmbientLayer layer;

        private AmbientBuilder(AmbientLayer layer) {this.layer = layer;}

        public AmbientBuilder keyframe(ModelPart part, Consumer<AmbientKeyframe> consumer) {
            AmbientKeyframe kf = new AmbientKeyframe();
            consumer.accept(kf);
            layer.parts.add(part);
            layer.kfs.add(kf);
            return this;
        }

        public AnimPipeline<E> end() {
            return AnimPipeline.this;
        }
    }

    public final class ClipBuilder {

        private final ClipLayer<E> layer;

        private ClipBuilder(ClipLayer<E> layer) {
            this.layer = layer;
        }

        public PoseSegmentBuilder pose(
                int startTick, int endTick,
                EasingFunction easing,
                ContextVar timerVar, float speed
        ) {
            PoseEntry entry = new PoseEntry(startTick, endTick, easing, timerVar, speed);
            layer.poses.add(entry);
            return new PoseSegmentBuilder(this, entry);
        }

        public PoseSegmentBuilder pose(int startTick, int endTick, EasingFunction easing) {
            return pose(startTick, endTick, easing, ContextVar.AGE_IN_TICKS, 0f);
        }

        public AnimPipeline<E> end() {
            return AnimPipeline.this;
        }
    }

    public final class PoseSegmentBuilder {

        private final ClipBuilder parent;
        private final PoseEntry entry;

        private PoseSegmentBuilder(ClipBuilder parent, PoseEntry entry) {
            this.parent = parent;
            this.entry = entry;
        }

        public PoseSegmentBuilder keyframe(ModelPart part, Consumer<AnimPose.Builder> consumer) {
            AnimPose.Builder b = new AnimPose.Builder();
            consumer.accept(b);
            entry.parts.add(part);
            entry.builders.add(b);
            return this;
        }

        public ClipBuilder end() {
            return parent;
        }
    }
}