package me.frep.vulcan.spigot.check.impl.movement.step;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Step", type = 'A', complexType = "Vanilla", description = "Invalid Step height.")
public class StepA extends AbstractCheck {
    public StepA(PlayerData playerData) {
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
                ExemptType.SHULKER, ExemptType.ENDER_PEARL, ExemptType.SLEEPING, ExemptType.BED, ExemptType.BLOCK_BREAK)) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        double maxStep = this.playerData.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) ? 1.0 : 0.6;
        if (deltaY <= maxStep) {
            return;
        }
        this.fail("deltaY=" + deltaY + " max=" + maxStep);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
