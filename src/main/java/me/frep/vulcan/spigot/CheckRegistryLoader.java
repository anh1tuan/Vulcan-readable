package me.frep.vulcan.spigot;

import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import me.frep.vulcan.spigot.check.data.CheckData;
import me.frep.vulcan.spigot.check.manager.CheckManager;

public final class CheckRegistryLoader {
    public static void clearCheckSettings() {
        ConfigValues.punishmentCommandsByCheck.clear();
        ConfigValues.enabledByCheck.clear();
        ConfigValues.maxViolationsByCheck.clear();
        ConfigValues.alertIntervalByCheck.clear();
        ConfigValues.minimumVlToNotifyByCheck.clear();
        ConfigValues.punishmentCommandListByCheck.clear();
        ConfigValues.punishableByCheck.clear();
        ConfigValues.punishmentCommandsBySeverity.clear();
        ConfigValues.maximumPingByCheck.clear();
        ConfigValues.minimumTpsByCheck.clear();
        ConfigValues.checkEnabledByName.clear();
        ConfigValues.maxBufferByCheck.clear();
        ConfigValues.bufferDecayByCheck.clear();
        ConfigValues.bufferMultipleByCheck.clear();
        ConfigValues.hotbarShuffleEnabledByCheck.clear();
        ConfigValues.hotbarShuffleMinimumVlByCheck.clear();
        ConfigValues.hotbarShuffleIntervalByCheck.clear();
        ConfigValues.randomRotationEnabledByCheck.clear();
        ConfigValues.randomRotationMinimumVlByCheck.clear();
        ConfigValues.randomRotationIntervalByCheck.clear();
        ConfigValues.discordAlertIntervalByCheck.clear();
        ConfigValues.maxVlBeforeSetbackByCheck.clear();
    }

    public static void loadCheckSettings() {
        for (Class<? extends AbstractCheck> checkClass : CheckManager.checkClasses) {
            CheckInfo info = checkClass.getAnnotation(CheckInfo.class);
            if (info == null) {
                continue;
            }

            String simpleName = checkClass.getSimpleName();
            String checkId = (info.name() + info.type()).replace(" ", "");
            String category = categoryOf(checkClass);
            String checkKey = info.name().replace(" ", "").toLowerCase();
            String typeKey = Character.toString(Character.toLowerCase(info.type()));
            String prefix = "checks." + category + "." + checkKey + "." + typeKey + ".";

            boolean enabled = ConfigValues.getConfigBoolean(prefix + "enabled");
            boolean punishable = ConfigValues.getConfigBoolean(prefix + "punishable");
            boolean broadcastPunishment = ConfigValues.getConfigBoolean(prefix + "broadcast-punishment");
            int maxViolations = ConfigValues.getConfigInt(prefix + "max-violations");
            int alertInterval = ConfigValues.getConfigInt(prefix + "alert-interval");
            int dontAlertUntil = ConfigValues.getConfigInt(prefix + "dont-alert-until");
            int maximumPing = ConfigValues.getConfigInt(prefix + "maximum-ping");
            double minimumTps = ConfigValues.getConfigDouble(prefix + "minimum-tps");
            java.util.List punishmentCommands = ConfigValues.getConfigStringList(prefix + "punishment-commands");
            int discordAlertInterval = ConfigValues.getConfigInt(prefix + "discord-alert-interval");
            int bufferMax = ConfigValues.getConfigInt(prefix + "buffer.max");
            double bufferMultiple = ConfigValues.getConfigDouble(prefix + "buffer.multiple");
            double bufferDecay = ConfigValues.getConfigDouble(prefix + "buffer.decay");
            boolean hotbarShuffle = ConfigValues.getConfigBoolean(prefix + "hotbar-shuffle.enabled");
            int hotbarShuffleMinimum = ConfigValues.getConfigInt(prefix + "hotbar-shuffle.minimum-vl-to-shuffle");
            int hotbarShuffleEvery = ConfigValues.getConfigInt(prefix + "hotbar-shuffle.shuffle-every");
            boolean randomRotation = ConfigValues.getConfigBoolean(prefix + "random-rotation.enabled");
            int randomRotationMinimum = ConfigValues.getConfigInt(prefix + "random-rotation.minimum-vl-to-randomly-rotate");
            int randomRotationEvery = ConfigValues.getConfigInt(prefix + "random-rotation.rotate-every");

            ConfigValues.enabledByCheck.put(simpleName, enabled);
            ConfigValues.checkEnabledByName.put(simpleName, enabled);
            ConfigValues.checkEnabledByName.put(checkId, enabled);
            ConfigValues.maxViolationsByCheck.put(simpleName, maxViolations);
            ConfigValues.minimumVlToNotifyByCheck.put(simpleName, dontAlertUntil);
            ConfigValues.alertIntervalByCheck.put(simpleName, alertInterval);
            ConfigValues.maximumPingByCheck.put(simpleName, maximumPing);
            ConfigValues.minimumTpsByCheck.put(simpleName, minimumTps);
            ConfigValues.punishmentCommandListByCheck.put(simpleName, punishmentCommands);
            ConfigValues.punishableByCheck.put(simpleName, punishable);
            ConfigValues.punishmentCommandsBySeverity.put(simpleName, broadcastPunishment);
            ConfigValues.discordAlertIntervalByCheck.put(simpleName, discordAlertInterval);
            ConfigValues.maxVlBeforeSetbackByCheck.put(simpleName, Integer.MAX_VALUE);
            ConfigValues.maxBufferByCheck.put(simpleName, bufferMax);
            ConfigValues.bufferMultipleByCheck.put(simpleName, bufferMultiple);
            ConfigValues.bufferDecayByCheck.put(simpleName, bufferDecay);
            ConfigValues.hotbarShuffleEnabledByCheck.put(simpleName, hotbarShuffle);
            ConfigValues.hotbarShuffleMinimumVlByCheck.put(simpleName, hotbarShuffleMinimum);
            ConfigValues.hotbarShuffleIntervalByCheck.put(simpleName, hotbarShuffleEvery);
            ConfigValues.randomRotationEnabledByCheck.put(simpleName, randomRotation);
            ConfigValues.randomRotationMinimumVlByCheck.put(simpleName, randomRotationMinimum);
            ConfigValues.randomRotationIntervalByCheck.put(simpleName, randomRotationEvery);

            if (ConfigValues.settingsEnableApi || ConfigValues.apiExportEnabled) {
                CheckData data = new CheckData(simpleName);
                data.setEnabled(enabled);
                data.setPunishable(punishable);
                data.setBroadcastPunishment(broadcastPunishment);
                data.setHotbarShuffle(hotbarShuffle);
                data.setRandomRotation(randomRotation);
                data.setMaxViolations(maxViolations);
                data.setAlertInterval(alertInterval);
                data.setMinimumVlToNotify(dontAlertUntil);
                data.setMaxPing(maximumPing);
                data.setMinimumVlToRandomlyRotate(randomRotationMinimum);
                data.setMinimumVlToShuffleHotbar(hotbarShuffleMinimum);
                data.setRandomRotationInterval(randomRotationEvery);
                data.setMinimumTps(minimumTps);
                data.setMaxBuffer((double) bufferMax);
                data.setBufferDecay(bufferDecay);
                data.setBufferMultiple(bufferMultiple);
                data.setPunishmentCommands(punishmentCommands);
                ConfigValues.punishmentCommandsByCheck.put(simpleName, data);
            }
        }
    }


    private static String categoryOf(Class<? extends AbstractCheck> checkClass) {
        String pkg = checkClass.getName();
        if (pkg.contains(".combat.")) {
            return "combat";
        }
        if (pkg.contains(".movement.")) {
            return "movement";
        }
        if (pkg.contains(".player.")) {
            return "player";
        }
        return "player";
    }

    private CheckRegistryLoader() {
        throw new UnsupportedOperationException("CheckRegistryLoader is a utility class and cannot be instantiated");
    }
}
