package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = '5', complexType = "Scaffold", description = "Invalid Block place.")
public class BadPackets5 extends AbstractCheck {
    public BadPackets5(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isBlockPlacePacket(event) || this.isExempt(ExemptType.CREATIVE, ExemptType.CLIENT_1_9_OR_NEWER)) {
            return;
        }
        if (this.playerData.getLastBlockPlacePacket() == null) {
            return;
        }
        double x = this.playerData.getLastBlockPlacePacket().getCursorPosition().getX();
        double y = this.playerData.getLastBlockPlacePacket().getCursorPosition().getY();
        double z = this.playerData.getLastBlockPlacePacket().getCursorPosition().getZ();
        if ((x < 0.0 || x > 1.0 || y < 0.0 || y > 1.0 || z < 0.0 || z > 1.0)
            && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("x=" + x + " y=" + y + " z=" + z);
        } else {
            this.applyBufferDecay();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
