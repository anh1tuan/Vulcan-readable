package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'I', complexType = "Motion", description = "Invalid motion.")
public class ElytraI extends AbstractCheck {
    public ElytraI(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (!this.playerData.isGliding()) {
            return;
        }
        double locationY = this.playerData.getBlockBreakTracker().getLocationY();
        double remainder = Math.abs(locationY % 0.1);
        if (remainder <= 1.0E-8 || remainder >= 0.1 - 1.0E-8) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("y=" + locationY + " remainder=" + remainder);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
