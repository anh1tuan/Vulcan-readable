package me.frep.vulcan.spigot.check.impl.movement.flight;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Flight", type = 'A', complexType = "Prediction [S]", description = "Invalid Y-Axis movement.")
public class FlightA extends AbstractCheck {
    public FlightA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isClientOnGround()) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isBlockGridAlignedY()) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (Math.abs(deltaY - 0.11760000228882461) <= 0.001 && this.isExempt(ExemptType.FAST)) {
            return;
        }
        double lastDeltaY = this.playerData.getBlockBreakTracker().getLastDeltaY();
        if (lastDeltaY + 0.09800000190734881 <= 0.001) {
            return;
        }
        double gravity = this.playerData.getStatusTracker().getGravity();
        double expectedDeltaY = (lastDeltaY - gravity) * 0.9800000190734863;
        double difference = Math.abs(deltaY - expectedDeltaY);
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        if (airTicks < 30 && this.isExempt(ExemptType.PISTON)) {
            return;
        }
        if (this.isExempt(
                ExemptType.SHULKER_BOX, ExemptType.WEB, ExemptType.BOAT, ExemptType.ILLEGAL_BLOCK, ExemptType.SLEEPING,
                ExemptType.SLOW_FALLING, ExemptType.SHULKER, ExemptType.TRAPDOOR, ExemptType.FISHING_ROD,
                ExemptType.LILY_PAD, ExemptType.SPECTATOR, ExemptType.HONEY, ExemptType.CHORUS_FRUIT, ExemptType.BED,
                ExemptType.CHAIN, ExemptType.WORLD_CHANGE, ExemptType.CANCELLED_PLACE, ExemptType.GLIDING,
                ExemptType.ELYTRA, ExemptType.LAGGED_NEAR_GROUND_MODERN, ExemptType.EMPTIED_BUCKET, ExemptType.DEAD,
                ExemptType.CANCELLED_BREAK, ExemptType.FENCE, ExemptType.LAGGED_NEAR_GROUND, ExemptType.BLOCK_PLACE_FAST,
                ExemptType.GLASS_PANE, ExemptType.NOT_MOVING, ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.CLIMBABLE,
                ExemptType.LIQUID, ExemptType.VELOCITY, ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.LEVITATION,
                ExemptType.BUBBLE_COLUMN, ExemptType.SLIME)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isFlightExempt()) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isGlidingExempt()) {
            return;
        }
        if (difference < 0.001) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        int teleportTicks = this.playerData.getStatusTracker().getTicksSinceQueuedTeleport();
        int velocityTicks = this.playerData.getTickTracker().getTicksSinceVelocity();
        this.fail("deltaY=" + deltaY + " difference=" + difference + " ticks=" + airTicks + " tp=" + teleportTicks + " vel=" + velocityTicks);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
