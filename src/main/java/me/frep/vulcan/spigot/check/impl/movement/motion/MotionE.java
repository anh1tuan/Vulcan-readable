package me.frep.vulcan.spigot.check.impl.movement.motion;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Motion", type = 'E', complexType = "Modulo", experimental = false, description = "Impossible movement values.")
public class MotionE extends AbstractCheck {
    public MotionE(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isPositionPacket(event)) {
            return;
        }
        double locationY = this.playerData.getBlockBreakTracker().getLocationY();
        double remainder = Math.abs(locationY % 0.1);
        if (remainder <= 1.0E-8 || remainder >= 0.1 - 1.0E-8) {
            return;
        }
        if (Math.abs(locationY) < 0.11) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("y=" + locationY + " modulo=" + remainder);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
