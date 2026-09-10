package me.frep.vulcan.spigot.check.impl.movement.motion;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Motion", type = 'C', complexType = "Jump", description = "Invalid jump motion.")
public class MotionC extends AbstractCheck {
    public MotionC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        double jumpOffset = Math.abs(Math.abs(deltaY) - 0.41999998688697815);
        if (jumpOffset < 1.0E-5) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isClientOnGround()) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().getLastDeltaY() >= 0.0) {
            return;
        }
        if (deltaY <= 0.0) {
            return;
        }
        if (this.isExempt(ExemptType.JUMP_BOOST, ExemptType.SLIME, ExemptType.VELOCITY, ExemptType.TELEPORT, ExemptType.FLIGHT, ExemptType.STAIRS)) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaY=" + deltaY + " offset=" + jumpOffset);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
