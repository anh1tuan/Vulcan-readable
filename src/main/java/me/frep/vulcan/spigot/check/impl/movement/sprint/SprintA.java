package me.frep.vulcan.spigot.check.impl.movement.sprint;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Sprint", type = 'A', complexType = "Angle", description = "Invalid sprint direction.")
public class SprintA extends AbstractCheck {
    public SprintA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        Player player = this.playerData.getPlayer();
        if (player == null || !player.isSprinting()) {
            return;
        }
        if (this.isExempt(
                ExemptType.RIPTIDE, ExemptType.TELEPORT, ExemptType.WORLD_CHANGE, ExemptType.JOINED,
                ExemptType.SERVER_POSITION, ExemptType.SWIMMING, ExemptType.LIQUID, ExemptType.ICE)) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (deltaXZ <= 0.25) {
            return;
        }
        float yaw = this.playerData.getActionTracker().getYaw();
        this.fail("deltaXZ=" + deltaXZ + " yaw=" + yaw);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
