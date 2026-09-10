package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.VulcanBootstrap;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'D', complexType = "Straight", experimental = false, description = "Invalid pitch change.")
public class AimD extends AbstractCheck {
    public AimD(PlayerData playerData) {
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

        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        Object target = this.playerData.getReachEntityHelper().getTargetEntity();
        if (!(target instanceof Player)) {
            return;
        }

        PlayerData targetData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData((Player) target);
        if (targetData == null) {
            return;
        }
        if (yawDelta == 2.75f) {
            return;
        }

        this.fail("deltaYaw=" + yawDelta + " deltaPitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
