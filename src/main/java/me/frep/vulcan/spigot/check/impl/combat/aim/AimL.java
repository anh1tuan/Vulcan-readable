package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'L', complexType = "Direction", description = "Switching directions too quickly.")
public class AimL extends AbstractCheck {
    private float lastPitchDelta;

    public AimL(PlayerData playerData) {
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
        float pitchDelta = this.playerData.getActionTracker().getPitch()
                - this.playerData.getActionTracker().getPreviousPitchDelta();
        boolean switched = this.lastPitchDelta != 0.0f
                && Math.signum(pitchDelta) != Math.signum(this.lastPitchDelta)
                && Math.abs(pitchDelta) > 1.0f
                && Math.abs(this.lastPitchDelta) > 1.0f;
        this.lastPitchDelta = pitchDelta;
        if (!switched) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaPitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
