package me.frep.vulcan.spigot.check.impl.movement.speed;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Speed", type = 'C', complexType = "Air", description = "Moving too quickly in the air.")
public class SpeedC extends AbstractCheck {
    public SpeedC(PlayerData playerData) {
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
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        if (airTicks <= 0) {
            return;
        }
        if (this.isExempt(ExemptType.SERVER_POSITION_FAST, ExemptType.FARMLAND, ExemptType.SNOW, ExemptType.CANCELLED_PLACE)) {
            return;
        }
        double walkSpeed = this.playerData.getStatusTracker().getWalkSpeed();
        if (walkSpeed <= 0.0) {
            walkSpeed = 0.1;
        }
        double threshold = 0.3625 * (walkSpeed / 0.1);
        if (airTicks == 2) {
            threshold += 0.025;
        }
        int speedAmplifier = this.playerData.getStatusTracker().getSpeedAmplifier();
        threshold += speedAmplifier * 0.2 * Math.pow(0.9150000214576721, this.playerData.getTickTracker().getPing());
        double difference = deltaXZ - threshold;
        if (difference < ConfigValues.checksMovementSpeedCMinDifference) {
            return;
        }
        this.fail("speed=" + deltaXZ + " max=" + threshold + " diff=" + difference + " ticks=" + airTicks + " deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
