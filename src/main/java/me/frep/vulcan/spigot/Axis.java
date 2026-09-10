package me.frep.vulcan.spigot;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Map;

public enum Axis implements Predicate, Named {
    X("x", AxisDirection.HORIZONTAL),
    Y("y", AxisDirection.VERTICAL),
    Z("z", AxisDirection.HORIZONTAL);

    private static final Map BY_NAME = Maps.newHashMap();
    private final String axisName;
    private final AxisDirection plane;

    private Axis(String axisName, AxisDirection plane) {
        this.axisName = axisName;
        this.plane = plane;
    }

    public static Axis byName(String name) {
        return (Axis) BY_NAME.get(name);
    }

    public boolean isVertical() {
        return this.plane == AxisDirection.VERTICAL;
    }

    public boolean isHorizontal() {
        return this.plane == AxisDirection.HORIZONTAL;
    }

    public boolean appliesTo(BlockFace face) {
        return face != null && face.getAxis() == this;
    }

    public AxisDirection getPlane() {
        return this.plane;
    }

    @Override
    public String getName() {
        return this.axisName;
    }

    @Override
    public String toString() {
        return this.axisName;
    }

    @Override
    public boolean apply(Object object) {
        return this.appliesTo((BlockFace) object);
    }

    static {
        BY_NAME.put("x", X);
        BY_NAME.put("y", Y);
        BY_NAME.put("z", Z);
    }
}
