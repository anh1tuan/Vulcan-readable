package me.frep.vulcan.spigot.check.impl.combat.killaura;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
    name = "Kill Aura",
    type = 'C',
    complexType = "Head Snap",
    experimental = false,
    description = "Invalid acceleration."
)
public class KillAuraC extends AbstractCheck {
    public KillAuraC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (this.getTicksSinceAttack() >= 3) {
            return;
        }

        float acceleration = this.playerData.getActionTracker().getYawAcceleration();
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        if (acceleration <= 0.0f) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }

        this.fail("accel=" + acceleration + " deltaYaw=" + yawDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
