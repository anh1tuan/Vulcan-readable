package me.frep.vulcan.spigot.check.impl.player.groundspoof;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Ground Spoof", type = 'A', complexType = "Spoof", description = "Spoofed onGround value.")
public class GroundSpoofA extends AbstractCheck {
    public GroundSpoofA(PlayerData playerData) {
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
        int teleportTicks = this.playerData.getStatusTracker().getTicksSinceQueuedTeleport();
        if (teleportTicks < 20) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.SLIME, ExemptType.VEHICLE, ExemptType.BOAT)) {
            return;
        }
        if (packetOnGround == serverOnGround) {
            return;
        }
        if (packetOnGround && Math.abs(deltaY) <= 0.3116 + 0.005) {
            return;
        }
        this.fail("deltaY=" + deltaY + " ticks=" + this.playerData.getBlockBreakTracker().getTicksSinceFeetIce()
                + " packet=" + packetOnGround + " server=" + serverOnGround);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
