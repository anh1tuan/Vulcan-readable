package me.frep.vulcan.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;

public class RiptideListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRiptide(PlayerRiptideEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData != null) {
            playerData.getBlockBreakTracker().setRiptideTicks(0);
        }
    }
}
