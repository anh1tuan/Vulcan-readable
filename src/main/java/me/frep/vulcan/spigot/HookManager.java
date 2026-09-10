package me.frep.vulcan.spigot;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.entity.Player;


public final class HookManager {
    private final Map playerDataByUuid = new ConcurrentHashMap();

    public PlayerData getPlayerData(Player player) {
        if (player == null) {
            return null;
        }
        return (PlayerData) this.playerDataByUuid.get(player.getUniqueId());
    }

    public PlayerData getPlayerDataByUser(User user) {
        if (user == null || user.getUUID() == null) {
            return null;
        }
        return (PlayerData) this.playerDataByUuid.get(user.getUUID());
    }

    public void registerUser(User user) {
        if (user == null || user.getUUID() == null) {
            return;
        }
        this.playerDataByUuid.put(user.getUUID(), new PlayerData(user));
    }

    public void unregisterPlayer(UUID uuid) {
        if (uuid == null) {
            return;
        }
        this.playerDataByUuid.remove(uuid);
    }

    public void unregisterUser(User user) {
        if (user == null || user.getUUID() == null) {
            return;
        }
        this.playerDataByUuid.remove(user.getUUID());
    }

    public Collection getAllPlayerData() {
        return this.playerDataByUuid.values();
    }

}
