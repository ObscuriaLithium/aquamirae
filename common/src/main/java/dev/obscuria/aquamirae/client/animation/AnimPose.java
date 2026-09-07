package dev.obscuria.aquamirae.client.animation;

public final class AnimPose {

    static final float DEG2RAD = (float) (Math.PI / 180.0);

    final float xRot, yRot, zRot;
    final float xScale, yScale, zScale;

    private AnimPose(float xRot, float yRot, float zRot, float xScale, float yScale, float zScale) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
    }

    static AnimPose identity() {
        return new AnimPose(0, 0, 0, 1, 1, 1);
    }

    public static final class Builder {

        float xBase = 0, xSwing = 0, xSpeed = 1, xOffset = 0;
        float yBase = 0, ySwing = 0, ySpeed = 1, yOffset = 0;
        float zBase = 0, zSwing = 0, zSpeed = 1, zOffset = 0;

        float xScaleBase = 1, xScaleSwing = 0, xScaleSpeed = 1, xScaleOffset = 0;
        float yScaleBase = 1, yScaleSwing = 0, yScaleSpeed = 1, yScaleOffset = 0;
        float zScaleBase = 1, zScaleSwing = 0, zScaleSpeed = 1, zScaleOffset = 0;

        public Builder rotation(float x, float y, float z) {
            this.xBase = x;
            this.yBase = y;
            this.zBase = z;
            return this;
        }

        public Builder rotation(float xSwing, float x, float ySwing, float y, float zSwing, float z, float offset) {
            return rotation(xSwing, x, ySwing, y, zSwing, z, 1f, offset);
        }

        public Builder rotation(float xSwing, float x, float ySwing, float y, float zSwing, float z, float speed, float offset) {
            xRot(xSwing, x, speed, offset);
            yRot(ySwing, y, speed, offset);
            zRot(zSwing, z, speed, offset);
            return this;
        }

        public Builder xRot(float base) {
            this.xSwing = 0;
            this.xBase = base;
            this.xSpeed = 1;
            this.xOffset = 0;
            return this;
        }

        public Builder xRot(float swing, float base) {
            this.xSwing = swing;
            this.xBase = base;
            this.xSpeed = 1;
            this.xOffset = 0;
            return this;
        }

        public Builder xRot(float swing, float base, float offset) {
            this.xSwing = swing;
            this.xBase = base;
            this.xSpeed = 1;
            this.xOffset = offset;
            return this;
        }

        public Builder xRot(float swing, float base, float speed, float offset) {
            this.xSwing = swing;
            this.xBase = base;
            this.xSpeed = speed;
            this.xOffset = offset;
            return this;
        }

        public Builder yRot(float base) {
            this.ySwing = 0;
            this.yBase = base;
            this.ySpeed = 1;
            this.yOffset = 0;
            return this;
        }

        public Builder yRot(float swing, float base) {
            this.ySwing = swing;
            this.yBase = base;
            this.ySpeed = 1;
            this.yOffset = 0;
            return this;
        }

        public Builder yRot(float swing, float base, float offset) {
            this.ySwing = swing;
            this.yBase = base;
            this.ySpeed = 1;
            this.yOffset = offset;
            return this;
        }

        public Builder yRot(float swing, float base, float speed, float offset) {
            this.ySwing = swing;
            this.yBase = base;
            this.ySpeed = speed;
            this.yOffset = offset;
            return this;
        }

        public Builder zRot(float base) {
            this.zSwing = 0;
            this.zBase = base;
            this.zSpeed = 1;
            this.zOffset = 0;
            return this;
        }

        public Builder zRot(float swing, float base) {
            this.zSwing = swing;
            this.zBase = base;
            this.zSpeed = 1;
            this.zOffset = 0;
            return this;
        }

        public Builder zRot(float swing, float base, float offset) {
            this.zSwing = swing;
            this.zBase = base;
            this.zSpeed = 1;
            this.zOffset = offset;
            return this;
        }

        public Builder zRot(float swing, float base, float speed, float offset) {
            this.zSwing = swing;
            this.zBase = base;
            this.zSpeed = speed;
            this.zOffset = offset;
            return this;
        }

        public Builder scale(float s) {
            this.xScaleBase = yScaleBase = zScaleBase = s;
            return this;
        }

        public Builder scale(float x, float y, float z) {
            this.xScaleBase = x;
            this.yScaleBase = y;
            this.zScaleBase = z;
            return this;
        }

        public Builder xScale(float base) {
            this.xScaleBase = base;
            return this;
        }

        public Builder yScale(float base) {
            this.yScaleBase = base;
            return this;
        }

        public Builder zScale(float base) {
            this.zScaleBase = base;
            return this;
        }

        AnimPose evaluate(float timer, float speed) {
            return new AnimPose(
                    evalRot(xBase, xSwing, xSpeed, xOffset, timer, speed),
                    evalRot(yBase, ySwing, ySpeed, yOffset, timer, speed),
                    evalRot(zBase, zSwing, zSpeed, zOffset, timer, speed),
                    evalScale(xScaleBase, xScaleSwing, xScaleSpeed, xScaleOffset, timer, speed),
                    evalScale(yScaleBase, yScaleSwing, yScaleSpeed, yScaleOffset, timer, speed),
                    evalScale(zScaleBase, zScaleSwing, zScaleSpeed, zScaleOffset, timer, speed)
            );
        }

        static float evalRot(float base, float swing, float lsp, float off, float timer, float speed) {
            return (float) ((swing * Math.cos(timer * speed * lsp + off * 6.2832f) + base) * DEG2RAD);
        }

        static float evalScale(float base, float swing, float lsp, float off, float timer, float speed) {
            return swing * (float) Math.cos(timer * speed * lsp + off * 6.2832f) + base;
        }
    }
}
