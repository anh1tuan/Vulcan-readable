package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'U', complexType = "GCD Flaw", description = "GCD bypass.")
public class AimU extends AbstractCheck {
    public AimU(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isRotationPacket(event)) {
            return;
        }
        if (this.getTicksSinceAttack() >= 3) {
            return;
        }
        if (!this.playerData.getActionTracker().isSensitivityInExpectedRange()) {
            return;
        }
        double sensitivity = this.playerData.getActionTracker().getSensitivity();
        float gcdStep = (float) sensitivity * 0.6f + 0.2f;
        gcdStep = gcdStep * gcdStep * gcdStep * 1.2f;
        float yaw = this.playerData.getActionTracker().getYaw();
        float pitch = this.playerData.getActionTracker().getPitch();
        double yawDifference = Math.abs(yaw - (yaw - yaw % gcdStep));
        double pitchDifference = Math.abs(pitch - (pitch - pitch % gcdStep));
        if (yawDifference > 0.01 || pitchDifference > 0.01) {
            return;
        }
        this.fail("diff=" + yawDifference + "pitchDiff=" + pitchDifference);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
