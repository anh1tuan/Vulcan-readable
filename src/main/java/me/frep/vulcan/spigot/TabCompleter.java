package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private static final List<String> SUBCOMMANDS = Arrays.asList(
        "help",
        "profile",
        "ban",
        "reload",
        "disablecheck",
        "violations",
        "cps",
        "knockback",
        "kb",
        "connection",
        "freeze",
        "rotate",
        "shuffle",
        "top",
        "testalert"
    );

    @Nullable
    @Override
    public List onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, new ArrayList());
        }
        if (args.length == 2 && "disablecheck".equalsIgnoreCase(args[0]) && ConfigValues.enabledByCheck != null) {
            return StringUtil.copyPartialMatches(args[1], ConfigValues.enabledByCheck.keySet(), new ArrayList());
        }
        return new ArrayList();
    }
}
