package me.frep.vulcan.spigot;

/**
 * Minecraft {@code Vec3d} used by block-position helpers.
 */
public final class Vec3 {
    public final double x;
    public final double y;
    public final double z;

    public Vec3(double x, double y, double z) {
        this.x = canonicalizeZero(x);
        this.y = canonicalizeZero(y);
        this.z = canonicalizeZero(z);
    }

    private static double canonicalizeZero(double value) {
        return value == -0.0 ? 0.0 : value;
    }

    public Vec3 subtractReverse(Vec3 other) {
        return new Vec3(other.x - this.x, other.y - this.y, other.z - this.z);
    }

    public Vec3 normalize() {
        double length = length();
        if (length < 1.0E-4) {
            return new Vec3(0.0, 0.0, 0.0);
        }
        return new Vec3(this.x / length, this.y / length, this.z / length);
    }

    public double dot(Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vec3 cross(Vec3 other) {
        return new Vec3(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x
        );
    }

    public Vec3 add(Vec3 other) {
        return add(other.x, other.y, other.z);
    }

    public Vec3 add(double dx, double dy, double dz) {
        return new Vec3(this.x + dx, this.y + dy, this.z + dz);
    }

    public Vec3 subtract(double dx, double dy, double dz) {
        return add(-dx, -dy, -dz);
    }

    public double distance(Vec3 other) {
        return MathHelper.sqrtDouble(distanceSquared(other));
    }

    public double distanceSquared(Vec3 other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double length() {
        return MathHelper.sqrtDouble(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vec3 getIntermediateWithXValue(Vec3 other, double x) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        if (dx * dx < 1.0E-7) {
            return null;
        }
        double t = (x - this.x) / dx;
        if (t < 0.0 || t > 1.0) {
            return null;
        }
        return new Vec3(this.x + dx * t, this.y + dy * t, this.z + dz * t);
    }

    public Vec3 getIntermediateWithYValue(Vec3 other, double y) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        if (dy * dy < 1.0E-7) {
            return null;
        }
        double t = (y - this.y) / dy;
        if (t < 0.0 || t > 1.0) {
            return null;
        }
        return new Vec3(this.x + dx * t, this.y + dy * t, this.z + dz * t);
    }

    public Vec3 getIntermediateWithZValue(Vec3 other, double z) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        if (dz * dz < 1.0E-7) {
            return null;
        }
        double t = (z - this.z) / dz;
        if (t < 0.0 || t > 1.0) {
            return null;
        }
        return new Vec3(this.x + dx * t, this.y + dy * t, this.z + dz * t);
    }

    public Vec3 rotatePitch(float pitch) {
        float cos = MathHelper.cos(pitch);
        float sin = MathHelper.sin(pitch);
        return new Vec3(this.x, this.y * (double) cos + this.z * (double) sin, this.z * (double) cos - this.y * (double) sin);
    }

    public Vec3 rotateYaw(float yaw) {
        float cos = MathHelper.cos(yaw);
        float sin = MathHelper.sin(yaw);
        return new Vec3(this.x * (double) cos + this.z * (double) sin, this.y, this.z * (double) cos - this.x * (double) sin);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
