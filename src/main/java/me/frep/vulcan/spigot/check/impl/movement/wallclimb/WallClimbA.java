package me.frep.vulcan.spigot.check.impl.movement.wallclimb;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Wall Climb", type = 'A', complexType = "Spider", description = "Climbing up a wall.")
public class WallClimbA extends AbstractCheck {
    private int climbTicks;

    public WallClimbA(PlayerData playerData) {
        super(playerData);
        this.climbTicks = 0;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isPositionPacket(event)) {
            return;
        }
        if (this.isExempt(
                ExemptType.CLIMBABLE, ExemptType.PLACED_CLIMBABLE, ExemptType.LIQUID,
                ExemptType.VELOCITY, ExemptType.TELEPORT, ExemptType.FLIGHT, ExemptType.GLIDING, ExemptType.ELYTRA,
                ExemptType.VEHICLE, ExemptType.BOAT, ExemptType.SLIME, ExemptType.HONEY, ExemptType.SCAFFOLDING)) {
            this.climbTicks = 0;
            return;
        }
        double offsetX = Math.abs(this.playerData.getBlockBreakTracker().getLocationX() % 1.0);
        double offsetZ = Math.abs(this.playerData.getBlockBreakTracker().getLocationZ() % 1.0);
        boolean touchingWall = offsetX < 0.05 || offsetX > 0.95 || offsetZ < 0.05 || offsetZ > 0.95
                || offsetX > 0.67 || offsetZ > 0.67;
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        boolean spiderMotion = Math.abs(deltaY - 0.20000004768371582) < 0.001;
        if (!touchingWall || !spiderMotion || this.playerData.getBlockBreakTracker().isClientOnGround()) {
            this.climbTicks = 0;
            return;
        }
        ++this.climbTicks;
        if (this.climbTicks < 3) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaY=" + deltaY + " ticks=" + this.climbTicks);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
