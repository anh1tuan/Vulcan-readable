package me.frep.vulcan.spigot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.entity.Player;

public final class AlertFileLogger {
    private AlertFileLogger() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String formatCheckCategory(String category) {
        if (category == null || category.isEmpty()) {
            return category;
        }
        return category.substring(0, 1).toUpperCase() + category.substring(1);
    }

    public static void logPunishment(AbstractCheck check, PlayerData data) {
        logNotes(check, data);
    }

    public static void logAlert(AbstractCheck check, PlayerData data, String extra) {
        if (check == null || data == null || data.getPlayer() == null) {
            return;
        }
        try {
            File dataFolder = VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            File logFile = new File(dataFolder, "ns.txt");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(logFile, true));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Integer maxVl = (Integer) ConfigValues.maxViolationsByCheck.get(check.getCheckName());
            String experimental = check.getCheckInfo() != null && check.getCheckInfo().experimental()
                ? ColorUtil.translateColors("*")
                : "";
            String line = ConfigValues.logFileAlertMessage
                .replaceAll("%player%", data.getUsername())
                .replaceAll("%max-vl%", Integer.toString(maxVl == null ? 0 : maxVl.intValue()))
                .replaceAll("%cps%", TrigUtil.formatDecimal(data.getClickTracker().getCps()))
                .replaceAll("%check%", check.getCheckInfo() == null ? check.getCheckName() : check.getCheckInfo().name())
                .replaceAll("%world%", data.getPlayer().getWorld().getName())
                .replaceAll("%x%", Double.toString(data.getBlockBreakTracker().getLocationX()))
                .replaceAll("%y%", Double.toString(data.getBlockBreakTracker().getLocationY()))
                .replaceAll("%z%", Double.toString(data.getBlockBreakTracker().getLocationZ()))
                .replaceAll("%client-brand%", data.getClientBrand())
                .replaceAll("%complex-type%", check.getCheckInfo() == null ? "" : check.getCheckInfo().complexType())
                .replaceAll("%tps%", new DecimalFormat("#.##").format(ServerCompatibility.getTicksPerSecond()))
                .replaceAll("%ping%", Integer.toString(CollisionUtil.getPlayerPing(data.getPlayer())))
                .replaceAll("%description%", check.getCheckInfo() == null ? "" : check.getCheckInfo().description())
                .replaceAll("%version%", data.getUser().getClientVersion().getReleaseName())
                .replaceAll("%dev%", experimental)
                .replaceAll("%vl%", Integer.toString(check.getViolationLevel()))
                .replaceAll("%type%", Character.toString(check.getType()));
            writer.println("[" + dateFormat.format(new Date()) + "] [" + timeFormat.format(Calendar.getInstance().getTime()) + "] "
                + line + (extra == null ? "" : " " + extra));
            writer.flush();
            writer.close();
            rotateLogFileIfNeeded(dataFolder, logFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void logNotes(AbstractCheck check, PlayerData data) {
        if (check == null || data == null || data.getPlayer() == null) {
            return;
        }
        Player player = data.getPlayer();
        try {
            File dataFolder = VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            File notes = new File(dataFolder, "nts.txt");
            if (!notes.exists()) {
                notes.createNewFile();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(notes, true));
            SimpleDateFormat stamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            SimpleDateFormat readable = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            writer.println("------[ " + data.getUsername() + " ] finished for "
                + formatCheckCategory(check.getDisplayName())
                + " Type " + Character.toUpperCase(check.getDisplayType()) + " ---------");
            writer.println("");
            writer.println("Time:");
            writer.println("Date: " + readable.format(new Date()));
            writer.println("Stamp: " + stamp.format(Calendar.getInstance().getTime()));
            writer.println("");
            writer.println("Location:");
            writer.println("World: " + player.getWorld().getName());
            writer.println("X: " + data.getBlockBreakTracker().getLocationX());
            writer.println("Y: " + data.getBlockBreakTracker().getLocationY());
            writer.println("Z: " + data.getBlockBreakTracker().getLocationZ());
            writer.println("");
            writer.println("Information:");
            writer.println("UUID: " + player.getUniqueId());
            writer.println("KeepAlive Ping: " + data.getCheckState().getPing());
            writer.println("TPS: " + new DecimalFormat("#.##").format(ServerCompatibility.getTicksPerSecond()));
            writer.println("Game Mode: " + player.getGameMode());
            writer.println("Client Brand: " + data.getClientBrand());
            writer.println("Version: " + data.getClientVersion().getReleaseName());
            writer.println("");
            writer.println("Violations: " + data.getTotalViolations());
            writer.println("Combat Violations: " + data.getCombatViolations());
            writer.println("Movement Violations: " + data.getMovementViolations());
            writer.println("Player Violations: " + data.getPlayerViolations());
            writer.println("");
            writer.println("Confirmation:");
            writer.println("Ping: " + data.getCheckState().getPing());
            writer.println("Ticks Lived: " + data.getStatusTracker().getTicksLived());
            writer.println("Username: " + data.getUsername());
            if (ServerCompatibility.isServer1_9OrNewer()) {
                writer.println("Holding Item (Main Hand): " + player.getInventory().getItemInMainHand());
                writer.println("Holding Item (Off Hand): " + player.getInventory().getItemInOffHand());
            } else {
                writer.println("Holding Item: " + player.getInventory().getItemInHand());
            }
            writer.println("");
            writer.println("Armor");
            writer.println("Helmet: " + player.getInventory().getHelmet());
            writer.println("Chestplate: " + player.getInventory().getChestplate());
            writer.println("Leggings: " + player.getInventory().getLeggings());
            writer.println("Boots: " + player.getInventory().getBoots());
            writer.println("");
            writer.println("Checks:");
            Iterator checks = data.getChecks().iterator();
            while (checks.hasNext()) {
                AbstractCheck other = (AbstractCheck) checks.next();
                if (other.getViolationLevel() > 0 && other.getCheckInfo() != null) {
                    writer.println("Check: " + formatCheckCategory(other.getDisplayName())
                        + " Type " + Character.toUpperCase(other.getType())
                        + " VL: " + other.getViolationLevel()
                        + " (" + other.getCheckInfo().description() + ")");
                }
            }
            writer.println("Potion Effects: " + player.getActivePotionEffects());
            writer.println("-------------------------------------------------------------------");
            writer.println("");
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void rotateLogFileIfNeeded(File dataFolder, File logFile) throws IOException {
        if (logFile.length() / 1000L <= (long) ConfigValues.settingsMaxLogsFileSize) {
            return;
        }
        File logsDir = new File(dataFolder, "logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }
        SimpleDateFormat stamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(
            new File(logsDir, stamp.format(new Date()) + ".zip")
        ));
        zip.putNextEntry(new ZipEntry("ns.txt"));
        FileInputStream input = new FileInputStream(logFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) > 0) {
            zip.write(buffer, 0, read);
        }
        zip.closeEntry();
        zip.close();
        input.close();
        new PrintWriter(logFile).close();
    }
}
