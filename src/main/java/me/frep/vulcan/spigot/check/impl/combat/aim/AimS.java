package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'S', complexType = "Divisor Y", experimental = false, description = "Subtle aim assist modifications.")
public class AimS extends AbstractCheck {
    public AimS(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isRotationPacket(event) || this.getTicksSinceAttack() >= 3) {
            return;
        }
        if (!this.playerData.getActionTracker().isSensitivityInExpectedRange()) {
            return;
        }
        double sensitivity = this.playerData.getActionTracker().getSensitivity();
        if (sensitivity < 0.01) {
            return;
        }
        float factor = (float) sensitivity * 0.6f + 0.2f;
        float gcdStep = factor * factor * factor * 1.2f;
        if ((double) gcdStep < 1.0E-4) {
            return;
        }
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        double yawDivisor = (double) yawDelta / (double) gcdStep;
        double pitchDivisor = (double) pitchDelta / (double) gcdStep;
        double yawError = Math.abs((double) Math.round(yawDivisor) - yawDivisor);
        double pitchError = Math.abs((double) Math.round(pitchDivisor) - pitchDivisor);
        if (pitchError > 0.03 && yawError < 1.0E-4 && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("divisorX=" + yawError + " divisorY=" + pitchError);
        } else {
            this.applyBufferDecay();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
