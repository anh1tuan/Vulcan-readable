package me.frep.vulcan.spigot;

import ac.vulcan.anticheat.SchedulerBridge;


public class KeepAliveRunnable
implements Runnable {
    private int currentTick = 0;
    private int lagSpikeCount = 0;
    private static SchedulerBridge scheduledTask;
    private long lastWallClockMs = Long.MAX_VALUE;
    private long tickDurationMs = Long.MAX_VALUE;
    private long lastLagSpikeTime;
    private static final boolean assertionsEnabled = !KeepAliveRunnable.class.desiredAssertionStatus();
    public void start() {
        if (scheduledTask != null) {
            throw new IllegalStateException("TickProcessor has already been started!");
        }
        scheduledTask = VulcanBootstrap.getSchedulerAdapter().runTaskTimer(this, 0L, 1L);
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
        ++this.currentTick;
        for (Object playerDataRaw : VulcanBootstrap.INSTANCE.getHookManager().getAllPlayerData()) {
            if (!(playerDataRaw instanceof PlayerData)) continue;
            PlayerData playerData = (PlayerData) playerDataRaw;
            playerData.getClickTracker().handleKeepAlive();
        }
        if (this.currentTick % 200 == 0) {
            this.lagSpikeCount = 0;
        }
        if (ConfigValues.punishmentStatisticsBroadcastEnabled
            && this.currentTick % ConfigValues.punishmentStatisticsBroadcastInterval == 0) {
            ConfigValues.punishmentStatisticsBroadcastBroadcast.forEach(msg -> broadcastPunishmentCount((String) msg));
        }
        if (ConfigValues.judgementDaysRunAtInterval
            && this.currentTick % ConfigValues.judgementDaysInterval == 0) {
            BanWaveManager.getRemainingCount();
        }
        if (ConfigValues.discordWebhookCooldown
            && this.currentTick % ConfigValues.discordWebhookCooldownResetTicks == 0) {
            VulcanBootstrap.INSTANCE.setDiscordWebhookCooldownCount(0);
        }
        long now = System.currentTimeMillis();
        this.tickDurationMs = now - this.lastWallClockMs;
        this.lastWallClockMs = now;
        if (this.currentTick <= 500) {
            return;
        }
        if (this.tickDurationMs <= 90L && this.tickDurationMs >= 10L) {
            return;
        }
        ++this.lagSpikeCount;
        this.lastLagSpikeTime = now;
        if (ConfigValues.settingsTestingStuff) {
            ServerCompatibility.logInfo(
                "Potential lag spike detected! delay=" + this.tickDurationMs
                    + " count=" + this.lagSpikeCount
            );
        }
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public int getLagSpikeCount() {
        return this.lagSpikeCount;
    }

    public long getLastWallClockMs() {
        return this.lastWallClockMs;
    }

    public long getTickDurationMs() {
        return this.tickDurationMs;
    }

    public long getLastLagSpikeTime() {
        return this.lastLagSpikeTime;
    }

    private static void broadcastPunishmentCount(String message) {
        ServerCompatibility.broadcastTranslatedMessage(
            message.replace("%amount%", Integer.toString(StatsManager.getTotalPunishments()))
        );
    }
}
