package me.frep.vulcan.spigot.check.impl.movement.step;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Step", type = 'C', complexType = "Motion", description = "Reverse step.")
public class StepC extends AbstractCheck {
    public StepC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (!this.playerData.getBlockBreakTracker().isClientOnGround()) {
            return;
        }
        if (this.isExempt(
                ExemptType.JOINED, ExemptType.TELEPORT, ExemptType.SHULKER_BOX, ExemptType.SPECTATOR,
                ExemptType.WORLD_CHANGE, ExemptType.RIPTIDE, ExemptType.BOAT, ExemptType.VEHICLE, ExemptType.PISTON,
                ExemptType.SHULKER, ExemptType.ENDER_PEARL, ExemptType.SLEEPING, ExemptType.BED, ExemptType.BLOCK_BREAK,
                ExemptType.SLIME, ExemptType.VELOCITY)) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (deltaY >= -0.6) {
            return;
        }
        this.fail("deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
