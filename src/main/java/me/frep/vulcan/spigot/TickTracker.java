package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;
import com.github.retrooper.packetevents.util.Vector3d;
import java.util.LinkedList;

public class TickTracker {
    private final PlayerData playerData;
    private final LinkedList velocitySamples = new LinkedList();
    private final LinkedList explosionSamples = new LinkedList();
    private boolean awaitingFlying;
    private boolean awaitingInteract;
    private double lastQueuedVelocityY;
    private double lastVelocityY;
    private double velocityX;
    private double velocityZ;
    private int flyingTicks;
    private int transactionTicks;
    private int ticksSinceVelocity = 1000;
    private int ticksSinceExplosion = 1000;

    public TickTracker(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void handleVelocityPacket(WrapperPlayServerEntityVelocity packet, long timestamp, PacketSendEvent event) {
        this.playerData.getPacketHandler().runOrQueueOnSummary(() -> this.queueVelocity(packet));
        this.playerData.getPacketHandler().execute(this.velocitySamples::poll);
        event.getTasksAfterSend().add(() -> this.playerData.getPacketHandler().sendConfirmation(false));
    }

    public void handleExplosionPacket(long timestamp, WrapperPlayServerExplosion packet, PacketSendEvent event) {
        if (packet.getKnockback() == null) {
            return;
        }
        this.ticksSinceExplosion = 0;
        this.playerData.getPacketHandler().runOrQueueOnSummary(() -> this.explosionSamples.add(packet.getKnockback()));
        this.playerData.getPacketHandler().execute(this.explosionSamples::poll);
        event.getTasksAfterSend().add(() -> this.playerData.getPacketHandler().sendConfirmation(false));
    }

    public void handleFlyingPacket() {
        ++this.flyingTicks;
        ++this.transactionTicks;
        ++this.ticksSinceVelocity;
        ++this.ticksSinceExplosion;
        if (this.velocitySamples.isEmpty()) {
            return;
        }
        Object sample = this.velocitySamples.peekFirst();
        if (!(sample instanceof Vector3d)) {
            return;
        }
        Vector3d velocity = (Vector3d) sample;
        this.velocityX = velocity.getX();
        this.lastVelocityY = velocity.getY();
        this.lastQueuedVelocityY = velocity.getY();
        this.velocityZ = velocity.getZ();
    }

    public void handleClientTickEnd() {
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public LinkedList getVelocitySamples() {
        return this.velocitySamples;
    }

    public LinkedList getExplosionSamples() {
        return this.explosionSamples;
    }

    public boolean isAwaitingFlying() {
        return this.awaitingFlying;
    }

    public boolean isAwaitingInteract() {
        return this.awaitingInteract;
    }

    public double getLastQueuedVelocityY() {
        return this.lastQueuedVelocityY;
    }

    public double getLastVelocityY() {
        return this.lastVelocityY;
    }

    public double getVelocityX() {
        return this.velocityX;
    }

    public double getVelocityZ() {
        return this.velocityZ;
    }

    public int getFlyingTicks() {
        return this.flyingTicks;
    }

    public int getPing() {
        return (int) (this.playerData.getPacketHandler().getLastReplyElapsed() / 50L);
    }

    public int getTransactionPing() {
        return (int) this.playerData.getPacketHandler().getLastReplyElapsed();
    }

    public int getTicksSinceVelocity() {
        return this.ticksSinceVelocity;
    }

    public int getTicksSinceExplosion() {
        return this.ticksSinceExplosion;
    }

    private void queueVelocity(WrapperPlayServerEntityVelocity packet) {
        this.lastQueuedVelocityY = packet.getVelocity().getY();
        this.ticksSinceVelocity = 0;
        this.velocitySamples.add(packet.getVelocity());
    }
}
