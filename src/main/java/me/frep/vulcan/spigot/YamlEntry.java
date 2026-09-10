package me.frep.vulcan.spigot;

final class YamlEntry {
    private final Object key;
    private final Object value;

    YamlEntry(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    Object getKey() {
        return this.key;
    }

    Object getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof YamlEntry)) {
            return false;
        }
        YamlEntry entry = (YamlEntry) other;
        return java.util.Objects.equals(this.key, entry.key)
            && java.util.Objects.equals(this.value, entry.value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.key, this.value);
    }
}
