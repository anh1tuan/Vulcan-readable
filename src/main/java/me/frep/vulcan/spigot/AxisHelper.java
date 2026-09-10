package me.frep.vulcan.spigot;

public enum AxisHelper {
    NETHERITE(9),
    DIAMOND(8),
    GOLD(12),
    IRON(6),
    STONE(4),
    WOOD(2);

    private final int level;

    AxisHelper(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
