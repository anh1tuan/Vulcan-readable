package me.frep.vulcan.spigot.check.impl.movement.speed;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Speed", type = 'B', complexType = "Ground", description = "Moving too quickly on the ground.")
public class SpeedB extends AbstractCheck {
    public SpeedB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
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
        int soulSandTicks = this.playerData.getBlockBreakTracker().getSoulSandTicks();
        int ticksSinceSolidFeet = this.playerData.getBlockBreakTracker().getTicksSinceSolidFeet();
        int packedIceTicks = this.playerData.getBlockBreakTracker().getPackedIceTicks();
        int ping = this.playerData.getTickTracker().getPing();
        double vehicleDeltaY = this.playerData.getBlockBreakTracker().getVehicleDeltaY();
        boolean depthStrider = this.isExempt(ExemptType.DEPTH_STRIDER);
        double lastLocationY = this.playerData.getBlockBreakTracker().getLastLocationY();

        double threshold = 0.2975;
        double speedMultiplier = this.playerData.getStatusTracker().getWalkSpeed();
        if (speedMultiplier <= 0.0) {
            speedMultiplier = 0.1;
        }
        threshold *= speedMultiplier / 0.1;

        if (iceTicks == 0.0) {
            threshold += 0.323;
            if (this.playerData.getBlockBreakTracker().isNearbyHeadSolid()) {
                threshold += 0.076;
            }
            if (ticksSinceSolidFeet >= 60) {
                threshold += 0.35;
            }
            if (packedIceTicks >= 30) {
                threshold += 0.6;
            }
            if (lastLocationY == 0.0) {
                threshold += 0.1;
            }
        }
        if (ticksSinceSolidFeet < 30) {
            threshold += 0.2;
        }
        if (soulSandTicks < 10 && ticksSinceSolidFeet < 10) {
            threshold += 0.55;
        }
        int speedAmplifier = this.playerData.getStatusTracker().getSpeedAmplifier();
        threshold += speedAmplifier * 0.2 * Math.pow(0.9150000214576721, ping);
        if (threshold < 0.295) {
            threshold = 0.295;
        }
        if (depthStrider) {
            return;
        }
        double difference = deltaXZ - threshold;
        if (difference < ConfigValues.checksMovementSpeedBMinDifference) {
            return;
        }
        if (this.isExempt(ExemptType.SERVER_POSITION, ExemptType.CHORUS_FRUIT)) {
            return;
        }
        this.fail("speed=" + deltaXZ + " max=" + threshold + " diff=" + difference + " ticks=" + iceTicks + " deltaY=" + deltaY + " vehicleY=" + vehicleDeltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
