package me.frep.vulcan.spigot;

import org.bukkit.Location;

public final class ReachTarget {
    private final Location location;
    private final int tick;

    public ReachTarget(Location location, int tick) {
        this.location = location;
        this.tick = tick;
    }

    public Location getLocation() {
        return this.location;
    }

    public int getTick() {
        return this.tick;
    }
}
