package me.frep.vulcan.spigot;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;


public class PacketHandler {
    private static final short PING_ID_RESET = (short) -32348;
    private static final short PING_ID_WRAP = (short) -420;

    private final PlayerData playerData;
    private final Deque checkSummaries = new ConcurrentLinkedDeque();
    private final Deque pendingRunnables = new ConcurrentLinkedDeque();
    private short nextPingId = PING_ID_RESET;
    private short lastSentPingId = (short) -69;
    private long lastReplyElapsed;
    private long lastFlyingTime = -1L;
    private long lastFlyingDelta = -1L;
    private long lastConfirmationTime = System.currentTimeMillis();
    private long lastFastFlyingTime = Long.MAX_VALUE;
    private int lastRepliedId = -42069;

    public PacketHandler(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void runOrQueueOnSummary(Runnable runnable) {
        CheckSummary last = (CheckSummary) this.checkSummaries.peekLast();
        if (last == null) {
            runnable.run();
            return;
        }
        last.getPendingRunnables().add(runnable);
    }

    public void execute(Runnable runnable) {
        this.addPendingRunnable(runnable);
    }

    public void addPendingRunnable(Runnable runnable) {
        if (runnable != null) {
            this.pendingRunnables.add(runnable);
        }
    }

    public void enqueueConfirmation(int transactionId) {
        if (!this.playerData.isJoined()) {
            return;
        }
        CheckSummary summary = new CheckSummary(transactionId, System.currentTimeMillis());
        if (!this.pendingRunnables.isEmpty()) {
            this.pendingRunnables.forEach(r -> summary.addPendingRunnable((Runnable) r));
            this.pendingRunnables.clear();
        }
        this.checkSummaries.add(summary);
    }

    public void handlePong(int transactionId) {
        CheckSummary expected = (CheckSummary) this.checkSummaries.poll();
        if (expected == null) {
            this.alertAndKickInvalid(transactionId);
            return;
        }
        if (expected.getTransactionId() != transactionId && !shouldIgnoreMismatchedId(transactionId)) {
            this.alertAndKickOutOfOrder(transactionId, expected.getTransactionId());
            return;
        }
        this.lastRepliedId = transactionId;
        this.lastReplyElapsed = System.currentTimeMillis() - expected.getCreatedAtMillis();
        this.lastConfirmationTime = System.currentTimeMillis();
        expected.runPendingRunnables();
    }

    public void handleFlying() {
        long now = System.currentTimeMillis();
        long delay = now - this.lastConfirmationTime;
        long timeoutMillis = TimeUnit.SECONDS.toMillis(ConfigValues.connectionNoResponseDelaySeconds);
        boolean joinedLongEnough = now - this.playerData.getJoinTime() > 30000L;
        if (delay > timeoutMillis && joinedLongEnough && !this.isNearLastServerPosition()) {
            VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(
                ConfigValues.connectionNoResponseStaffAlertMessage
                    .replaceAll("%delay%", Long.toString(delay))
                    .replaceAll("%player%", this.playerData.getUsername())
            );
            ServerCompatibility.logTranslatedInfo(
                ConfigValues.connectionNoResponseConsoleAlertMessage
                    .replaceAll("%delay%", Long.toString(delay))
                    .replaceAll("%player%", this.playerData.getUsername())
            );
            this.kick(ConfigValues.connectionNoResponseKickMessage);
            return;
        }
        if (this.checkSummaries.size() > ConfigValues.connectionMaxSizeAmount && joinedLongEnough) {
            VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(
                ConfigValues.connectionMaxSizeStaffAlertMessage
                    .replaceAll("%size%", Integer.toString(this.checkSummaries.size()))
                    .replaceAll("%player%", this.playerData.getUsername())
            );
            ServerCompatibility.logTranslatedInfo(
                ConfigValues.connectionMaxSizeConsoleAlertMessage
                    .replaceAll("%size%", Integer.toString(this.checkSummaries.size()))
                    .replaceAll("%player%", this.playerData.getUsername())
            );
            this.kick(ConfigValues.connectionMaxSizeKickMessage);
            return;
        }
        if (this.lastFlyingTime > 0L) {
            this.lastFlyingDelta = now - this.lastFlyingTime;
            if (this.lastFlyingDelta < 5L) {
                this.lastFastFlyingTime = now;
            }
        }
        this.lastFlyingTime = now;
    }

    public void sendConfirmation(boolean immediate) {
        if (this.playerData.getUser() == null
            || this.playerData.getUser().getEncoderState() != ConnectionState.PLAY) {
            return;
        }
        Runnable sendPing = this::sendNextPing;
        if (!immediate) {
            ChannelHelper.runInEventLoop(this.playerData.getUser().getChannel(), sendPing);
            return;
        }
        sendPing.run();
    }

    public void sendPingPacket(int pingId) {
        PacketWrapper packet;
        if (ServerCompatibility.isServer1_17OrNewer()) {
            packet = new WrapperPlayServerPing(pingId);
        } else {
            packet = new WrapperPlayServerWindowConfirmation(0, (short) pingId, false);
        }
        this.playerData.getUser().sendPacket(packet);
    }

    public short nextPingId() {
        this.nextPingId = (short) (this.nextPingId + 1);
        if (this.nextPingId > PING_ID_WRAP) {
            this.nextPingId = PING_ID_RESET;
        }
        return this.nextPingId;
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public Deque getCheckSummaries() {
        return this.checkSummaries;
    }

    public Deque getPendingRunnables() {
        return this.pendingRunnables;
    }

    public short getNextPingId() {
        return this.nextPingId;
    }

    public short getLastSentPingId() {
        return this.lastSentPingId;
    }

    public long getLastReplyElapsed() {
        return this.lastReplyElapsed;
    }

    public long getLastFlyingTime() {
        return this.lastFlyingTime;
    }

    public long getLastFlyingDelta() {
        return this.lastFlyingDelta;
    }

    public long getLastConfirmationTime() {
        return this.lastConfirmationTime;
    }

    public long getLastFastFlyingTime() {
        return this.lastFastFlyingTime;
    }

    public int getLastRepliedId() {
        return this.lastRepliedId;
    }

    public void setNextPingId(int value) {
        this.nextPingId = (short) value;
    }

    public void setLastSentPingId(int value) {
        this.lastSentPingId = (short) value;
    }

    public void setLastReplyElapsed(long value) {
        this.lastReplyElapsed = value;
    }

    public void setLastFlyingTime(long value) {
        this.lastFlyingTime = value;
    }

    public void setLastFlyingDelta(long value) {
        this.lastFlyingDelta = value;
    }

    public void setLastConfirmationTime(long value) {
        this.lastConfirmationTime = value;
    }

    public void setLastFastFlyingTime(long value) {
        this.lastFastFlyingTime = value;
    }

    public void setLastRepliedId(int value) {
        this.lastRepliedId = value;
    }

    private void sendNextPing() {
        short pingId = this.nextPingId();
        this.sendPingPacket(pingId);
        this.lastSentPingId = pingId;
        this.enqueueConfirmation(pingId);
    }

    private boolean isNearLastServerPosition() {
        if (!ConfigValues.connectionNoResponseCodeOfConductFix) {
            return false;
        }
        BlockBreakTracker tracker = this.playerData.getBlockBreakTracker();
        double distance = TrigUtil.hypot3(
            tracker.getLocationX() - tracker.getLastServerX(),
            tracker.getLocationY() - tracker.getLastServerY(),
            tracker.getLocationZ() - tracker.getLastServerZ()
        );
        return distance < 5.0;
    }

    private boolean shouldIgnoreMismatchedId(int transactionId) {
        return ConfigValues.otherAnticheatSupport
            && transactionId != 0
            && System.currentTimeMillis() - this.playerData.getJoinTime() < 30000L;
    }

    private void alertAndKickInvalid(int transactionId) {
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(
            ConfigValues.connectionInvalidStaffAlertMessage
                .replaceAll("%id%", Integer.toString(transactionId))
                .replaceAll("%player%", this.playerData.getUsername())
        );
        ServerCompatibility.logTranslatedInfo(
            ConfigValues.connectionInvalidConsoleAlertMessage
                .replaceAll("%player%", this.playerData.getUsername())
                .replaceAll("%id%", Integer.toString(transactionId))
        );
        this.kick(ConfigValues.connectionInvalidKickMessage);
    }

    private void alertAndKickOutOfOrder(int receivedId, int expectedId) {
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(
            ConfigValues.connectionOutOfOrderStaffAlertMessage
                .replaceAll("%id%", Integer.toString(receivedId))
                .replaceAll("%last-id%", Integer.toString(expectedId))
                .replaceAll("%player%", this.playerData.getUsername())
        );
        ServerCompatibility.logTranslatedInfo(
            ConfigValues.connectionOutOfOrderConsoleAlertMessage
                .replaceAll("%id%", Integer.toString(receivedId))
                .replaceAll("%last-id%", Integer.toString(expectedId))
                .replaceAll("%player%", this.playerData.getUsername())
        );
        this.kick(ConfigValues.connectionOutOfOrderKickMessage);
    }

    private void kick(String message) {
        this.playerData.setJoined(true);
        if (this.playerData.getPlayer() != null) {
            AsyncScheduler.runTask(() -> this.playerData.getPlayer().kickPlayer(ColorUtil.translateColors(message)));
        } else if (this.playerData.getUser() != null) {
            this.playerData.getUser().closeConnection();
        }
    }
}
