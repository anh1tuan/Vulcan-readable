package me.frep.vulcan.spigot.check.impl.movement.fastclimb;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Fast Climb", type = 'A', complexType = "Limit", description = "Climbing too quickly")
public class FastClimbA extends AbstractCheck {
    public FastClimbA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (!this.playerData.getBlockBreakTracker().isClimbableExempt()
                && !this.isExempt(ExemptType.CLIMBABLE, ExemptType.PLACED_CLIMBABLE)) {
            return;
        }
        if (this.playerData.getStatusTracker().getGravity() != 0.08) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (deltaY <= 0.118) {
            return;
        }
        this.fail("deltaY=" + deltaY + " ticks=" + this.playerData.getBlockBreakTracker().getAirTicks());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
