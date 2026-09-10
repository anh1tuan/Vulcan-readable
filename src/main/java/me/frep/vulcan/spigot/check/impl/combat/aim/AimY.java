package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'Y', complexType = "Rotation", description = "Generic rotation analysis heuristic.")
public class AimY extends AbstractCheck {
    public AimY(PlayerData playerData) {
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
        if (this.isOnGround()) {
            return;
        }
        float previousYaw = this.playerData.getActionTracker().getPreviousYaw();
        float yaw = this.playerData.getActionTracker().getYaw();
        double diff = Math.abs(yaw - previousYaw);
        double distance = this.playerData.getReachEntityHelper().getClosestReachDistance();
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.DEATH, ExemptType.WORLD_CHANGE)) {
            return;
        }
        if (this.playerData.getStatusTracker().getTicksSinceQueuedTeleport() < 20) {
            return;
        }
        if (distance <= 0.6 || distance >= 10.0) {
            return;
        }
        this.fail("diff=" + diff + "dist=" + distance);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
