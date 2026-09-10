package me.frep.vulcan.spigot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogsCommand extends CommandBase implements CommandExecutor {
    private final ExecutorService logReader = Executors.newSingleThreadExecutor();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            this.sendFormattedMessage(sender, ConfigValues.messagesCantExecuteFromConsole);
            return true;
        }

        Player player = (Player) sender;
        boolean punishLogs = "punishlogs".equalsIgnoreCase(command.getName());
        String permission = punishLogs ? "vulcan.punishlogs" : "vulcan.logs";
        if (!player.hasPermission(permission)) {
            this.sendFormattedMessage(sender, ConfigValues.messagesNoPermission);
            return true;
        }

        if (args.length < 1) {
            this.sendFormattedMessage(sender, punishLogs
                ? ConfigValues.commandsPunishlogsHeaderFooter
                : ConfigValues.messagesProfileCommandSyntax);
            return true;
        }

        int parsedPage = 1;
        if (args.length > 1) {
            try {
                parsedPage = Math.max(1, Integer.parseInt(args[1]));
            } catch (NumberFormatException ignored) {
                parsedPage = 1;
            }
        }
        final int page = parsedPage;

        String target = args[0];
        this.logReader.execute(() -> this.searchLogs(sender, target, page, punishLogs));
        return true;
    }

    private void searchLogs(CommandSender sender, String target, int page, boolean punishLogs) {
        String fileName = punishLogs ? "punishments.txt" : "logs.txt";
        File file = new File(VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder(), fileName);
        if (!file.exists()) {
            this.sendFormattedMessage(sender, ConfigValues.messagesNoLogsFile);
            return;
        }

        List<String> matches = new ArrayList<String>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.toLowerCase().contains(target.toLowerCase())) {
                    continue;
                }
                if (punishLogs && !line.toLowerCase().contains("punish")) {
                    continue;
                }
                matches.add(line);
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            return;
        }

        Collections.reverse(matches);
        if (matches.isEmpty()) {
            this.sendFormattedMessage(sender, ConfigValues.messagesNoLogs.replace("%player%", target));
            return;
        }

        if (punishLogs) {
            this.sendFormattedMessage(sender, ConfigValues.commandsPunishlogsHeaderFooter.replace("%player%", target));
            int index = 1;
            for (String line : matches) {
                String cleaned = line.replace("-", "").replace("[Vulcan]", "").replace("punish", "");
                this.sendFormattedMessage(sender, index + ". " + cleaned);
                ++index;
            }
            this.sendFormattedMessage(sender, ConfigValues.commandsPunishlogsHeaderFooter.replace("%player%", target));
            return;
        }

        int pages = (int) Math.ceil(matches.size() / 10.0);
        if (page > pages) {
            this.sendFormattedMessage(sender, ConfigValues.messagesNoPage
                .replace("%page%", Integer.toString(page))
                .replace("%player%", target));
            return;
        }

        this.sendFormattedMessage(sender, ConfigValues.commandsLogsHeaderFooter
            .replace("%player%", target)
            .replace("%page%", Integer.toString(page))
            .replace("%pages%", Integer.toString(pages)));

        int start = (page - 1) * 10;
        int end = Math.min(page * 10, matches.size());
        for (int i = start; i < end; ++i) {
            sender.sendMessage(ColorUtil.translateColors(ConfigValues.commandsLogsColor + matches.get(i)));
        }

        this.sendFormattedMessage(sender, ConfigValues.commandsLogsHeaderFooter
            .replace("%player%", target)
            .replace("%page%", Integer.toString(page))
            .replace("%pages%", Integer.toString(pages)));
    }
}
