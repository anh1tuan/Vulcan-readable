package me.frep.vulcan.spigot;

import com.google.common.collect.AbstractIterator;

final class MutableBlockVectorIterator extends AbstractIterator<MutableBlockVector> {
    private MutableBlockVector current;
    private final MutableBlockVectorIterable box;

    MutableBlockVectorIterator(MutableBlockVectorIterable box) {
        this.box = box;
        this.current = null;
    }

    @Override
    protected MutableBlockVector computeNext() {
        if (this.current == null) {
            this.current = this.box.min;
            return this.current;
        }
        if (this.current.equals(this.box.max)) {
            return this.endOfData();
        }
        int x = this.current.getBlockX();
        int y = this.current.getBlockY();
        int z = this.current.getBlockZ();
        if (x < this.box.max.getBlockX()) {
            ++x;
        } else if (y < this.box.max.getBlockY()) {
            x = this.box.min.getBlockX();
            ++y;
        } else if (z < this.box.max.getBlockZ()) {
            x = this.box.min.getBlockX();
            y = this.box.min.getBlockY();
            ++z;
        }
        this.current = new MutableBlockVector(x, y, z);
        return this.current;
    }
}
