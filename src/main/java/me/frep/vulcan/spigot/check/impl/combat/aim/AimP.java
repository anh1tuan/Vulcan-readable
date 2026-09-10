package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'P', complexType = "Yaw Acceleration", experimental = false, description = "Large yaw acceleration.")
public class AimP extends AbstractCheck {
    public AimP(PlayerData playerData) {
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
        float yawAccel = this.playerData.getActionTracker().getYawAcceleration();
        float pitchAccel = this.playerData.getActionTracker().getPitchAcceleration();
        if (yawAccel < 20.0f) {
            return;
        }
        this.fail("yawAccel=" + yawAccel + "pitchAccel=" + pitchAccel);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
