package me.frep.vulcan.spigot.check.impl.player.baritone;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Baritone", type = 'B', complexType = "Large Rotation", description = "Checks for Baritone like rotations.")
public class BaritoneB extends AbstractCheck {
    public BaritoneB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isRotationPacket(event)) {
            return;
        }
        WrapperPlayClientPlayerDigging digging = this.playerData.getLastDigPacket();
        if (digging == null || digging.getAction() != DiggingAction.CANCELLED_DIGGING) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED)) {
            return;
        }
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        if (yawDelta < 20.0f && pitchDelta < 20.0f) {
            return;
        }
        this.fail("yaw=" + yawDelta + " pitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
