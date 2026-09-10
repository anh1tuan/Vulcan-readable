package me.frep.vulcan.spigot;

import java.util.Iterator;
import java.util.Random;

import com.google.common.base.Predicate;


public enum AxisDirection implements Predicate, Iterable {
    HORIZONTAL,
    VERTICAL;

    public BlockFace[] getFaces() {
        if (this == HORIZONTAL) {
            return new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        }
        if (this == VERTICAL) {
            return new BlockFace[]{BlockFace.UP, BlockFace.DOWN};
        }
        throw new Error("Someone's been tampering with the universe!");
    }

    public BlockFace randomFace(Random random) {
        BlockFace[] faces = this.getFaces();
        return faces[random.nextInt(faces.length)];
    }

    public boolean test(BlockFace face) {
        return face != null && face.getAxis().getPlane() == this;
    }

    @Override
    public Iterator iterator() {
        return java.util.Arrays.asList(this.getFaces()).iterator();
    }

    @Override
    public boolean apply(Object object) {
        return this.test((BlockFace) object);
    }
}
