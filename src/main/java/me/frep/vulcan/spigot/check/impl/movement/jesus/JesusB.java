package me.frep.vulcan.spigot.check.impl.movement.jesus;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Jesus", type = 'B', complexType = "Motion", description = "Invalid Y-change in water.")
public class JesusB extends AbstractCheck {
    public JesusB(PlayerData playerData) {
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
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (this.isExempt(
                ExemptType.LILY_PAD, ExemptType.STAIRS, ExemptType.SLAB, ExemptType.CAMPFIRE, ExemptType.CARPET,
                ExemptType.END_ROD, ExemptType.FENCE, ExemptType.BOAT, ExemptType.FLOWER_POT, ExemptType.CANCELLED_PLACE,
                ExemptType.CHAIN, ExemptType.SNOW, ExemptType.SWIMMING_JESUS, ExemptType.BED, ExemptType.TRAPDOOR,
                ExemptType.FULLY_SUBMERGED, ExemptType.BUBBLE_COLUMN, ExemptType.VELOCITY, ExemptType.DEPTH_STRIDER,
                ExemptType.DOLPHINS_GRACE, ExemptType.SEA_PICKLE, ExemptType.SWIMMING_ON_OLD_VERSION, ExemptType.SPECTATOR,
                ExemptType.SOUL_SAND, ExemptType.NOT_MOVING, ExemptType.NEAR_SOLID, ExemptType.CHUNK, ExemptType.SKULL,
                ExemptType.CLIMBABLE, ExemptType.HAPPY_GHAST, ExemptType.SCAFFOLDING, ExemptType.SLOW_FALLING)) {
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
        this.fail("deltaY=" + deltaY + " deltaXZ=" + deltaXZ);
        this.playerData.getStatusTracker().setJesusFlagged(true);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
