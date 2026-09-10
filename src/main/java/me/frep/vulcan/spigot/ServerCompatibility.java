package me.frep.vulcan.spigot;

import java.util.logging.Level;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;


public final class ServerCompatibility {
    private static final boolean newerThan1_7_10;
    private static final boolean server1_8OrNewer;
    private static final boolean server1_9OrNewer;
    private static final boolean olderThan1_13;
    private static final boolean olderThan1_8;
    private static final boolean olderThan1_9;
    private static final boolean newerThan1_13;
    private static final boolean server1_16OrNewer;
    private static final boolean server1_12OrNewer;
    private static final boolean server1_11OrNewer;
    private static final boolean server1_17OrNewer;
    private static final boolean server1_14OrNewer;
    private static final boolean server1_18OrNewer;
    private static final boolean server1_19OrNewer;
    private static final boolean server1_20OrNewer;
    private static final boolean server1_20_2OrNewer;
    private static final boolean olderThan1_21;
    private static final boolean server1_12_2OrNewer;
    private static final boolean server1_21_2OrNewer;

    private ServerCompatibility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static double getTicksPerSecond() {
        if (isFolia()) {
            return 20.0;
        }
        try {
            return Math.min(20.0, SpigotReflectionUtil.getTPS());
        } catch (UnsupportedOperationException ignored) {
            return 20.0;
        }
    }

    public static void logInfo(String message) {
        VulcanBootstrap.INSTANCE.getLogger().log(Level.INFO, ColorUtil.translateColors(message));
    }

    public static void logTranslatedInfo(String message) {
        logInfo(message);
    }

    public static void logDebugIfEnabled(String message) {
        if (ConfigValues.settingsDebug) {
            logInfo(message);
        }
    }

    public static ServerVersion getServerVersion() {
        return PacketEvents.getAPI().getServerManager().getVersion();
    }

    public static boolean isFolia() {
        try {
            Class.forName("paper.threadedregions.");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }


    public static boolean isServer1_17OrNewer() {
        return server1_17OrNewer;
    }

    public static boolean isServer1_19OrNewer() {
        return server1_19OrNewer;
    }

    public static boolean isServer1_14OrNewer() {
        return server1_14OrNewer;
    }

    public static boolean isServer1_11OrNewer() {
        return server1_11OrNewer;
    }

    public static boolean isBukkitPlatform() {
        return newerThan1_7_10;
    }

    public static boolean isServerOlderThan1_9() {
        return olderThan1_9;
    }

    public static boolean isServer1_8OrNewer() {
        return server1_8OrNewer;
    }

    public static boolean isModernProtocol() {
        return newerThan1_13;
    }

    public static boolean isServer1_16OrNewer() {
        return server1_16OrNewer;
    }

    public static boolean isServer1_9OrNewer() {
        return server1_9OrNewer;
    }

    public static boolean isServerOlderThan1_13() {
        return olderThan1_13;
    }

    public static boolean isServerOlderThan1_8() {
        return olderThan1_8;
    }

    public static boolean isServer1_18OrNewer() {
        return server1_18OrNewer;
    }

    public static boolean isServerOlderThan1_21() {
        return olderThan1_21;
    }

    public static boolean isServer1_20OrNewer() {
        return server1_20OrNewer;
    }

    public static boolean isServer1_20_2OrNewer() {
        return server1_20_2OrNewer;
    }

    public static void dispatchConsoleCommand(String command) {
        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), ColorUtil.translateColors(command));
    }

    public static void broadcastTranslatedMessage(String message) {
        Bukkit.broadcastMessage(ColorUtil.translateColors(message));
    }

    public static boolean isServer1_12OrNewer() {
        return server1_12OrNewer;
    }

    public static boolean isServerOlderThan1_17() {
        return getServerVersion().isOlderThan(ServerVersion.V_1_17);
    }

    public static boolean isServer1_12_2OrNewer() {
        return server1_12_2OrNewer;
    }

    public static boolean isViaVersionPresent() {
        try {
            Class.forName("com.viaversion.viaversion.api.Via");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static boolean isServer1_21_2OrNewer() {
        return server1_21_2OrNewer;
    }

    public static boolean isOffHandSupported() {
        return true;
    }

    static {
        ServerVersion version = getServerVersion();
        newerThan1_7_10 = version.isNewerThan(ServerVersion.V_1_7_10);
        server1_8OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_8);
        server1_9OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_9);
        olderThan1_13 = version.isOlderThan(ServerVersion.V_1_13);
        olderThan1_8 = version.isOlderThan(ServerVersion.V_1_8);
        olderThan1_9 = version.isOlderThan(ServerVersion.V_1_9);
        newerThan1_13 = version.isNewerThan(ServerVersion.V_1_13);
        server1_16OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_16);
        server1_12OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_12);
        server1_11OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_11);
        server1_17OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_17);
        server1_14OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_14);
        server1_18OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_18);
        server1_19OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_19);
        server1_20OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_20);
        server1_20_2OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_20_2);
        olderThan1_21 = version.isOlderThan(ServerVersion.V_1_21);
        server1_12_2OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_12_2);
        server1_21_2OrNewer = version.isNewerThanOrEquals(ServerVersion.V_1_21_2);
    }
}
