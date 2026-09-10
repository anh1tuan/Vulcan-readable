package me.frep.vulcan.spigot;

import java.util.Collection;
import java.util.LinkedList;

public final class SampleDeque extends LinkedList {
    private final int maxSize;

    public SampleDeque(int maxSize) {
        this.maxSize = maxSize;
    }

    public SampleDeque(Collection collection, int maxSize) {
        super(collection);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(Object object) {
        if (this.size() >= this.maxSize) {
            this.removeFirst();
        }
        return super.add(object);
    }

    public boolean isFull() {
        return this.size() >= this.maxSize;
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public double average() {
        if (this.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Object sample : this) {
            if (sample instanceof Number) {
                sum += ((Number) sample).doubleValue();
            }
        }
        return sum / (double) this.size();
    }
}
