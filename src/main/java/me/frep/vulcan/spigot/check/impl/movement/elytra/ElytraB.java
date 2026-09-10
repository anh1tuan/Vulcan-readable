package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'B', complexType = "Acceleration", description = "Invalid Y-Axis change.")
public class ElytraB extends AbstractCheck {
    public ElytraB(PlayerData playerData) {
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
        double lastDeltaY = this.playerData.getBlockBreakTracker().getLastDeltaY();
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double accelY = Math.abs(deltaY - lastDeltaY);
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.RIPTIDE, ExemptType.VELOCITY)) {
            return;
        }
        if (accelY < 0.08) {
            return;
        }
        this.fail("ticks=" + this.playerData.getBlockBreakTracker().getAirTicks()
                + " deltaXZ=" + deltaXZ + " deltaY=" + deltaY + " accelY=" + accelY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
