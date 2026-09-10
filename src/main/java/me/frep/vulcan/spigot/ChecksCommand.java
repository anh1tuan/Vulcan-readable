package me.frep.vulcan.spigot;

import java.util.List;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChecksCommand extends CommandBase implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("vulcan.command") && !sender.hasPermission("vulcan.admin")) {
            this.sendFormattedMessage(sender, ConfigValues.messagesNoPermission);
            return true;
        }

        PlayerData playerData = null;
        if (args.length > 0) {
            Player target = sender.getServer().getPlayerExact(args[0]);
            if (target != null) {
                playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(target);
            }
        } else if (sender instanceof Player) {
            playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData((Player) sender);
        }

        if (playerData == null) {
            this.sendFormattedMessage(sender, ConfigValues.messagesInvalidTarget);
            return true;
        }

        this.sendFormattedMessage(sender, "&7Checks for &f" + playerData.getUsername() + "&7:");
        List checks = playerData.getChecks();
        if (checks == null) {
            return true;
        }
        for (Object raw : checks) {
            if (!(raw instanceof AbstractCheck)) {
                continue;
            }
            AbstractCheck check = (AbstractCheck) raw;
            CheckInfo info = check.getCheckInfo();
            String name = info == null ? check.getClass().getSimpleName() : info.name();
            String type = info == null ? "?" : Character.toString(info.type());
            this.sendFormattedMessage(
                sender,
                "&8- &f" + name + " " + type + " &7VL: &f" + check.getViolationLevel()
            );
        }
        return true;
    }
}
