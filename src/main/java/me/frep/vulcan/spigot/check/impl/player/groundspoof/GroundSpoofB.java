package me.frep.vulcan.spigot.check.impl.player.groundspoof;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Ground Spoof", type = 'B', complexType = "Spoof", description = "Spoofed OnGround value.")
public class GroundSpoofB extends AbstractCheck {
    public GroundSpoofB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        boolean packetOnGround = this.playerData.getBlockBreakTracker().isClientOnGround();
        boolean serverOnGround = this.playerData.getBlockBreakTracker().isNearbySolid();
        double locationY = this.playerData.getBlockBreakTracker().getLocationY();
        boolean fenceLike = Math.abs(locationY % 1.0 - 0.6) < 1.0E-4
                || Math.abs(locationY % 0.1 - 0.0625) < 1.0E-4
                || Math.abs(locationY % 1.0 - 0.5625) < 1.0E-4;
        if (fenceLike) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.SLIME, ExemptType.VEHICLE, ExemptType.BOAT)) {
            return;
        }
        if (packetOnGround == serverOnGround) {
            return;
        }
        if (!packetOnGround) {
            return;
        }
        this.fail("client=" + packetOnGround + "server=" + serverOnGround);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
