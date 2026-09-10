package me.frep.vulcan.spigot;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Bounded insertion-order map used as a sample window.
 */
public final class OffsetVector extends java.util.LinkedHashMap {
    private final int maxSize;
    private final Deque insertionOrder = new LinkedList();

    public OffsetVector(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public Object put(Object key, Object value) {
        if (!this.containsKey(key)) {
            this.insertionOrder.addLast(key);
            if (this.insertionOrder.size() > this.maxSize) {
                Object oldest = this.insertionOrder.removeFirst();
                super.remove(oldest);
            }
        }
        return super.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        this.insertionOrder.remove(key);
        return super.remove(key);
    }

    @Override
    public void clear() {
        this.insertionOrder.clear();
        super.clear();
    }

    public int getMaxSize() {
        return this.maxSize;
    }
}
