package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public final class ColorUtil {
    private static final Pattern HEX_COLOR = Pattern.compile("#[0-9A-Fa-f]{6}");

    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String translateColors(String input) {
        if (input == null) {
            return "";
        }
        String text = input;
        if (ServerCompatibility.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_16)) {
            Matcher matcher = HEX_COLOR.matcher(text);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                String hex = matcher.group();
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(ChatColor.of(hex).toString()));
            }
            matcher.appendTail(buffer);
            text = buffer.toString();
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }


}
