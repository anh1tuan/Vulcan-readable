package me.frep.vulcan.spigot.check.impl.movement.noslow;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "No Slow", type = 'B', complexType = "Soul Sand", description = "Moving too quickly on Soul Sand.")
public class NoSlowB extends AbstractCheck {
    public NoSlowB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isPositionPacket(event)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().getSoulSandTicks() != 0) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double max = 0.11 + this.playerData.getStatusTracker().getEntityInteractionRange() * 0.0625;
        if (this.playerData.getStatusTracker().getSoulSpeedLevel() > 0) {
            max += this.playerData.getStatusTracker().getSoulSpeedLevel() * 0.0625;
        }
        if (this.playerData.getBlockBreakTracker().getRecordedDeltaXZ() <= 0.11f
                && this.playerData.getStatusTracker().getWalkSpeed() <= 0.10999999940395355) {
            return;
        }
        if (this.isExempt(ExemptType.VELOCITY, ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.VEHICLE)) {
            return;
        }
        if (deltaXZ <= max) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaXZ=" + deltaXZ + " max=" + max);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
