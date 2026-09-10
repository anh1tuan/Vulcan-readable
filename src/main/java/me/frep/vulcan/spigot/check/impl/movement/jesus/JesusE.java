package me.frep.vulcan.spigot.check.impl.movement.jesus;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Jesus", type = 'E', complexType = "Speed", description = "Moving too quickly on water.")
public class JesusE extends AbstractCheck {
    public JesusE(PlayerData playerData) {
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
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (this.isExempt(
                ExemptType.LILY_PAD, ExemptType.STAIRS, ExemptType.SLAB, ExemptType.CAMPFIRE, ExemptType.CARPET,
                ExemptType.END_ROD, ExemptType.FENCE, ExemptType.FLOWER_POT, ExemptType.HAPPY_GHAST, ExemptType.NOT_MOVING,
                ExemptType.CANCELLED_PLACE, ExemptType.CHAIN, ExemptType.SNOW, ExemptType.SWIMMING_JESUS, ExemptType.BED,
                ExemptType.TRAPDOOR, ExemptType.BOAT, ExemptType.FULLY_SUBMERGED, ExemptType.BUBBLE_COLUMN, ExemptType.VELOCITY,
                ExemptType.DEPTH_STRIDER, ExemptType.DOLPHINS_GRACE, ExemptType.FENCE_GATE, ExemptType.SWIMMING_ON_OLD_VERSION,
                ExemptType.SPECTATOR, ExemptType.BLOCK_PLACE, ExemptType.CLIMBABLE, ExemptType.NEAR_SOLID,
                ExemptType.SLOW_FALLING, ExemptType.CHUNK, ExemptType.WALL, ExemptType.SCAFFOLDING, ExemptType.SLIME,
                ExemptType.SKULL)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isFlightExempt()
                || this.playerData.getBlockBreakTracker().isGlidingExempt()
                || this.playerData.getBlockBreakTracker().isLevitationExempt()
                || this.playerData.getBlockBreakTracker().isTeleportExempt()) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().getVehicleX() != 0.0
                || this.playerData.getBlockBreakTracker().getLastVehicleX() != 0.0) {
            return;
        }
        double max = 0.166;
        if (this.playerData.getBlockBreakTracker().getTicksSinceStandingOnSolidOrIce() >= 10) {
            max += 0.35;
        }
        if (deltaXZ <= max) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaXZ=" + deltaXZ + " max=" + max);
        this.playerData.getStatusTracker().setJesusFlagged(true);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
