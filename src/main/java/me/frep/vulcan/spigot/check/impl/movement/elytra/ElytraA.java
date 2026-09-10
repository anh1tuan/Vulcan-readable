package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'A', complexType = "Limit", description = "Gliding too quickly on X/Z axis.")
public class ElytraA extends AbstractCheck {
    public ElytraA(PlayerData playerData) {
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
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.RIPTIDE, ExemptType.VELOCITY)) {
            return;
        }
        if (deltaXZ < 1.5) {
            return;
        }
        this.fail("deltaXZ=" + deltaXZ);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
