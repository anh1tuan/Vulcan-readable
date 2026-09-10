package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'G', complexType = "Acceleration", description = "Invalid acceleration.")
public class ElytraG extends AbstractCheck {
    public ElytraG(PlayerData playerData) {
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
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double lastDeltaXZ = this.playerData.getBlockBreakTracker().getLastDeltaY();
        double acceleration = deltaXZ - Math.abs(lastDeltaXZ);
        if (acceleration <= 0.031) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("accel=" + acceleration + " deltaXZ=" + deltaXZ + " deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
