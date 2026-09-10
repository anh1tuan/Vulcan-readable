package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'C', complexType = "Acceleration", description = "Invalid acceleration.")
public class ElytraC extends AbstractCheck {
    private double lastDeltaXZ;

    public ElytraC(PlayerData playerData) {
        super(playerData);
        this.lastDeltaXZ = 0.0;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (!this.playerData.isGliding()) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        double accel = Math.abs(deltaXZ - this.lastDeltaXZ);
        this.lastDeltaXZ = deltaXZ;
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.RIPTIDE)) {
            return;
        }
        if (accel < 0.1) {
            return;
        }
        this.fail("accel=" + accel + " deltaXZ=" + deltaXZ + " deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
