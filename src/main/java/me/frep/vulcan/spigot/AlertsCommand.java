package me.frep.vulcan.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCommand extends CommandBase implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            this.sendFormattedMessage(sender, ConfigValues.messagesCantExecuteFromConsole);
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("vulcan.alerts")) {
            this.sendFormattedMessage(sender, ConfigValues.messagesNoPermission);
            return true;
        }

        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().toggleAlerts(player);
        return true;
    }
}
