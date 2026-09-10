package me.frep.vulcan.spigot;

import org.bukkit.entity.Boat;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;

public final class ConnectionState {
    public static boolean isBoat(Entity entity) {
        return entity instanceof Boat;
    }

    public static boolean isMinecart(Entity entity) {
        return entity.getType().toString().contains("MINECART");
    }

    public static boolean isPhantom(Entity entity) {
        return "PHANTOM".equals(entity.getType().toString());
    }

    public static boolean isDragon(Entity entity) {
        return entity instanceof EnderDragon || entity instanceof EnderDragonPart;
    }


    private ConnectionState() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
