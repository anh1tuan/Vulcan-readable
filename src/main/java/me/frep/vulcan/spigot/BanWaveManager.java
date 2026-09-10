package me.frep.vulcan.spigot;

import java.util.Iterator;
import me.frep.vulcan.api.event.VulcanJudgementDayEndEvent;
import me.frep.vulcan.api.event.VulcanJudgementDayStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Judgement-day / ban-wave runner.
 */
public class BanWaveManager {
    private static int remainingCount;

    public static void setEnabled() {
        executeBanWave();
    }

    public static void executeBanWave() {
        VulcanBootstrap.INSTANCE.getAlertsExecutor().execute(BanWaveManager::runBanWave);
    }

    public static boolean isPlayerWhitelisted(Player player) {
        if (player == null) {
            return false;
        }
        PluginYamlConfig pending = new PluginYamlConfig("pending");
        ConfigurationSection users = pending.getConfig().getConfigurationSection("users");
        if (users == null) {
            return false;
        }
        Iterator iterator = users.getKeys(false).iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (key.equalsIgnoreCase(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static int countPendingUsers() {
        PluginYamlConfig pending = new PluginYamlConfig("pending");
        ConfigurationSection users = pending.getConfig().getConfigurationSection("users");
        if (users == null) {
            return 0;
        }
        return users.getKeys(false).size();
    }

    private static void runBanWave() {
        PluginYamlConfig pending = new PluginYamlConfig("pending");
        PluginYamlConfig executed = new PluginYamlConfig("executed");
        ConfigurationSection users = pending.getConfig().getConfigurationSection("users");
        if (users == null || users.getKeys(false) == null) {
            return;
        }
        if (ConfigValues.apiExportEnabled) {
            Bukkit.getPluginManager().callEvent((Event) new VulcanJudgementDayStartEvent());
        }
        ConfigValues.judgementDaysStartedBroadcast.forEach(msg -> ServerCompatibility.broadcastTranslatedMessage((String) msg));
        int processed = 0;
        for (String key : users.getKeys(false)) {
            String name = pending.getConfig().getString("users." + key + ".name");
            String uuid = pending.getConfig().getString("users." + key + ".uuid");
            String date = pending.getConfig().getString("users." + key + ".date");
            String addedBy = pending.getConfig().getString("users." + key + ".addedBy");
            String reason = pending.getConfig().getString("users." + key + ".reason");
            String command = pending.getConfig().getString("users." + key + ".command");
            AsyncScheduler.runTask(() -> runJudgementCommands(name));
            ConfigValues.judgementDaysBroadcast.forEach(message -> ServerCompatibility.broadcastTranslatedMessage(((String) message).replaceAll("%player%", name)));
            executed.getConfig().set("users." + key + ".name", name);
            executed.getConfig().set("users." + key + ".uuid", uuid);
            executed.getConfig().set("users." + key + ".command", command);
            executed.getConfig().set("users." + key + ".date", date);
            executed.getConfig().set("users." + key + ".addedBy", addedBy);
            executed.getConfig().set("users." + key + ".reason", reason);
            executed.save();
            ++processed;
            try {
                Thread.sleep(ConfigValues.judgementDaysCooldown);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        if (ConfigValues.apiExportEnabled) {
            Bukkit.getPluginManager().callEvent((Event) new VulcanJudgementDayEndEvent());
        }
        int processedCount = processed;
        ConfigValues.judgementDaysEndedBroadcast.forEach(message -> ServerCompatibility.broadcastTranslatedMessage(((String) message).replaceAll("%id%", Integer.toString(processedCount))));
        pending.getConfig().set("users", null);
        pending.save();
        VulcanBootstrap.INSTANCE.getVulcanPlugin().saveConfig();
    }

    private static void runJudgementCommands(String playerName) {
        ConfigValues.judgementDaysCommands.forEach(command -> ServerCompatibility.dispatchConsoleCommand(((String) command).replaceAll("%player%", playerName)));
    }

    public static void setRemainingCount(int remainingCount) {
        BanWaveManager.remainingCount = remainingCount;
    }

    public static int getRemainingCount() {
        executeBanWave();
        return remainingCount;
    }

    static {
        BanWaveManager.setRemainingCount(0);
    }
}
