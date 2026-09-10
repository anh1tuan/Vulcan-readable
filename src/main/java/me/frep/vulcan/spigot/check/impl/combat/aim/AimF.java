package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'F', complexType = "Straight", description = "Invalid yaw change.")
public class AimF extends AbstractCheck {
    public AimF(PlayerData playerData) {
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
        if (!(this.playerData.getReachEntityHelper().getTargetEntity() instanceof Player)) {
            return;
        }
        if (this.isExempt(ExemptType.VEHICLE, ExemptType.TELEPORT)) {
            return;
        }
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        if (yawDelta == 0.0f || pitchDelta != 0.0f) {
            return;
        }
        this.fail("deltaYaw=" + yawDelta + "deltaPitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
