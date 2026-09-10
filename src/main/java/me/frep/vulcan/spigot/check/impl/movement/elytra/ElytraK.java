package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'K', complexType = "Acceleration", description = "Accelerating while ascending.")
public class ElytraK extends AbstractCheck {
    private double lastDeltaXZ;

    public ElytraK(PlayerData playerData) {
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
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        boolean accelerating = deltaXZ > this.lastDeltaXZ;
        this.lastDeltaXZ = deltaXZ;
        if (deltaY <= 0.0 || !accelerating) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.RIPTIDE)) {
            return;
        }
        this.fail("deltaXZ=" + deltaXZ + " accel=" + accelerating
                + " ticks=" + this.playerData.getBlockBreakTracker().getAirTicks());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
