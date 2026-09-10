package me.frep.vulcan.spigot.check.impl.movement.jesus;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Jesus", type = 'A', complexType = "Ground", description = "Walking on water.")
public class JesusA extends AbstractCheck {
    public JesusA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().getLocationY() > 250.0) {
            return;
        }
        if (!this.playerData.getBlockBreakTracker().isClientOnGround()) {
            return;
        }
        if (!this.playerData.getBlockBreakTracker().isBlockGridAlignedY()) {
            return;
        }
        if (this.isExempt(
                ExemptType.LILY_PAD, ExemptType.STAIRS, ExemptType.SLAB, ExemptType.CAMPFIRE, ExemptType.CARPET,
                ExemptType.END_ROD, ExemptType.FENCE, ExemptType.BOAT, ExemptType.HAPPY_GHAST, ExemptType.CANCELLED_PLACE,
                ExemptType.CHAIN, ExemptType.SNOW, ExemptType.SWIMMING_JESUS, ExemptType.BED, ExemptType.TRAPDOOR,
                ExemptType.DOLPHINS_GRACE, ExemptType.FLOWER_POT, ExemptType.SPECTATOR, ExemptType.SCAFFOLDING,
                ExemptType.SKULL, ExemptType.SEA_PICKLE, ExemptType.NEAR_SOLID, ExemptType.NOT_MOVING, ExemptType.BLOCK_PLACE)) {
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
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("y=" + this.playerData.getBlockBreakTracker().getLocationY());
        this.playerData.getStatusTracker().setJesusFlagged(true);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
