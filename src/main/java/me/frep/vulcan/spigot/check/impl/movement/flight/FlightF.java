package me.frep.vulcan.spigot.check.impl.movement.flight;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Flight", type = 'F', complexType = "Prediction", description = "Invalid Y-Axis movement.")
public class FlightF extends AbstractCheck {
    public FlightF(PlayerData playerData) {
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
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        double lastDeltaY = this.playerData.getBlockBreakTracker().getLastDeltaY();
        double expectedDeltaY = (lastDeltaY - this.playerData.getStatusTracker().getGravity()) * 0.9800000190734863;
        double difference = Math.abs(deltaY - expectedDeltaY);
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        int soulSandTicks = this.playerData.getBlockBreakTracker().getSoulSandTicks();
        if (this.isExempt(
                ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.CLIMBABLE, ExemptType.LIQUID, ExemptType.VELOCITY,
                ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.LEVITATION, ExemptType.SLOW_FALLING, ExemptType.WEB,
                ExemptType.ELYTRA, ExemptType.GLIDING, ExemptType.BOAT, ExemptType.VEHICLE, ExemptType.HONEY,
                ExemptType.SLIME, ExemptType.BUBBLE_COLUMN, ExemptType.PISTON, ExemptType.SOUL_SAND)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isFlightExempt()
                || this.playerData.getBlockBreakTracker().isGlidingExempt()) {
            return;
        }
        if (difference < 0.001) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("ticks=" + soulSandTicks + " ticks=" + airTicks + " value=" + deltaY + " diff=" + difference
                + " diff=" + difference + " ticks=" + airTicks
                + " tp=" + this.playerData.getStatusTracker().getTicksSinceQueuedTeleport()
                + " delay=" + this.playerData.getStatusTracker().getTicksSinceDig()
                + " vel=" + this.playerData.getTickTracker().getTicksSinceVelocity());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
