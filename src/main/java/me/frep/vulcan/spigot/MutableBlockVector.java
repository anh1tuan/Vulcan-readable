package me.frep.vulcan.spigot;

import org.bukkit.entity.Entity;

/**
 * Minecraft mutable {@code BlockPos}.
 */
public class MutableBlockVector extends BlockVector {
    public static final MutableBlockVector ORIGIN = new MutableBlockVector(0, 0, 0);

    private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public MutableBlockVector(int x, int y, int z) {
        super(x, y, z);
    }

    public MutableBlockVector(double x, double y, double z) {
        super(x, y, z);
    }

    public MutableBlockVector(Entity entity) {
        this(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ());
    }

    public MutableBlockVector(Vec3 position) {
        this(position.x, position.y, position.z);
    }

    public MutableBlockVector(BlockVector position) {
        this(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    public MutableBlockVector add(double dx, double dy, double dz) {
        return new MutableBlockVector((double) getBlockX() + dx, (double) getBlockY() + dy, (double) getBlockZ() + dz);
    }

    public MutableBlockVector add(int dx, int dy, int dz) {
        return new MutableBlockVector(getBlockX() + dx, getBlockY() + dy, getBlockZ() + dz);
    }

    public MutableBlockVector add(BlockVector other) {
        return add(other.getBlockX(), other.getBlockY(), other.getBlockZ());
    }

    public MutableBlockVector subtract(BlockVector other) {
        return add(-other.getBlockX(), -other.getBlockY(), -other.getBlockZ());
    }

    public MutableBlockVector multiply(int factor) {
        return new MutableBlockVector(getBlockX() * factor, getBlockY() * factor, getBlockZ() * factor);
    }

    public MutableBlockVector offset(BlockFace face) {
        return offset(face, 1);
    }

    public MutableBlockVector offset(BlockFace face, int distance) {
        return new MutableBlockVector(
            getBlockX() + face.getModX() * distance,
            getBlockY() + face.getModY() * distance,
            getBlockZ() + face.getModZ() * distance
        );
    }

    public MutableBlockVector offsetUp() {
        return offset(BlockFace.UP);
    }

    public MutableBlockVector offsetUp(int distance) {
        return offset(BlockFace.UP, distance);
    }

    public MutableBlockVector offsetDown() {
        return offset(BlockFace.DOWN);
    }

    public MutableBlockVector offsetDown(int distance) {
        return offset(BlockFace.DOWN, distance);
    }

    public MutableBlockVector offsetNorth() {
        return offset(BlockFace.NORTH);
    }

    public MutableBlockVector offsetNorth(int distance) {
        return offset(BlockFace.NORTH, distance);
    }

    public MutableBlockVector offsetSouth() {
        return offset(BlockFace.SOUTH);
    }

    public MutableBlockVector offsetSouth(int distance) {
        return offset(BlockFace.SOUTH, distance);
    }

    public MutableBlockVector offsetWest() {
        return offset(BlockFace.WEST);
    }

    public MutableBlockVector offsetWest(int distance) {
        return offset(BlockFace.WEST, distance);
    }

    public MutableBlockVector offsetEast() {
        return offset(BlockFace.EAST);
    }

    public MutableBlockVector offsetEast(int distance) {
        return offset(BlockFace.EAST, distance);
    }

    public MutableBlockVector crossProduct(BlockVector other) {
        return new MutableBlockVector(
            getBlockY() * other.getBlockZ() - getBlockZ() * other.getBlockY(),
            getBlockZ() * other.getBlockX() - getBlockX() * other.getBlockZ(),
            getBlockX() * other.getBlockY() - getBlockY() * other.getBlockX()
        );
    }

    public long toPackedLong() {
        return ((long) getBlockX() & X_MASK) << X_SHIFT
            | ((long) getBlockY() & Y_MASK) << Y_SHIFT
            | ((long) getBlockZ() & Z_MASK);
    }

    public static MutableBlockVector fromPackedLong(long packed) {
        int x = (int) (packed << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int y = (int) (packed << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int z = (int) (packed << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new MutableBlockVector(x, y, z);
    }

    public static Iterable<MutableBlockVector> between(MutableBlockVector from, MutableBlockVector to) {
        MutableBlockVector min = new MutableBlockVector(
            Math.min(from.getBlockX(), to.getBlockX()),
            Math.min(from.getBlockY(), to.getBlockY()),
            Math.min(from.getBlockZ(), to.getBlockZ())
        );
        MutableBlockVector max = new MutableBlockVector(
            Math.max(from.getBlockX(), to.getBlockX()),
            Math.max(from.getBlockY(), to.getBlockY()),
            Math.max(from.getBlockZ(), to.getBlockZ())
        );
        return new MutableBlockVectorIterable(min, max);
    }

    @Override
    public BlockVector toImmutable() {
        return new BlockVector(getBlockX(), getBlockY(), getBlockZ());
    }
}
