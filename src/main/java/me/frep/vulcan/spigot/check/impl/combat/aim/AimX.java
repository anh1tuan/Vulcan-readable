package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'X', complexType = "Analysis", description = "Generic rotation analysis heuristic.")
public class AimX extends AbstractCheck {
    public AimX(PlayerData playerData) {
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
        if (this.isExempt(ExemptType.CINEMATIC, ExemptType.SPECTATOR)) {
            return;
        }

        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        this.fail("sens=" + pitchDelta + " deltaYaw=" + yawDelta + " deltaPitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
