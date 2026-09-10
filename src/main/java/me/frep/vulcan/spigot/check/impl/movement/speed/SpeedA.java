package me.frep.vulcan.spigot.check.impl.movement.speed;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Speed", type = 'A', complexType = "Friction", description = "Invalid friction.")
public class SpeedA extends AbstractCheck {
    private double lastDeltaXZ;

    public SpeedA(PlayerData playerData) {
        super(playerData);
        this.lastDeltaXZ = 0.0;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (this.isExempt(
                ExemptType.ILLEGAL_BLOCK, ExemptType.FULLY_STUCK, ExemptType.DOLPHINS_GRACE, ExemptType.WEB,
                ExemptType.SLEEPING, ExemptType.GLIDING, ExemptType.ELYTRA, ExemptType.DEPTH_STRIDER, ExemptType.VOID,
                ExemptType.WORLD_CHANGE, ExemptType.CHUNK, ExemptType.JOINED, ExemptType.ENTITY_COLLISION,
                ExemptType.ANVIL, ExemptType.TRAPDOOR, ExemptType.SPECTATOR, ExemptType.PARTIALLY_STUCK,
                ExemptType.FENCE_GATE, ExemptType.FROZEN, ExemptType.CHORUS_FRUIT, ExemptType.BLOCK_BREAK,
                ExemptType.CANCELLED_MOVE)) {
            this.lastDeltaXZ = deltaXZ;
            return;
        }
        if (airTicks <= 2) {
            this.lastDeltaXZ = deltaXZ;
            return;
        }
        double predicted = this.lastDeltaXZ * 0.9100000262260437 + 0.026;
        double difference = Math.abs(deltaXZ - predicted);
        this.lastDeltaXZ = deltaXZ;
        if (difference < 0.001) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("difference=" + difference + " ticks=" + airTicks + " deltaXZ=" + deltaXZ);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
