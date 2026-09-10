package me.frep.vulcan.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player victim = (Player) event.getEntity();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(victim);
        if (playerData == null) {
            return;
        }
        playerData.getStatusTracker().recordDamagerType(event.getDamager().getType());
    }
}
