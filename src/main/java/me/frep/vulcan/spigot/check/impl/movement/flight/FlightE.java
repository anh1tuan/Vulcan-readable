package me.frep.vulcan.spigot.check.impl.movement.flight;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Flight", type = 'E', complexType = "Hover", description = "Invalid Y-Axis movement.")
public class FlightE extends AbstractCheck {
    public FlightE(PlayerData playerData) {
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
        double lastDeltaY = this.playerData.getBlockBreakTracker().getLastDeltaY();
        double acceleration = Math.abs(deltaY - lastDeltaY);
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        if (airTicks < 8) {
            return;
        }
        if (Math.abs(deltaY) > 0.03) {
            return;
        }
        if (acceleration > 0.01) {
            return;
        }
        if (this.isExempt(
                ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.CLIMBABLE, ExemptType.LIQUID, ExemptType.VELOCITY,
                ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.LEVITATION, ExemptType.SLOW_FALLING, ExemptType.WEB,
                ExemptType.ELYTRA, ExemptType.GLIDING, ExemptType.BOAT, ExemptType.VEHICLE, ExemptType.HONEY,
                ExemptType.SLIME, ExemptType.BUBBLE_COLUMN, ExemptType.SCAFFOLDING)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isFlightExempt()
                || this.playerData.getBlockBreakTracker().isGlidingExempt()) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("ticks=" + airTicks + " acceleration=" + acceleration + " deltaY=" + deltaY
                + " delay=" + this.playerData.getStatusTracker().getTicksSinceDig()
                + " tp=" + this.playerData.getStatusTracker().getTicksSinceQueuedTeleport()
                + " vel=" + this.playerData.getTickTracker().getTicksSinceVelocity());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
