package me.frep.vulcan.spigot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;

public final class StatsManager {
    private static YamlFileHelper statsYaml;
    private static File statsFile;

    private StatsManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void saveStatsFile() {
        try {
            statsYaml.save(statsFile);
        } catch (Exception exception) {
            File broken = new File(VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder(), "stats.yml.broken." + new Date().getTime());
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "stats.yml");
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "Error saving file and renaming the current file to: " + broken.getName());
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "Try fixing the file with a yaml parser online!");
            statsFile.renameTo(broken);
            loadStatsFile();
        }
    }

    public static void reloadStatsFile() {
        try {
            statsYaml.load(statsFile);
        } catch (Exception exception) {
            File broken = new File(VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder(), "stats.yml.broken." + new Date().getTime());
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "stats.yml");
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "Error loading file and renaming the current file to: " + broken.getName());
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "Try fixing the file with a yaml parser online!");
            statsFile.renameTo(broken);
            loadStatsFile();
        }
    }

    public static void loadStatsFile() {
        File dataFolder = VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        statsFile = new File(dataFolder, "stats.yml");
        copyStreamToFile(VulcanBootstrap.INSTANCE.getVulcanPlugin().getResource("stats.yml"), statsFile);
        statsYaml = YamlFileHelper.loadFromFile(statsFile);
        try {
            statsYaml.saveSkippingRootKeys(statsFile);
        } catch (Exception ignored) {
        }
    }

    private static void copyStreamToFile(InputStream inputStream, File file) {
        if (inputStream == null || file == null || file.exists()) {
            return;
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static int getInt(String key) {
        return statsYaml.getInt(key);
    }

    public static int getTotalPunishments() {
        return getInt("punishments");
    }

    public static void recordStat(String key, Object value) {
        statsYaml.set(key, value);
        saveStatsFile();
        reloadStatsFile();
    }
}
