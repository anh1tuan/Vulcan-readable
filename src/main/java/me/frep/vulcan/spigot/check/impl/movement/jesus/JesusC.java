package me.frep.vulcan.spigot.check.impl.movement.jesus;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Jesus", type = 'C', complexType = "Y Motion", description = "Invalid Y-Change in water.")
public class JesusC extends AbstractCheck {
    public JesusC(PlayerData playerData) {
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
        if (this.isExempt(
                ExemptType.LILY_PAD, ExemptType.STAIRS, ExemptType.SLAB, ExemptType.CAMPFIRE, ExemptType.CARPET,
                ExemptType.END_ROD, ExemptType.FENCE, ExemptType.BOAT, ExemptType.WEB, ExemptType.HAPPY_GHAST,
                ExemptType.CANCELLED_PLACE, ExemptType.CHAIN, ExemptType.SNOW, ExemptType.SWIMMING_JESUS, ExemptType.BED,
                ExemptType.TRAPDOOR, ExemptType.FULLY_SUBMERGED, ExemptType.VELOCITY, ExemptType.BUBBLE_COLUMN,
                ExemptType.SWIMMING_ON_OLD_VERSION, ExemptType.SOUL_SAND, ExemptType.CLIMBABLE, ExemptType.SPECTATOR,
                ExemptType.NEAR_SOLID, ExemptType.SKULL, ExemptType.FLOWER_POT, ExemptType.CHUNK, ExemptType.NOT_MOVING,
                ExemptType.SCAFFOLDING)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().getPreviousDeltaY() != 0.0
                || this.playerData.getBlockBreakTracker().isFlightExempt()
                || this.playerData.getBlockBreakTracker().isGlidingExempt()) {
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
