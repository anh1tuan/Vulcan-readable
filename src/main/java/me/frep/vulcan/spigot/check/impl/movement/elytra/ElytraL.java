package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'L', complexType = "Ascending", description = "Moving up wrongly.")
public class ElytraL extends AbstractCheck {
    public ElytraL(PlayerData playerData) {
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
        float pitch = this.playerData.getActionTracker().getPitch();
        if (deltaY <= 0.0) {
            return;
        }
        if (pitch > -10.0f) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.RIPTIDE)) {
            return;
        }
        this.fail("deltaY=" + deltaY + " pitch=" + pitch
                + " rt=" + this.playerData.getBlockBreakTracker().getAirTicks());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
