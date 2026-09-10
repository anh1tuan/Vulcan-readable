package me.frep.vulcan.spigot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand extends CommandBase implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("vulcan.command") && !sender.hasPermission("vulcan.admin")) {
            this.sendFormattedMessage(sender, ConfigValues.messagesNoPermission);
            return true;
        }
        if (args.length < 1) {
            this.sendFormattedMessage(sender, ConfigValues.messagesProfileCommandSyntax);
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            this.sendFormattedMessage(sender, ConfigValues.messagesInvalidTarget);
            return true;
        }
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(target);
        if (playerData == null) {
            this.sendFormattedMessage(sender, ConfigValues.messagesInvalidTarget);
            return true;
        }
        new MainCommand().sendProfile(sender, playerData);
        return true;
    }
}
