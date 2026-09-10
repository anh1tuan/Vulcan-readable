package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.TrigUtil;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'I', complexType = "Constant", experimental = false, description = "Not constant rotations.")
public class AimI extends AbstractCheck {
    public AimI(PlayerData playerData) {
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

        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        float lastPitch = this.playerData.getActionTracker().getPreviousPitchDelta();
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        long gcdExpanded = (long) (TrigUtil.TWO_POW_24 * (double) pitchDelta);
        long lastPitchExpanded = (long) (TrigUtil.TWO_POW_24 * (double) lastPitch);
        long gcd = TrigUtil.gcd(gcdExpanded, lastPitchExpanded);

        if (this.isExempt(ExemptType.CINEMATIC)) {
            return;
        }
        if (this.playerData.getActionTracker().isTickBelowFifty()) {
            return;
        }

        this.fail("rotation=" + gcd / 1000L + " deltaPitch=" + pitchDelta + " deltaYaw=" + yawDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
