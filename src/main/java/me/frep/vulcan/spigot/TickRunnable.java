package me.frep.vulcan.spigot;

import ac.vulcan.anticheat.SchedulerBridge;
import me.frep.vulcan.api.event.VulcanViolationResetEvent;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Bukkit;

public class TickRunnable implements Runnable {
    private static SchedulerBridge scheduledTask;
    static final boolean assertionsDisabled = !TickRunnable.class.desiredAssertionStatus();

    public static void resetViolations() {
        if (!ConfigValues.violationResetEnabled) {
            return;
        }
        VulcanViolationResetEvent event = new VulcanViolationResetEvent();
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        for (Object playerDataRaw : VulcanBootstrap.INSTANCE.getHookManager().getAllPlayerData()) {
            if (!(playerDataRaw instanceof PlayerData)) continue;
            PlayerData playerData = (PlayerData) playerDataRaw;
            resetPlayerViolations(playerData);
        }
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(ConfigValues.violationResetMessage);
    }

    public void start() {
        if (scheduledTask != null) {
            return;
        }
        long periodTicks = 1200L;
        scheduledTask = VulcanBootstrap.getSchedulerAdapter().runTaskTimer(this, periodTicks, periodTicks);
    }

    public void stop() {
        if (scheduledTask == null) {
            return;
        }
        scheduledTask.cancel();
        scheduledTask = null;
    }

    @Override
    public void run() {
        TickRunnable.resetViolations();
    }

    private static void resetPlayerViolations(PlayerData playerData) {
        playerData.setTotalViolations(0);
        playerData.setCombatViolations(0);
        playerData.setMovementViolations(0);
        playerData.setPlayerViolations(0);
        playerData.setAutoClickerViolations(0);
        playerData.setScaffoldViolations(0);
        playerData.setTimerViolations(0);
        playerData.getChecks().forEach(check -> { if (check instanceof AbstractCheck) resetCheck((AbstractCheck) check); });
    }

    private static void resetCheck(AbstractCheck check) {
        check.setViolationLevel(0);
        check.resetBuffer();
    }
}
