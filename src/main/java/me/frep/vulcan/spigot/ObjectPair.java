package me.frep.vulcan.spigot;

public class ObjectPair {
    private final Object key;
    private final Object value;

    public ObjectPair(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return this.key;
    }

    public Object getValue() {
        return this.value;
    }
}
