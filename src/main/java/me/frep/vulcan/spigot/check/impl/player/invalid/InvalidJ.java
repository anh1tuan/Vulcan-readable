package me.frep.vulcan.spigot.check.impl.player.invalid;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ServerCompatibility;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Invalid", type = 'J', complexType = "Motion", description = "Moving too quickly.", experimental = true)
public class InvalidJ extends AbstractCheck {
    public InvalidJ(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().isUnmoved()) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        int iceTicks = this.playerData.getBlockBreakTracker().getTicksSinceFeetIce();
        double threshold = 0.2975;
        double walkSpeed = this.playerData.getStatusTracker().getWalkSpeed();
        if (walkSpeed > 0.0) {
            threshold *= walkSpeed / 0.1;
        }
        if (ServerCompatibility.isServer1_16OrNewer()) {
            threshold += 0.5;
        }
        if (ServerCompatibility.isServer1_9OrNewer()) {
            threshold += 0.2;
        }
        if (this.isExempt(ExemptType.SNOW, ExemptType.ICE, ExemptType.SOUL_SAND, ExemptType.VELOCITY, ExemptType.TELEPORT)) {
            return;
        }
        double difference = deltaXZ - threshold;
        if (difference <= 0.0) {
            return;
        }
        this.fail("speed=" + deltaXZ + "max=" + threshold + "diff=" + difference + "ticks=" + iceTicks + "deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
