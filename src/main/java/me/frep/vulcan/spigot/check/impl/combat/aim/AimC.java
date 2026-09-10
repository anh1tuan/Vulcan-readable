package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'C', complexType = "Repeated", description = "Repeated yaw values.")
public class AimC extends AbstractCheck {
    public AimC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isRotationPacket(event)) {
            return;
        }
        if (!this.isTicksSinceAttackBelow(3)) {
            return;
        }

        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        if (yawDelta == 1.25f) {
            return;
        }

        this.fail("deltaYaw=" + yawDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
