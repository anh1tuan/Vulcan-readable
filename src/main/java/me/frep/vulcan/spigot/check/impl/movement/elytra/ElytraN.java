package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'N', experimental = true, complexType = "Ground", description = "Moving too quickly on the ground.")
public class ElytraN extends AbstractCheck {
    public ElytraN(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (!this.playerData.isGliding()) {
            return;
        }
        if (this.playerData.getStatusTracker().getTicksWithoutAttributes() > 50) {
            return;
        }
        if (!this.playerData.getBlockBreakTracker().isNearbySolid()
                && !this.playerData.getBlockBreakTracker().isNearbyHeadSolid()) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        int iceTicks = this.playerData.getBlockBreakTracker().getTicksSinceFeetIce();
        double walkSpeed = this.playerData.getStatusTracker().getWalkSpeed();
        if (walkSpeed <= 0.0) {
            walkSpeed = 0.1;
        }
        double threshold = 0.2975 * (walkSpeed / 0.1);
        int speedAmplifier = this.playerData.getStatusTracker().getSpeedAmplifier();
        threshold += speedAmplifier * 0.2 * Math.pow(0.9150000214576721, this.playerData.getTickTracker().getPing());
        if (this.isExempt(ExemptType.SERVER_POSITION, ExemptType.CHORUS_FRUIT)) {
            return;
        }
        double difference = deltaXZ - threshold;
        if (difference < ConfigValues.checksMovementSpeedBMinDifference) {
            return;
        }
        this.fail("speed=" + deltaXZ + " max=" + threshold + " diff=" + difference + " ticks=" + iceTicks + " deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
