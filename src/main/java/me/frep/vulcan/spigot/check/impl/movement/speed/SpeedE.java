package me.frep.vulcan.spigot.check.impl.movement.speed;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Speed", type = 'E', complexType = "Prediction", description = "Invalid friction.")
public class SpeedE extends AbstractCheck {
    private double lastDeltaXZ;

    public SpeedE(PlayerData playerData) {
        super(playerData);
        this.lastDeltaXZ = 0.0;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (this.isExempt(
                ExemptType.TELEPORT, ExemptType.FLIGHT, ExemptType.VELOCITY, ExemptType.GLIDING, ExemptType.ELYTRA,
                ExemptType.DEPTH_STRIDER, ExemptType.DOLPHINS_GRACE, ExemptType.WEB, ExemptType.LIQUID,
                ExemptType.JOINED, ExemptType.WORLD_CHANGE)) {
            this.lastDeltaXZ = deltaXZ;
            return;
        }
        double predicted = this.lastDeltaXZ * 0.9100000262260437;
        double difference = Math.abs(deltaXZ - predicted);
        this.lastDeltaXZ = deltaXZ;
        if (difference < 1.0E-4) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("dXZ=" + deltaXZ + " diff=" + difference + " predicted=" + predicted);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
