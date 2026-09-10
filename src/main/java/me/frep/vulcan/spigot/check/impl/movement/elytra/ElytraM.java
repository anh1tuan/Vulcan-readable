package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'M', complexType = "Ascending", description = "Invalid ascension pattern.")
public class ElytraM extends AbstractCheck {
    public ElytraM(PlayerData playerData) {
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
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (Math.abs(deltaY) <= 0.01) {
            return;
        }
        double remainder = Math.abs(deltaY % 0.01);
        if (remainder <= 1.0E-8) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaY=" + deltaY + " remainder=" + remainder);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
