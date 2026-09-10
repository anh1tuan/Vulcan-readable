package me.frep.vulcan.spigot.check.impl.combat.velocity;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Velocity", type = 'A', complexType = "Vertical", description = "Vertical velocity modifications.")
public class VelocityA extends AbstractCheck {
    public VelocityA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double velocityY = this.playerData.getTickTracker().getLastVelocityY();
        if (Math.abs(velocityY) < 0.1) {
            return;
        }
        if (this.isExempt(
                ExemptType.COLLIDING_VERTICALLY, ExemptType.LIQUID, ExemptType.WEB, ExemptType.SLIME, ExemptType.FLIGHT,
                ExemptType.ELYTRA, ExemptType.VEHICLE, ExemptType.CLIMBABLE, ExemptType.SLOW_FALLING, ExemptType.LEVITATION,
                ExemptType.BUBBLE_COLUMN, ExemptType.TELEPORT, ExemptType.JOINED)) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        double percent = deltaY / velocityY;
        if (percent >= 0.21) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("percent=" + percent * 100.0 + "% deltaY=" + deltaY + " velocityY=" + velocityY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
