package me.frep.vulcan.spigot.check.impl.movement.flight;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Flight", type = 'D', complexType = "Glide", description = "Invalid Y-Axis movement.")
public class FlightD extends AbstractCheck {
    public FlightD(PlayerData playerData) {
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
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        if (airTicks < 10) {
            return;
        }
        if (deltaY >= -0.08 || deltaY <= -0.42) {
            return;
        }
        if (this.isExempt(
                ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.CLIMBABLE, ExemptType.LIQUID, ExemptType.VELOCITY,
                ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.LEVITATION, ExemptType.SLOW_FALLING, ExemptType.WEB,
                ExemptType.ELYTRA, ExemptType.GLIDING, ExemptType.BOAT, ExemptType.VEHICLE, ExemptType.HONEY,
                ExemptType.SLIME, ExemptType.BUBBLE_COLUMN)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isFlightExempt()
                || this.playerData.getBlockBreakTracker().isGlidingExempt()) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaY=" + deltaY + " ticks=" + airTicks
                + " delay=" + this.playerData.getStatusTracker().getTicksSinceDig()
                + " tp=" + this.playerData.getStatusTracker().getTicksSinceQueuedTeleport()
                + " vel=" + this.playerData.getTickTracker().getTicksSinceVelocity());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
