package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'O', complexType = "Small Yaw", description = "Too small pitch change.")
public class AimO extends AbstractCheck {
    public AimO(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isRotationPacket(event)) {
            return;
        }
        if (!this.isTicksSinceAttackBelow(2)) {
            return;
        }
        if (!(this.playerData.getReachEntityHelper().getTargetEntity() instanceof Player)) {
            return;
        }
        if (this.isExempt(ExemptType.VEHICLE, ExemptType.TELEPORT)) {
            return;
        }
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        if (pitchDelta >= 0.05f || yawDelta < 0.03f) {
            return;
        }
        this.fail("deltaPitch=" + pitchDelta + "deltaYaw=" + yawDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
