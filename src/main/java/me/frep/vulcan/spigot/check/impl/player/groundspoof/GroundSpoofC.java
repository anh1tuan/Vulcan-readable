package me.frep.vulcan.spigot.check.impl.player.groundspoof;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Ground Spoof", type = 'C', complexType = "Spoof", description = "Spoofed OnGround value.")
public class GroundSpoofC extends AbstractCheck {
    public GroundSpoofC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        boolean packetOnGround = this.playerData.getBlockBreakTracker().isClientOnGround();
        boolean serverOnGround = this.playerData.getBlockBreakTracker().isNearbySolid();
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (deltaY < -0.5) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.SLIME, ExemptType.VEHICLE, ExemptType.BOAT)) {
            return;
        }
        if (packetOnGround == serverOnGround) {
            return;
        }
        if (packetOnGround) {
            return;
        }
        this.fail("client=" + packetOnGround + "server=" + serverOnGround + "deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
