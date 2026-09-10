package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'Q', complexType = "GCD Modulo", description = "GCD bypass flaw detected.")
public class AimQ extends AbstractCheck {
    public AimQ(PlayerData playerData) {
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
        if (sensitivity < 0.01) {
            return;
        }
        float gcdStep = (float) sensitivity * 0.6f + 0.2f;
        gcdStep = gcdStep * gcdStep * gcdStep * 1.2f;
        float yaw = this.playerData.getActionTracker().getYaw();
        float pitch = this.playerData.getActionTracker().getPitch();
        double yawDifference = Math.abs(yaw - (yaw - yaw % gcdStep));
        double pitchDifference = Math.abs(pitch - (pitch - pitch % gcdStep));
        float combined = this.playerData.getActionTracker().getYawDelta()
                + this.playerData.getActionTracker().getPitchDelta();
        if (this.isExempt(ExemptType.CREATIVE, ExemptType.SPECTATOR)) {
            return;
        }
        if (yawDifference == 0.0 && pitchDifference == 0.0) {
            return;
        }
        if (combined <= 0.75f) {
            return;
        }
        this.fail("yawDifference=" + yawDifference + "pitchDifference=" + pitchDifference + "combined=" + combined);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
