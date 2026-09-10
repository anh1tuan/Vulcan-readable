package me.frep.vulcan.spigot;

import java.util.Iterator;

final class BlockPosBox implements Iterable {
    final MutableBlockVector min;
    final MutableBlockVector max;

    BlockPosBox(MutableBlockVector min, MutableBlockVector max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Iterator iterator() {
        return new BlockPosIterator(this);
    }
}
