package me.frep.vulcan.spigot.check.impl.combat.aim;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.VulcanBootstrap;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Aim", type = 'H', complexType = "Negative", experimental = false, description = "Invalid sensitivity.")
public class AimH extends AbstractCheck {
    public AimH(PlayerData playerData) {
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

        float lastPitch = this.playerData.getActionTracker().getPreviousPitchDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        Object target = this.playerData.getReachEntityHelper().getTargetEntity();
        if (!(target instanceof Player)) {
            return;
        }

        PlayerData targetData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData((Player) target);
        if (targetData == null) {
            return;
        }
        if (this.isExempt(ExemptType.CINEMATIC)) {
            return;
        }
        if (this.playerData.getActionTracker().isTickBelowFifty()) {
            return;
        }

        this.fail("final=" + lastPitch + " deltaPitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
