package me.frep.vulcan.spigot;

import ac.vulcan.anticheat.UniversalRunnable;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

class ScheduledTask extends UniversalRunnable {
    final List commands;
    final PlayerData playerData;
    final int violations;
    final AbstractCheck check;
    final AlertPlaceholders placeholders;

    ScheduledTask(AlertPlaceholders placeholders, List commands, PlayerData playerData, int violations, AbstractCheck check) {
        this.placeholders = placeholders;
        this.commands = commands;
        this.playerData = playerData;
        this.violations = violations;
        this.check = check;
    }

    @Override
    public void run() {
        for (Object raw : this.commands) {
            this.dispatch((String) raw);
        }
        AlertPlaceholders.setDebug(false);
    }

    private void dispatch(String template) {
        Player player = this.playerData.getPlayer();
        if (player == null || template == null) {
            return;
        }

        String targetName = "None";
        if (this.playerData.getReachEntityHelper() != null) {
            org.bukkit.entity.Entity target = this.playerData.getReachEntityHelper().getTargetEntity();
            if (target != null) {
                targetName = target.getName();
            } else if (this.playerData.getReachEntityHelper().getAttackingPlayer() != null) {
                targetName = this.playerData.getReachEntityHelper().getAttackingPlayer().getName();
            }
        }

        String command = template
            .replace("%prefix%", "")
            .replace("[player] ", "")
            .replace("%server-name%", ConfigValues.settingsServerName)
            .replace("%ping%", Integer.toString(this.playerData.getCheckState().getPing()))
            .replace("%tps%", TrigUtil.formatDecimal(ServerCompatibility.getTicksPerSecond()))
            .replace("%player%", player.getName())
            .replace("%version%", this.playerData.getUser().getClientVersion().getReleaseName())
            .replace("%client-brand%", this.playerData.getClientBrand())
            .replace("%vl%", Integer.toString(this.violations))
            .replace("%violations%", Integer.toString(this.violations))
            .replace("%category%", AlertFileLogger.formatCheckCategory(this.check.getCategory().toLowerCase()))
            .replace("%complex-type%", this.check.getCheckInfo().complexType())
            .replace("%description%", this.check.getCheckInfo().description())
            .replace("%check%", this.check.getCheckInfo().name())
            .replace("%type%", Character.toString(this.check.getCheckInfo().type()))
            .replace("%dev%", targetName);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            command = PlaceholderAPI.setPlaceholders(player, command);
        }

        command = ColorUtil.translateColors(command);
        if (template.trim().startsWith("[player]")) {
            player.performCommand(command.startsWith("/") ? command.substring(1) : command);
            return;
        }
        ServerCompatibility.dispatchConsoleCommand(command);
    }
}
