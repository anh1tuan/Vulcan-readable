package me.frep.vulcan.spigot.check.impl.player.invalid;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Invalid", type = 'C', complexType = "Y", description = "Too large Y-Axis movement.")
public class InvalidC extends AbstractCheck {
    public InvalidC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event) || this.isOnGround()) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (Math.abs(deltaY) < 4.0) {
            return;
        }
        if (this.isExempt(
                ExemptType.FLIGHT, ExemptType.ELYTRA, ExemptType.VEHICLE, ExemptType.TELEPORT, ExemptType.JOINED,
                ExemptType.VELOCITY, ExemptType.SLIME, ExemptType.PISTON, ExemptType.RIPTIDE, ExemptType.LEVITATION)) {
            return;
        }
        this.fail("deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
