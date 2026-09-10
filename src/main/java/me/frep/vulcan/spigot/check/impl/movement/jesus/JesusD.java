package me.frep.vulcan.spigot.check.impl.movement.jesus;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Jesus", type = 'D', complexType = "Jump", description = "Jumping on/in water.")
public class JesusD extends AbstractCheck {
    public JesusD(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isPositionPacket(event)) {
            return;
        }
        if (!this.playerData.getBlockBreakTracker().isFeetOnIce()) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().getLocationY() > 250.0) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (deltaY <= 0.0) {
            return;
        }
        if (this.isExempt(
                ExemptType.LILY_PAD, ExemptType.STAIRS, ExemptType.SLAB, ExemptType.CAMPFIRE, ExemptType.CARPET,
                ExemptType.END_ROD, ExemptType.FENCE, ExemptType.BOAT, ExemptType.FULLY_SUBMERGED, ExemptType.BUBBLE_COLUMN,
                ExemptType.VELOCITY, ExemptType.SWIMMING_JESUS, ExemptType.TRAPDOOR, ExemptType.BED, ExemptType.CHAIN,
                ExemptType.SNOW, ExemptType.SPECTATOR, ExemptType.CLIMBABLE, ExemptType.NEAR_SOLID, ExemptType.CHUNK,
                ExemptType.SCAFFOLDING, ExemptType.SLIME, ExemptType.JUMP_BOOST)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isFlightExempt()
                || this.playerData.getBlockBreakTracker().isGlidingExempt()
                || this.playerData.getBlockBreakTracker().isTeleportExempt()) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaY=" + deltaY);
        this.playerData.getStatusTracker().setJesusFlagged(true);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
