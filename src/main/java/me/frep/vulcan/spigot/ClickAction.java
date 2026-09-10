package me.frep.vulcan.spigot;

public enum ClickAction {
    POSITIVE(1),
    NEGATIVE(-1);

    private final int offset;

    private ClickAction(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return this.offset;
    }

    public String toString() {
        return this.name();
    }
}
