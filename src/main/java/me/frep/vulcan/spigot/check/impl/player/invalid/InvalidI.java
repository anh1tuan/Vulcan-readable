package me.frep.vulcan.spigot.check.impl.player.invalid;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Invalid", type = 'I', complexType = "Invalid Y movement", description = "Impossible Y change.")
public class InvalidI extends AbstractCheck {
    public InvalidI(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (!this.playerData.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            return;
        }
        if (this.isOnGround()) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (Math.abs(deltaY) < 10.0) {
            return;
        }
        if (this.isExempt(
                ExemptType.FLIGHT, ExemptType.ELYTRA, ExemptType.VEHICLE, ExemptType.TELEPORT, ExemptType.JOINED,
                ExemptType.VELOCITY, ExemptType.SLIME, ExemptType.RIPTIDE, ExemptType.LEVITATION, ExemptType.WORLD_CHANGE)) {
            return;
        }
        this.fail("deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
