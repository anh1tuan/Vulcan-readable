package me.frep.vulcan.spigot;

import org.bukkit.command.CommandSender;


public class CommandBase {
    private static final String[] MESSAGE_TOKENS;

    public void sendFormattedMessage(CommandSender sender, String message) {
        if (sender == null || message == null) {
            return;
        }
        sender.sendMessage(ColorUtil.translateColors(
            message.replace("%prefix%", ColorUtil.translateColors(ConfigValues.resetCommandSyntax))
        ));
    }

    public void sendSeparatorLine(CommandSender sender) {
        this.sendFormattedMessage(sender, MESSAGE_TOKENS[1]);
    }

    static {
        MESSAGE_TOKENS = new String[] {
            "%prefix%",
            "&7&m-----------------------------------------------",
        };
    }
}
