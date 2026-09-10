package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.TrigUtil;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'K', complexType = "Linear", experimental = false, description = "Not constant rotations.")
public class AimK extends AbstractCheck {
    public AimK(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isRotationPacket(event) || this.getTicksSinceAttack() >= 3) {
            return;
        }
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        float lastYawDelta = this.playerData.getActionTracker().getPreviousYawDelta();
        float lastPitchDelta = this.playerData.getActionTracker().getPreviousPitchDelta();
        if (yawDelta <= 0.1f || pitchDelta <= 0.1f || yawDelta >= 20.0f || pitchDelta >= 20.0f) {
            return;
        }
        double yawGcd = (double) TrigUtil.gcd((long) ((double) yawDelta * TrigUtil.TWO_POW_24), (long) ((double) lastYawDelta * TrigUtil.TWO_POW_24)) / TrigUtil.TWO_POW_24;
        double pitchGcd = (double) TrigUtil.gcd((long) ((double) pitchDelta * TrigUtil.TWO_POW_24), (long) ((double) lastPitchDelta * TrigUtil.TWO_POW_24)) / TrigUtil.TWO_POW_24;
        if (yawGcd == 0.0 || pitchGcd == 0.0) {
            return;
        }
        double yawDivisor = (double) yawDelta / yawGcd;
        double lastYawDivisor = (double) lastYawDelta / yawGcd;
        double pitchDivisor = (double) pitchDelta / pitchGcd;
        double lastPitchDivisor = (double) lastPitchDelta / pitchGcd;
        double yawRemainder = yawDivisor % lastYawDivisor;
        double pitchRemainder = pitchDivisor % lastPitchDivisor;
        double yawError = Math.abs(Math.floor(yawRemainder) - yawRemainder);
        double pitchError = Math.abs(Math.floor(pitchRemainder) - pitchRemainder);
        boolean linearYaw = yawRemainder > 60.0 || yawError > 0.1;
        boolean linearPitch = pitchRemainder > 60.0 || pitchError > 0.1;
        boolean pitchInRange = pitchDelta > -1.0f && pitchDelta < 100.0f;
        if (linearYaw && linearPitch && pitchInRange && !this.isExempt(ExemptType.CINEMATIC)
            && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("deltaYaw=" + yawDelta + " deltaPitch=" + pitchDelta);
        } else {
            this.applyBufferDecay();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
