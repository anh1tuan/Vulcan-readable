package me.frep.vulcan.spigot;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;

public enum BlockFace implements Named {
    DOWN(0, 1, -1, "down", ClickAction.NEGATIVE, Axis.Y, new BlockVector(0, -1, 0)),
    UP(1, 0, -1, "up", ClickAction.POSITIVE, Axis.Y, new BlockVector(0, 1, 0)),
    NORTH(2, 3, 2, "north", ClickAction.NEGATIVE, Axis.Z, new BlockVector(0, 0, -1)),
    SOUTH(3, 2, 0, "south", ClickAction.POSITIVE, Axis.Z, new BlockVector(0, 0, 1)),
    WEST(4, 5, 1, "west", ClickAction.NEGATIVE, Axis.X, new BlockVector(-1, 0, 0)),
    EAST(5, 4, 3, "east", ClickAction.POSITIVE, Axis.X, new BlockVector(1, 0, 0));

    private static final BlockFace[] BY_INDEX = new BlockFace[6];
    private static final BlockFace[] HORIZONTAL_FACES = new BlockFace[4];
    private static final Map BY_NAME = Maps.newHashMap();
    private final int index;
    private final int oppositeIndex;
    private final int horizontalIndex;
    private final String faceName;
    private final ClickAction clickAction;
    private final Axis axis;
    private final BlockVector directionVector;
    private final int modX;
    private final int modY;
    private final int modZ;

    private BlockFace(int index, int oppositeIndex, int horizontalIndex, String faceName, ClickAction clickAction, Axis axis, BlockVector direction) {
        this.index = index;
        this.oppositeIndex = oppositeIndex;
        this.horizontalIndex = horizontalIndex;
        this.faceName = faceName;
        this.clickAction = clickAction;
        this.axis = axis;
        this.directionVector = direction;
        this.modX = direction.getBlockX();
        this.modY = direction.getBlockY();
        this.modZ = direction.getBlockZ();
    }

    public int getIndex() {
        return this.index;
    }

    public int getModX() {
        return this.modX;
    }

    public int getModY() {
        return this.modY;
    }

    public int getModZ() {
        return this.modZ;
    }

    public ClickAction getClickAction() {
        return this.clickAction;
    }

    public BlockFace getOpposite() {
        return getFace(this.oppositeIndex);
    }

    public BlockFace getFace(Axis requested) {
        if (requested == Axis.Y) {
            return this.modY > 0 ? UP : (this.modY < 0 ? DOWN : this);
        }
        if (requested == Axis.X) {
            return this.modX > 0 ? EAST : (this.modX < 0 ? WEST : this);
        }
        return this.modZ > 0 ? SOUTH : (this.modZ < 0 ? NORTH : this);
    }

    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }

    public Axis getAxis() {
        return this.axis;
    }

    public BlockVector toBlockVector() {
        return this.directionVector;
    }

    @Override
    public String getName() {
        return this.faceName;
    }

    @Override
    public String toString() {
        return this.faceName;
    }

    public static BlockFace getByName(String name) {
        return name == null ? null : (BlockFace) BY_NAME.get(name.toLowerCase());
    }

    public static BlockFace getFace(int index) {
        return BY_INDEX[Math.floorMod(index, BY_INDEX.length)];
    }

    public static BlockFace getHorizontalFace(int index) {
        return HORIZONTAL_FACES[Math.floorMod(index, HORIZONTAL_FACES.length)];
    }

    public static BlockFace fromAngle(double degrees) {
        return getHorizontalFace(MathHelper.floorDouble(degrees / 90.0 + 0.5) & 3);
    }

    public static BlockFace random(Random random) {
        return values()[random.nextInt(values().length)];
    }

    public static BlockFace fromVector(float x, float y, float z) {
        BlockFace best = NORTH;
        float bestDot = -Float.MAX_VALUE;
        BlockFace[] faces = values();
        for (int i = 0; i < faces.length; ++i) {
            BlockFace face = faces[i];
            float dot = x * (float) face.modX + y * (float) face.modY + z * (float) face.modZ;
            if (dot > bestDot) {
                bestDot = dot;
                best = face;
            }
        }
        return best;
    }

    static {
        BlockFace[] faces = values();
        for (int i = 0; i < faces.length; ++i) {
            BlockFace face = faces[i];
            BY_INDEX[face.index] = face;
            if (face.horizontalIndex >= 0) {
                HORIZONTAL_FACES[face.horizontalIndex] = face;
            }
            BY_NAME.put(face.faceName.toLowerCase(), face);
        }
    }
}
