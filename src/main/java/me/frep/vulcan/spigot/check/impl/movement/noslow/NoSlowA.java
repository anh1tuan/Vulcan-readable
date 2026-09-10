package me.frep.vulcan.spigot.check.impl.movement.noslow;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "No Slow", type = 'A', complexType = "Packet", description = "Placing while digging")
public class NoSlowA extends AbstractCheck {
    public NoSlowA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isDiggingPacket(event)) {
            return;
        }
        if (this.playerData.getLastBlockPlacePacket() == null) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("placing while digging");
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
