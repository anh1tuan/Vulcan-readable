package me.frep.vulcan.spigot.check.impl.movement.noslow;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "No Slow", type = 'C', complexType = "Web", description = "Moving too quickly in a web.")
public class NoSlowC extends AbstractCheck {
    public NoSlowC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isPositionPacket(event)) {
            return;
        }
        if (!this.isExempt(ExemptType.WEB)) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double max = 0.09;
        if (this.playerData.getBlockBreakTracker().getWebSlowdown() == 0.0) {
            return;
        }
        if (deltaXZ <= max) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaXZ=" + deltaXZ + "max=" + max);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
