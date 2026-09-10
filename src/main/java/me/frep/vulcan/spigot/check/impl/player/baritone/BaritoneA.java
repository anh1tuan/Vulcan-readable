package me.frep.vulcan.spigot.check.impl.player.baritone;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Baritone", type = 'A', complexType = "Rotations", description = "Checks for Baritone like rotations.")
public class BaritoneA extends AbstractCheck {
    private int ticksSinceCancel;

    public BaritoneA(PlayerData playerData) {
        super(playerData);
        this.ticksSinceCancel = 100;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        WrapperPlayClientPlayerDigging digging = this.playerData.getLastDigPacket();
        if (digging != null && digging.getAction() == DiggingAction.CANCELLED_DIGGING) {
            this.ticksSinceCancel = 0;
        }
        if (!this.isRotationPacket(event)) {
            return;
        }
        ++this.ticksSinceCancel;
        if (this.ticksSinceCancel > 3) {
            return;
        }
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        if (yawDelta == 0.0f && pitchDelta == 0.0f) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("yaw=" + yawDelta + " pitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
