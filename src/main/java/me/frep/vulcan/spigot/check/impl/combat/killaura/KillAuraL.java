package me.frep.vulcan.spigot.check.impl.combat.killaura;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Kill Aura", type = 'L', complexType = "Strafe", experimental = true, description = "Impossible strafe.")
public class KillAuraL extends AbstractCheck {
    public KillAuraL(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (this.getTicksSinceAttack() >= 3) {
            return;
        }
        if (this.playerData.getPlayer() == null) {
            return;
        }
        if (this.playerData.getReachEntityHelper().getTargetEntity() == null) {
            return;
        }
        if (this.isExempt(ExemptType.SOUL_SAND)) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        double locationY = this.playerData.getBlockBreakTracker().getLocationY();
        float acceleration = this.playerData.getActionTracker().getYawAcceleration();
        long clickInterval = this.playerData.getClickTracker().getLastClickInterval();
        this.fail("y=" + locationY + " accel=" + acceleration + " click=" + clickInterval);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
