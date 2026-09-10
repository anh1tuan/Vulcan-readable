package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.List;


public class ClickTracker {
    private final PlayerData playerData;
    private long lastClickTimestamp = -1L;
    private long lastClickInterval;
    private long lastFlyingPacketTime = System.currentTimeMillis();
    private long lastItemFrameInteractTime = System.currentTimeMillis();
    private long lastNpcOrNoteBlockInteractTime = System.currentTimeMillis();
    private final SampleDeque clickSamples = new SampleDeque(20);
    private final List pendingAfterConfirmation = new ArrayList();
    private short lastTransactionId;
    private double cps;
    private double kurtosis;

    public ClickTracker(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void handleClick() {
        if (this.playerData.getExemptionChecker().isExempt(ExemptType.AUTOCLICKER_NON_DIG)) {
            return;
        }
        long now = System.currentTimeMillis();
        if (this.lastClickTimestamp > 0L) {
            this.lastClickInterval = now - this.lastClickTimestamp;
            this.clickSamples.add(Long.valueOf(this.lastClickInterval));
            double averageInterval = this.clickSamples.average();
            this.cps = averageInterval <= 0.0 ? 0.0 : 1000.0 / averageInterval;
        }
        this.lastClickTimestamp = now;
    }

    public void runAfterConfirmation(Runnable task) {
        if (task == null) {
            return;
        }
        this.pendingAfterConfirmation.add(task);
    }

    public void setLastTransactionId(int transactionId) {
        this.lastTransactionId = (short) transactionId;
    }

    public void handleConfirmation() {
        if (this.pendingAfterConfirmation.isEmpty()) {
            return;
        }
        List tasks = new ArrayList(this.pendingAfterConfirmation);
        this.pendingAfterConfirmation.clear();
        for (Object taskObject : tasks) {
            ((Runnable) taskObject).run();
        }
    }

    public void handleKeepAlive() {
        this.playerData.getCheckState().handleKeepAlive();
        this.handleConfirmation();
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public long getLastClickTimestamp() {
        return this.lastClickTimestamp;
    }

    public long getLastClickInterval() {
        return this.lastClickInterval;
    }

    public long getLastFlyingPacketTime() {
        return this.lastFlyingPacketTime;
    }

    public long getLastItemFrameInteractTime() {
        return this.lastItemFrameInteractTime;
    }

    public long getLastNpcOrNoteBlockInteractTime() {
        return this.lastNpcOrNoteBlockInteractTime;
    }

    public short getLastTransactionId() {
        return this.lastTransactionId;
    }

    public SampleDeque getClickSamples() {
        return this.clickSamples;
    }

    public double getCps() {
        return this.cps;
    }

    public double getKurtosis() {
        return this.kurtosis;
    }

    public void setLastFlyingPacket(long timestamp) {
        this.lastFlyingPacketTime = timestamp;
    }

    public void setLastClickTimestamp(long timestamp) {
        this.lastClickTimestamp = timestamp;
    }

    public void setLastClickInterval(long interval) {
        this.lastClickInterval = interval;
    }

    public void setLastItemFrameInteractTime(long timestamp) {
        this.lastItemFrameInteractTime = timestamp;
    }

    public void setLastNpcOrNoteBlockInteractTime(long timestamp) {
        this.lastNpcOrNoteBlockInteractTime = timestamp;
    }

    public void setCps(double cps) {
        this.cps = cps;
    }

    public void setKurtosis(double kurtosis) {
        this.kurtosis = kurtosis;
    }

}
