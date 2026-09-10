package me.frep.vulcan.spigot.api;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.frep.vulcan.api.VulcanAPI;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.data.IPlayerData;
import me.frep.vulcan.spigot.BanWaveManager;
import me.frep.vulcan.spigot.CollisionUtil;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ServerCompatibility;
import me.frep.vulcan.spigot.VulcanBootstrap;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.manager.CheckManager;
import org.bukkit.entity.Player;


public class VulcanSpigotAPI
implements VulcanAPI {
    @Override
    public IPlayerData getPlayerData(Player player) {
        return VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
    }

    @Override
    public int getPing(Player player) {
        return CollisionUtil.getPlayerPing(player);
    }

    @Override
    public void executeBanWave() {
        BanWaveManager.setEnabled();
    }

    @Override
    public boolean isFrozen(Player player) {
        return ((PlayerData)this.getPlayerData(player)).getBlockBreakTracker().isFrozen();
    }

    @Override
    public double getKurtosis(Player player) {
        return ((PlayerData)this.getPlayerData(player)).getClickTracker().getKurtosis();
    }

    @Override
    public void setFrozen(Player player, boolean bl) {
        PlayerData playerRef = (PlayerData)this.getPlayerData(player);

        if (playerRef == null) {
            return;
        }

        playerRef.getBlockBreakTracker().setFrozen(bl);
    }

    @Override
    public int getSensitivity(Player player) {
        return (int) (((PlayerData)this.getPlayerData(player)).getActionTracker().getSensitivity() * 100.0);
    }

    @Override
    public double getCps(Player player) {
        return ((PlayerData)this.getPlayerData(player)).getClickTracker().getCps();
    }

    @Override
    public int getTransactionPing(Player player) {
        return ((PlayerData)this.getPlayerData(player)).getTickTracker().getTransactionPing();
    }

    @Override
    public int getTotalViolations(Player player) {
        return this.getPlayerData(player).getTotalViolations();
    }

    @Override
    public int getCombatViolations(Player player) {
        return this.getPlayerData(player).getCombatViolations();
    }

    @Override
    public int getMovementViolations(Player player) {
        return this.getPlayerData(player).getMovementViolations();
    }

    @Override
    public int getPlayerViolations(Player player) {
        return this.getPlayerData(player).getPlayerViolations();
    }

    @Override
    public double getTps() {
        return ServerCompatibility.getTicksPerSecond();
    }

    @Override
    public int getTicks() {
        return VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    @Override
    public int getJoinTicks(Player player) {
        return this.getPlayerData(player).getJoinTicks();
    }

    @Override
    public String getVulcanVersion() {
        return VulcanBootstrap.INSTANCE.getVulcanPlugin().getDescription().getVersion();
    }

    @Override
    public Check getCheck(Player player, String string, char c) {
        PlayerData playerRef = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);

        if (playerRef == null) {
            return null;
        }

        for (Object rawCheck : playerRef.getChecks()) {
            if (!(rawCheck instanceof AbstractCheck)) continue;
            AbstractCheck abstractCheck = (AbstractCheck) rawCheck;
            if (!abstractCheck.getName().equals(string) || !Character.toString(abstractCheck.getType()).equals(Character.toString(c))) continue;
            return abstractCheck;
        }
        return null;
    }

    @Override
    public Map getCheckData() {
        return ConfigValues.punishmentCommandsByCheck;
    }

    @Override
    public boolean hasAlertsEnabled(Player player) {
        return VulcanBootstrap.INSTANCE.getAlertMessageBuilder().getAlertPlayers().contains(player);
    }

    @Override
    public boolean isCheckEnabled(String string) {
        for (Constructor constructor : CheckManager.checkConstructors) {
            if (!constructor.getDeclaringClass().getSimpleName().equalsIgnoreCase(string)) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    public String getServerVersion() {
        return ServerCompatibility.getServerVersion().toString();
    }

    @Override
    public Set getChecks() {
        return ConfigValues.enabledByCheck.keySet();
    }

    @Override
    public Map getEnabledChecks() {
        return ConfigValues.enabledByCheck;
    }

    @Override
    public Map getMaxViolations() {
        return ConfigValues.maxViolationsByCheck;
    }

    @Override
    public Map getAlertIntervals() {
        return ConfigValues.alertIntervalByCheck;
    }

    @Override
    public Map getMinimumViolationsToNotify() {
        return ConfigValues.minimumVlToNotifyByCheck;
    }

    @Override
    public Map getPunishmentCommands() {
        return ConfigValues.punishmentCommandListByCheck;
    }

    @Override
    public Map getPunishableChecks() {
        return ConfigValues.punishableByCheck;
    }

    @Override
    public Map getBroadcastPunishments() {
        return ConfigValues.punishmentCommandsBySeverity;
    }

    @Override
    public Map getMaximumPings() {
        return ConfigValues.maximumPingByCheck;
    }

    @Override
    public Map getMinimumTps() {
        return ConfigValues.minimumTpsByCheck;
    }

    @Override
    public Map getMaxBuffers() {
        return ConfigValues.maxBufferByCheck;
    }

    @Override
    public Map getBufferDecays() {
        return ConfigValues.bufferDecayByCheck;
    }

    @Override
    public Map getBufferMultiples() {
        return ConfigValues.bufferMultipleByCheck;
    }

    @Override
    public Map getHotbarShuffle() {
        return ConfigValues.hotbarShuffleEnabledByCheck;
    }

    @Override
    public Map getHotbarShuffleMinimums() {
        return ConfigValues.hotbarShuffleMinimumVlByCheck;
    }

    @Override
    public Map getHotbarShuffleIntervals() {
        return ConfigValues.hotbarShuffleIntervalByCheck;
    }

    @Override
    public Map getRandomRotation() {
        return ConfigValues.randomRotationEnabledByCheck;
    }

    @Override
    public Map getRandomRotationMinimums() {
        return ConfigValues.randomRotationMinimumVlByCheck;
    }

    @Override
    public Map getRandomRotationIntervals() {
        return ConfigValues.randomRotationIntervalByCheck;
    }

    @Override
    public List getChecks(Player player) {
        return ((PlayerData)this.getPlayerData(player)).getChecks();
    }

    @Override
    public void toggleAlerts(Player player) {
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().toggleAlerts(player);
    }

    @Override
    public void toggleVerbose(Player player) {
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().toggleVerboseAlerts(player);
    }

    @Override
    public void flag(Player player, String check, String extra, String type) {
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().handleApiAlert(player, check, extra, type);
    }

    private static Check lambda$getChecks$0(AbstractCheck abstractCheck) {
        return abstractCheck;
    }
}
