package me.frep.vulcan.spigot;

/**
 * Minecraft {@code Vec3i} / immutable block position.
 */
public class BlockVector implements Comparable<BlockVector> {
    public static final BlockVector ZERO = new BlockVector(0, 0, 0);

    private final int blockX;
    private final int blockY;
    private final int blockZ;

    public BlockVector(int x, int y, int z) {
        this.blockX = x;
        this.blockY = y;
        this.blockZ = z;
    }

    public BlockVector(double x, double y, double z) {
        this(MathHelper.floorDouble(x), MathHelper.floorDouble(y), MathHelper.floorDouble(z));
    }

    public int getBlockX() {
        return this.blockX;
    }

    public int getBlockY() {
        return this.blockY;
    }

    public int getBlockZ() {
        return this.blockZ;
    }

    public BlockVector crossProduct(BlockVector other) {
        return new BlockVector(
            this.blockY * other.blockZ - this.blockZ * other.blockY,
            this.blockZ * other.blockX - this.blockX * other.blockZ,
            this.blockX * other.blockY - this.blockY * other.blockX
        );
    }

    public double distanceSquared(double x, double y, double z) {
        double dx = (double) this.blockX - x;
        double dy = (double) this.blockY - y;
        double dz = (double) this.blockZ - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distanceFromCenterSquared(double x, double y, double z) {
        double dx = (double) this.blockX + 0.5 - x;
        double dy = (double) this.blockY + 0.5 - y;
        double dz = (double) this.blockZ + 0.5 - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distanceTo(Vec3 position) {
        return distanceSquared(position.getX(), position.getY(), position.getZ());
    }

    public BlockVector toImmutable() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BlockVector)) {
            return false;
        }
        BlockVector vector = (BlockVector) other;
        return this.blockX == vector.blockX && this.blockY == vector.blockY && this.blockZ == vector.blockZ;
    }

    @Override
    public int hashCode() {
        return (this.blockY + this.blockZ * 31) * 31 + this.blockX;
    }

    @Override
    public int compareTo(BlockVector other) {
        if (this.blockY == other.blockY) {
            return this.blockZ == other.blockZ ? this.blockX - other.blockX : this.blockZ - other.blockZ;
        }
        return this.blockY - other.blockY;
    }

    @Override
    public String toString() {
        return "BlockPos{x=" + this.blockX + ", y=" + this.blockY + ", z=" + this.blockZ + '}';
    }
}
