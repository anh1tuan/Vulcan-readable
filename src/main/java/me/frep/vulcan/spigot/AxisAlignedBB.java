package me.frep.vulcan.spigot;

import org.bukkit.Location;

/**
 * Minecraft {@code AxisAlignedBB}.
 */
public final class AxisAlignedBB {
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;
    private final long createdAtMillis;

    public AxisAlignedBB(double x, double y, double z) {
        this(x, x, y, y, z, z);
    }

    public AxisAlignedBB(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        this.createdAtMillis = System.currentTimeMillis();
        this.minX = Math.min(minX, maxX);
        this.maxX = Math.max(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxY = Math.max(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public double distanceToLocation(Location location) {
        double dx = Math.min(Math.pow(location.getX() - this.minX, 2.0), Math.pow(location.getX() - this.maxX, 2.0));
        double dz = Math.min(Math.pow(location.getZ() - this.minZ, 2.0), Math.pow(location.getZ() - this.maxZ, 2.0));
        return Math.sqrt(dx + dz);
    }

    public double distanceXZ(double x, double z) {
        double dx = Math.min(Math.pow(x - this.minX, 2.0), Math.pow(x - this.maxX, 2.0));
        double dz = Math.min(Math.pow(z - this.minZ, 2.0), Math.pow(z - this.maxZ, 2.0));
        return Math.sqrt(dx + dz);
    }

    public double distanceXZ(AxisAlignedBB other) {
        double dx = Math.min(Math.pow(other.minX - this.minX, 2.0), Math.pow(other.maxX - this.maxX, 2.0));
        double dz = Math.min(Math.pow(other.minZ - this.minZ, 2.0), Math.pow(other.maxZ - this.maxZ, 2.0));
        return Math.sqrt(dx + dz);
    }

    public AxisAlignedBB offset(AxisAlignedBB other) {
        this.minX += other.minX;
        this.minY += other.minY;
        this.minZ += other.minZ;
        this.maxX += other.maxX;
        this.maxY += other.maxY;
        this.maxZ += other.maxZ;
        return this;
    }

    public AxisAlignedBB offset(double dx, double dy, double dz) {
        this.minX += dx;
        this.minY += dy;
        this.minZ += dz;
        this.maxX += dx;
        this.maxY += dy;
        this.maxZ += dz;
        return this;
    }

    public AxisAlignedBB grow(double dx, double dy, double dz) {
        this.minX -= dx;
        this.minY -= dy;
        this.minZ -= dz;
        this.maxX += dx;
        this.maxY += dy;
        this.maxZ += dz;
        return this;
    }

    public boolean intersects(Vec3d start, Vec3d end) {
        double minRayX = Math.min(start.getX(), end.getX());
        double maxRayX = Math.max(start.getX(), end.getX());
        double minRayY = Math.min(start.getY(), end.getY());
        double maxRayY = Math.max(start.getY(), end.getY());
        double minRayZ = Math.min(start.getZ(), end.getZ());
        double maxRayZ = Math.max(start.getZ(), end.getZ());
        return this.minX < maxRayX && this.maxX > minRayX
            && this.minY < maxRayY && this.maxY > minRayY
            && this.minZ < maxRayZ && this.maxZ > minRayZ;
    }

    public boolean isEmpty() {
        return this.minX >= this.maxX || this.minY >= this.maxY || this.minZ >= this.maxZ;
    }

    public double getCenterX() {
        return (this.minX + this.maxX) / 2.0;
    }

    public double getCenterY() {
        return (this.minY + this.maxY) / 2.0;
    }

    public double getCenterZ() {
        return (this.minZ + this.maxZ) / 2.0;
    }

    public boolean intersectsWithYZ(Vec3d point) {
        return point.getY() >= this.minY && point.getY() <= this.maxY
            && point.getZ() >= this.minZ && point.getZ() <= this.maxZ;
    }

    public boolean intersectsWithXZ(Vec3d point) {
        return point.getX() >= this.minX && point.getX() <= this.maxX
            && point.getZ() >= this.minZ && point.getZ() <= this.maxZ;
    }

    public boolean intersectsWithXY(Vec3d point) {
        return point.getX() >= this.minX && point.getX() <= this.maxX
            && point.getY() >= this.minY && point.getY() <= this.maxY;
    }

    public RayTraceResult rayTrace(Vec3d start, Vec3d end) {
        Vec3d hit = collideWithXPlane(this.minX, start, end);
        BlockFace face = BlockFace.WEST;
        Vec3d candidate = collideWithXPlane(this.maxX, start, end);
        if (isCloser(start, hit, candidate)) {
            hit = candidate;
            face = BlockFace.EAST;
        }
        candidate = collideWithYPlane(this.minY, start, end);
        if (isCloser(start, hit, candidate)) {
            hit = candidate;
            face = BlockFace.DOWN;
        }
        candidate = collideWithYPlane(this.maxY, start, end);
        if (isCloser(start, hit, candidate)) {
            hit = candidate;
            face = BlockFace.UP;
        }
        candidate = collideWithZPlane(this.minZ, start, end);
        if (isCloser(start, hit, candidate)) {
            hit = candidate;
            face = BlockFace.NORTH;
        }
        candidate = collideWithZPlane(this.maxZ, start, end);
        if (isCloser(start, hit, candidate)) {
            hit = candidate;
            face = BlockFace.SOUTH;
        }
        return hit == null ? null : new RayTraceResult(hit, face);
    }

    private boolean isCloser(Vec3d origin, Vec3d current, Vec3d candidate) {
        return candidate != null && (current == null || origin.distanceSquared(candidate) < origin.distanceSquared(current));
    }

    private Vec3d collideWithXPlane(double x, Vec3d start, Vec3d end) {
        Vec3d hit = start.getIntermediateWithXValue(end, x);
        return hit != null && intersectsWithYZ(hit) ? hit : null;
    }

    private Vec3d collideWithYPlane(double y, Vec3d start, Vec3d end) {
        Vec3d hit = start.getIntermediateWithYValue(end, y);
        return hit != null && intersectsWithXZ(hit) ? hit : null;
    }

    private Vec3d collideWithZPlane(double z, Vec3d start, Vec3d end) {
        Vec3d hit = start.getIntermediateWithZValue(end, z);
        return hit != null && intersectsWithXY(hit) ? hit : null;
    }

    public AxisAlignedBB copy() {
        return new AxisAlignedBB(this.minX, this.maxX, this.minY, this.maxY, this.minZ, this.maxZ);
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getMinZ() {
        return this.minZ;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public double getMaxZ() {
        return this.maxZ;
    }

    public long getCreatedAtMillis() {
        return this.createdAtMillis;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public void setMinZ(double minZ) {
        this.minZ = minZ;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public void setMaxZ(double maxZ) {
        this.maxZ = maxZ;
    }
}
