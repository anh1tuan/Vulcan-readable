package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'E', complexType = "Rotations [2]", experimental = false, description = "Invalid rotations.")
public class ScaffoldE extends AbstractCheck {
    private long lastPlaceMillis;

    public ScaffoldE(PlayerData playerData) {
        super(playerData);
        this.lastPlaceMillis = 0L;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (System.currentTimeMillis() - this.lastPlaceMillis > 350L) {
            return;
        }
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        if (pitchDelta <= 8.0f) {
            return;
        }
        this.fail("deltaYaw=" + yawDelta + " deltaPitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (event instanceof BlockPlaceEvent) {
            this.lastPlaceMillis = System.currentTimeMillis();
        }
    }
}
