package me.frep.vulcan.spigot;

import org.bukkit.util.Vector;

/**
 * Minecraft {@code Vec3d} used by axis-aligned ray traces.
 */
public final class Vec3d {
    public static final Vec3d ZERO = new Vec3d(0.0, 0.0, 0.0);

    private final double x;
    private final double y;
    private final double z;

    public Vec3d(double x, double y, double z) {
        this.x = canonicalizeZero(x);
        this.y = canonicalizeZero(y);
        this.z = canonicalizeZero(z);
    }

    public Vec3d(Vec3d other) {
        this(other.x, other.y, other.z);
    }

    public Vec3d(Vector vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    private static double canonicalizeZero(double value) {
        return value == -0.0 ? 0.0 : value;
    }

    public Vec3d subtract(Vec3d other) {
        return new Vec3d(other.x - this.x, other.y - this.y, other.z - this.z);
    }

    public Vec3d normalize() {
        double length = length();
        if (length < 1.0E-4) {
            return ZERO;
        }
        return new Vec3d(this.x / length, this.y / length, this.z / length);
    }

    public double dot(Vec3d other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vec3d cross(Vec3d other) {
        return new Vec3d(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x
        );
    }

    public Vec3d add(Vec3d other) {
        return add(other.x, other.y, other.z);
    }

    public Vec3d add(double dx, double dy, double dz) {
        return new Vec3d(this.x + dx, this.y + dy, this.z + dz);
    }

    public Vec3d subtract(double dx, double dy, double dz) {
        return add(-dx, -dy, -dz);
    }

    public Vec3d offset(BlockFace face) {
        return add(face.getModX(), face.getModY(), face.getModZ());
    }

    public double distance(Vec3d other) {
        return MathHelper.sqrtDouble(distanceSquared(other));
    }

    public double distanceXZ(Vec3d other) {
        double dx = other.x - this.x;
        double dz = other.z - this.z;
        return MathHelper.sqrtDouble(dx * dx + dz * dz);
    }

    public double distanceSquared(Vec3d other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distanceSquaredXZ(Vec3d other) {
        double dx = other.x - this.x;
        double dz = other.z - this.z;
        return dx * dx + dz * dz;
    }

    public double distanceY(Vec3d other) {
        return other.y - this.y;
    }

    public double length() {
        return MathHelper.sqrtDouble(lengthSquared());
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public double lengthSquaredXZ() {
        return this.x * this.x + this.z * this.z;
    }

    public Vec3d getIntermediateWithXValue(Vec3d other, double x) {
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
        return new Vec3d(this.x + dx * t, this.y + dy * t, this.z + dz * t);
    }

    public Vec3d getIntermediateWithYValue(Vec3d other, double y) {
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
        return new Vec3d(this.x + dx * t, this.y + dy * t, this.z + dz * t);
    }

    public Vec3d getIntermediateWithZValue(Vec3d other, double z) {
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
        return new Vec3d(this.x + dx * t, this.y + dy * t, this.z + dz * t);
    }

    public Vec3d rotatePitch(float pitch) {
        float cos = MathHelper.cos(pitch);
        float sin = MathHelper.sin(pitch);
        return new Vec3d(this.x, this.y * (double) cos + this.z * (double) sin, this.z * (double) cos - this.y * (double) sin);
    }

    public Vec3d rotateYaw(float yaw) {
        float cos = MathHelper.cos(yaw);
        float sin = MathHelper.sin(yaw);
        return new Vec3d(this.x * (double) cos + this.z * (double) sin, this.y, this.z * (double) cos - this.x * (double) sin);
    }

    public Vec3d scale(double factor) {
        return scale(factor, factor, factor);
    }

    public Vec3d scale(double sx, double sy, double sz) {
        return new Vec3d(this.x * sx, this.y * sy, this.z * sz);
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public int getBlockX() {
        return MathHelper.floorDouble(this.x);
    }

    public int getBlockY() {
        return MathHelper.floorDouble(this.y);
    }

    public int getBlockZ() {
        return MathHelper.floorDouble(this.z);
    }

    public float angle(Vec3d other) {
        double denominator = this.length() * other.length();
        if (denominator < 1.0E-4) {
            return 0.0f;
        }
        double cosine = this.dot(other) / denominator;
        if (cosine < -1.0) {
            cosine = -1.0;
        } else if (cosine > 1.0) {
            cosine = 1.0;
        }
        return (float) Math.acos(cosine);
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
        return "Position{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }
}
