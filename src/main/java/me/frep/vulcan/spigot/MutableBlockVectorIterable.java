package me.frep.vulcan.spigot;

import java.util.Iterator;

final class MutableBlockVectorIterable implements Iterable<MutableBlockVector> {
    final MutableBlockVector min;
    final MutableBlockVector max;

    MutableBlockVectorIterable(MutableBlockVector min, MutableBlockVector max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Iterator<MutableBlockVector> iterator() {
        return new MutableBlockVectorIterator(this);
    }
}
