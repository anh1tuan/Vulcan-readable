package me.frep.vulcan.spigot.check.impl.movement.motion;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Motion", type = 'B', complexType = "Inverse", description = "Repeated vertical motions.")
public class MotionB extends AbstractCheck {
    public MotionB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        double lastDeltaY = this.playerData.getBlockBreakTracker().getLastDeltaY();
        if (deltaY <= 0.0) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isClientOnGround()) {
            return;
        }
        if (this.isExempt(ExemptType.VELOCITY, ExemptType.TELEPORT, ExemptType.SLIME, ExemptType.WEB, ExemptType.FLIGHT, ExemptType.VEHICLE)) {
            return;
        }
        if (Math.abs(deltaY - lastDeltaY) > 1.0E-6) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
