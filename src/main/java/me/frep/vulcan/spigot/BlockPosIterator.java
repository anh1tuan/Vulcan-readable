package me.frep.vulcan.spigot;

import com.google.common.collect.AbstractIterator;

class BlockPosIterator extends AbstractIterator {
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private int cursorX;
    private int cursorY;
    private int cursorZ;
    private boolean started;

    BlockPosIterator(BlockPosBox box) {
        this.minX = Math.min(box.min.getBlockX(), box.max.getBlockX());
        this.minY = Math.min(box.min.getBlockY(), box.max.getBlockY());
        this.minZ = Math.min(box.min.getBlockZ(), box.max.getBlockZ());
        this.maxX = Math.max(box.min.getBlockX(), box.max.getBlockX());
        this.maxY = Math.max(box.min.getBlockY(), box.max.getBlockY());
        this.maxZ = Math.max(box.min.getBlockZ(), box.max.getBlockZ());
        this.cursorX = this.minX;
        this.cursorY = this.minY;
        this.cursorZ = this.minZ;
    }

    @Override
    protected Object computeNext() {
        if (!this.started) {
            this.started = true;
            return new MutableBlockVector(this.cursorX, this.cursorY, this.cursorZ);
        }
        if (this.cursorX < this.maxX) {
            ++this.cursorX;
        } else if (this.cursorY < this.maxY) {
            this.cursorX = this.minX;
            ++this.cursorY;
        } else if (this.cursorZ < this.maxZ) {
            this.cursorX = this.minX;
            this.cursorY = this.minY;
            ++this.cursorZ;
        } else {
            return this.endOfData();
        }
        return new MutableBlockVector(this.cursorX, this.cursorY, this.cursorZ);
    }
}
