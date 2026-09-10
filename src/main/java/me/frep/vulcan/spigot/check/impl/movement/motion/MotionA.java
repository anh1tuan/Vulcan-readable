package me.frep.vulcan.spigot.check.impl.movement.motion;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Motion", type = 'A', complexType = "Constant", description = "Repeated vertical motions.")
public class MotionA extends AbstractCheck {
    public MotionA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (Math.abs(Math.abs(deltaY) - 0.11760000228882461) > 0.001) {
            return;
        }
        if (this.isExempt(
                ExemptType.HALF_BLOCK, ExemptType.CLIMBABLE, ExemptType.FLIGHT, ExemptType.BED, ExemptType.LIQUID,
                ExemptType.TELEPORT, ExemptType.VELOCITY, ExemptType.ILLEGAL_BLOCK, ExemptType.SLEEPING,
                ExemptType.SOUL_SAND, ExemptType.HONEY, ExemptType.SLIME, ExemptType.LEVITATION, ExemptType.SPECTATOR,
                ExemptType.SCAFFOLDING, ExemptType.FENCE, ExemptType.BUBBLE_COLUMN, ExemptType.CREATIVE,
                ExemptType.SHULKER, ExemptType.SHULKER_BOX, ExemptType.ELYTRA, ExemptType.GLIDING,
                ExemptType.CANCELLED_PLACE, ExemptType.LENIENT_SCAFFOLDING, ExemptType.PLACED_CLIMBABLE, ExemptType.KELP,
                ExemptType.BOAT, ExemptType.CHUNK, ExemptType.TRAPDOOR, ExemptType.POWDER_SNOW,
                ExemptType.EMPTIED_BUCKET, ExemptType.CAKE)) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
