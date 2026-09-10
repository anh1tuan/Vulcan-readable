package me.frep.vulcan.spigot;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 * Per-player connection / keepalive ping state.
 */
public class CheckState {
    private final PlayerData playerData;
    private int ping;
    private final Map checkViolations = new HashMap();
    private final Map joinedViolations = new HashMap();
    private long lastKeepAliveTime;
    private long keepAliveDelta;
    private long createdAtMillis = System.currentTimeMillis();
    private long joinTimeMillis = System.currentTimeMillis();
    private long lastKeepAliveResponseTime = System.currentTimeMillis();
    private boolean keepAliveDelayed;
    private double keepAliveStrikeCount;
    private boolean maxPingAlertSent;
    private int maxPingViolationCount;
    private boolean maxPingKicked;

    public CheckState(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void handleKeepAlive() {
        long now = System.currentTimeMillis();
        this.keepAliveDelta = now - this.lastKeepAliveTime;
        this.keepAliveDelayed = this.keepAliveDelta <= 5L;
        if (this.keepAliveDelayed) {
            this.lastKeepAliveResponseTime = now;
        }
        Player player = this.playerData.getPlayer();
        if (player != null) {
            this.ping = CollisionUtil.getPlayerPing(player);
        }
        this.lastKeepAliveTime = now;
        this.handleKeepAliveKick();
        this.handleMaxPingKick();
    }

    public void setJoinTime() {
        this.joinTimeMillis = System.currentTimeMillis();
    }

    private void handleKeepAliveKick() {
        Player player = this.playerData.getPlayer();
        if (player == null) {
            return;
        }
        if (!ConfigValues.connectionKeepaliveKickEnabled) {
            return;
        }
        if (player.hasPermission("vulcan.bypass.connection.keepalive")) {
            return;
        }
        long delay = System.currentTimeMillis() - this.joinTimeMillis;
        if (delay <= ConfigValues.connectionKeepaliveMaxDelay) {
            return;
        }
        this.keepAliveStrikeCount += 1.0;
        if (this.keepAliveStrikeCount <= 5.0) {
            AsyncScheduler.runTask(this::kickForKeepAlive);
            if (ConfigValues.connectionKeepaliveStaffAlertMessage != null
                && !ConfigValues.connectionKeepaliveStaffAlertMessage.isEmpty()) {
                VulcanBootstrap.INSTANCE.getAlertPlaceholders().sendAlertMessage(
                    ConfigValues.connectionKeepaliveStaffAlertMessage
                        .replaceAll("%delay%", Long.toString(delay))
                        .replaceAll("%player%", this.playerData.getUsername())
                );
            }
        }
        if (ConfigValues.connectionKeepaliveConsoleMessage != null
            && !ConfigValues.connectionKeepaliveConsoleMessage.isEmpty()) {
            ServerCompatibility.logTranslatedInfo(
                ColorUtil.translateColors(
                    ConfigValues.connectionKeepaliveConsoleMessage
                        .replaceAll("%player%", this.playerData.getUsername())
                        .replaceAll("%delay%", Long.toString(delay))
                )
            );
        }
    }

    private void handleMaxPingKick() {
        Player player = this.playerData.getPlayer();
        if (player == null) {
            return;
        }
        if (!ConfigValues.connectionMaxPingKickEnabled) {
            return;
        }
        if (player.hasPermission("vulcan.bypass.connection.max-ping")) {
            return;
        }
        if (this.ping <= ConfigValues.connectionMaxPingMaxPing) {
            if (this.maxPingViolationCount > 0) {
                --this.maxPingViolationCount;
            }
            return;
        }
        if (this.maxPingKicked) {
            return;
        }
        ++this.maxPingViolationCount;
        if (this.maxPingViolationCount <= ConfigValues.connectionMaxPingMaxTicks) {
            return;
        }
        if (ConfigValues.connectionMaxPingStaffAlertMessage != null
            && !ConfigValues.connectionMaxPingStaffAlertMessage.isEmpty()) {
            VulcanBootstrap.INSTANCE.getAlertPlaceholders().sendAlertMessage(
                ConfigValues.connectionMaxPingStaffAlertMessage
                    .replaceAll("%ping%", Integer.toString(this.ping))
                    .replaceAll("%player%", this.playerData.getUsername())
            );
        }
        if (ConfigValues.connectionMaxPingConsoleMessage != null
            && !ConfigValues.connectionMaxPingConsoleMessage.isEmpty()) {
            ServerCompatibility.logTranslatedInfo(
                ColorUtil.translateColors(
                    ConfigValues.connectionMaxPingConsoleMessage
                        .replaceAll("%player%", this.playerData.getUsername())
                        .replaceAll("%ping%", Integer.toString(this.ping))
                )
            );
        }
        this.maxPingKicked = true;
        AsyncScheduler.runTask(this::kickForMaxPing);
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public int getPing() {
        return this.ping;
    }

    public Map getCheckViolations() {
        return this.checkViolations;
    }

    public Map getJoinedViolations() {
        return this.joinedViolations;
    }

    public long getLastKeepAliveTime() {
        return this.lastKeepAliveTime;
    }

    public long getKeepAliveDelta() {
        return this.keepAliveDelta;
    }

    public long getCreatedAtMillis() {
        return this.createdAtMillis;
    }

    public long getJoinTimeMillis() {
        return this.joinTimeMillis;
    }

    public long getLastKeepAliveResponseTime() {
        return this.lastKeepAliveResponseTime;
    }

    public boolean isKeepAliveDelayed() {
        return this.keepAliveDelayed;
    }

    public double getKeepAliveStrikeCount() {
        return this.keepAliveStrikeCount;
    }

    public boolean isMaxPingAlertSent() {
        return this.maxPingAlertSent;
    }

    public int getMaxPingViolationCount() {
        return this.maxPingViolationCount;
    }

    public boolean isMaxPingKicked() {
        return this.maxPingKicked;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public void setLastKeepAliveTime(long lastKeepAliveTime) {
        this.lastKeepAliveTime = lastKeepAliveTime;
    }

    public void setKeepAliveDelta(long keepAliveDelta) {
        this.keepAliveDelta = keepAliveDelta;
    }

    public void setCreatedAtMillis(long createdAtMillis) {
        this.createdAtMillis = createdAtMillis;
    }

    public void setJoinTimeMillis(long joinTime) {
        this.joinTimeMillis = joinTime;
    }

    public void setLastKeepAliveResponseTime(long lastKeepAliveResponseTime) {
        this.lastKeepAliveResponseTime = lastKeepAliveResponseTime;
    }

    public void setKeepAliveDelayed(boolean keepAliveDelayed) {
        this.keepAliveDelayed = keepAliveDelayed;
    }

    public void setKeepAliveStrikeCount(double keepAliveStrikeCount) {
        this.keepAliveStrikeCount = keepAliveStrikeCount;
    }

    public void setMaxPingAlertSent(boolean maxPingAlertSent) {
        this.maxPingAlertSent = maxPingAlertSent;
    }

    public void setMaxPingViolationCount(int maxPingViolationCount) {
        this.maxPingViolationCount = maxPingViolationCount;
    }

    public void setMaxPingKicked(boolean maxPingKicked) {
        this.maxPingKicked = maxPingKicked;
    }

    private void kickForMaxPing() {
        this.playerData.getPlayer().kickPlayer(ColorUtil.translateColors(ConfigValues.connectionMaxPingKickMessage));
    }

    private void kickForKeepAlive() {
        this.playerData.getPlayer().kickPlayer(ColorUtil.translateColors(ConfigValues.connectionKeepaliveKickMessage));
    }
}
