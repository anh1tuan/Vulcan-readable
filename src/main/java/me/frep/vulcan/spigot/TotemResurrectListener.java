package me.frep.vulcan.spigot;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;


public class TotemResurrectListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onTotemResurrect(EntityResurrectEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData((Player) entity);
        if (playerData != null) {
            playerData.getStatusTracker().setTicksSinceTotem(0);
        }
    }
}
