package me.frep.vulcan.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.data.IPlayerData;
import org.bukkit.entity.Player;

public interface VulcanAPI {
    public void toggleAlerts(Player var1);

    public void toggleVerbose(Player var1);

    public IPlayerData getPlayerData(Player var1);

    public int getPing(Player var1);

    public double getKurtosis(Player var1);

    public int getTransactionPing(Player var1);

    public int getSensitivity(Player var1);

    public void executeBanWave();

    public void setFrozen(Player var1, boolean var2);

    public double getCps(Player var1);

    public int getTotalViolations(Player var1);

    public boolean isFrozen(Player var1);

    public int getMovementViolations(Player var1);

    public int getPlayerViolations(Player var1);

    public int getCombatViolations(Player var1);

    public double getTps();

    public String getServerVersion();

    public Map getCheckData();

    public int getJoinTicks(Player var1);

    public boolean hasAlertsEnabled(Player var1);

    public Check getCheck(Player var1, String var2, char var3);

    public String getVulcanVersion();

    public boolean isCheckEnabled(String var1);

    public int getTicks();

    public List getChecks(Player var1);

    public Map getEnabledChecks();

    public Map getMaxViolations();

    public Map getAlertIntervals();

    public Map getMinimumViolationsToNotify();

    public Map getPunishmentCommands();

    public Map getPunishableChecks();

    public Map getBroadcastPunishments();

    public Map getMaximumPings();

    public Map getMinimumTps();

    public Map getMaxBuffers();

    public Map getBufferDecays();

    public Map getBufferMultiples();

    public Map getHotbarShuffle();

    public Map getHotbarShuffleMinimums();

    public Map getHotbarShuffleIntervals();

    public Map getRandomRotation();

    public Map getRandomRotationMinimums();

    public Map getRandomRotationIntervals();

    public Set getChecks();

    public void flag(Player var1, String var2, String var3, String var4);
}

