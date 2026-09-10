package me.frep.vulcan.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitBridgeListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }

        if (ConfigValues.settingsToggleAlertsOnJoin && player.hasPermission("vulcan.alerts")) {
            VulcanBootstrap.INSTANCE.getAlertMessageBuilder().toggleAlerts(player);
        }

        if (ConfigValues.clientBrandJoinMessages != null) {
            for (Object raw : ConfigValues.clientBrandJoinMessages) {
                player.sendMessage(ColorUtil.translateColors(String.valueOf(raw)
                    .replace("%client-brand%", playerData.getClientBrand())
                    .replace("%player%", player.getName())));
            }
        }

        playerData.getBlockBreakTracker().setLastLocationZ(player.getLocation().getZ());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().getAlertPlayers().remove(player);
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().getVerboseAlertPlayers().remove(player);
        VulcanBootstrap.INSTANCE.getClientBrandByUuid().remove(player.getUniqueId());

        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }

        if (playerData.getBlockBreakTracker().isFrozen()) {
            VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(
                ConfigValues.messagesLoggedOutWhileFrozen.replace("%player%", player.getName())
            );
            if (ConfigValues.freezeLoggedOutWhileFrozenCommands != null) {
                for (Object raw : ConfigValues.freezeLoggedOutWhileFrozenCommands) {
                    ServerCompatibility.dispatchConsoleCommand(ColorUtil.translateColors(
                        String.valueOf(raw).replace("%player%", player.getName())
                    ));
                }
            }
        }
    }
}
