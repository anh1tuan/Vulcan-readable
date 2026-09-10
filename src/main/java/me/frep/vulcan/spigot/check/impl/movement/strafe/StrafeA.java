package me.frep.vulcan.spigot.check.impl.movement.strafe;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Strafe", type = 'A', complexType = "Air", description = "Moving incorrectly in the air.")
public class StrafeA extends AbstractCheck {
    private double lastDeltaX;
    private double lastDeltaZ;
    private boolean lastSprinting;

    public StrafeA(PlayerData playerData) {
        super(playerData);
        this.lastDeltaX = 0.0;
        this.lastDeltaZ = 0.0;
        this.lastSprinting = false;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double deltaX = this.playerData.getBlockBreakTracker().getLocationX()
                - this.playerData.getBlockBreakTracker().getLastLocationX();
        double deltaZ = this.playerData.getBlockBreakTracker().getLocationZ()
                - this.playerData.getBlockBreakTracker().getLastLocationZ();
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (this.isOnGround() || this.playerData.getBlockBreakTracker().isClientOnGround()) {
            this.lastDeltaX = deltaX;
            this.lastDeltaZ = deltaZ;
            return;
        }
        if (this.isExempt(
                ExemptType.VELOCITY, ExemptType.TELEPORT, ExemptType.FLIGHT, ExemptType.ELYTRA, ExemptType.GLIDING,
                ExemptType.LIQUID, ExemptType.WEB, ExemptType.CLIMBABLE, ExemptType.JOINED, ExemptType.VEHICLE)) {
            this.lastDeltaX = deltaX;
            this.lastDeltaZ = deltaZ;
            return;
        }
        double predictedX = this.lastDeltaX * 0.9100000262260437;
        double predictedZ = this.lastDeltaZ * 0.9100000262260437;
        double difference = Math.hypot(deltaX - predictedX, deltaZ - predictedZ);
        boolean sprinting = this.playerData.getPlayer() != null && this.playerData.getPlayer().isSprinting();
        if (this.lastSprinting) {
            difference /= 1.3;
        }
        double accelerationThreshold = sprinting ? 0.026 : 0.02;
        difference -= accelerationThreshold;
        this.lastDeltaX = deltaX;
        this.lastDeltaZ = deltaZ;
        this.lastSprinting = sprinting;
        if (difference < 0.0075) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("difference=" + difference + " deltaXZ=" + deltaXZ);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
