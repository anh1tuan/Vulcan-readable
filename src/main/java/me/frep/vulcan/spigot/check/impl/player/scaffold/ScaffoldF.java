package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'F', complexType = "Packet", description = "Invalid rotations")
public class ScaffoldF extends AbstractCheck {
    private long lastPlaceMillis;
    private double lastDeltaXZ;

    public ScaffoldF(PlayerData playerData) {
        super(playerData);
        this.lastPlaceMillis = 0L;
        this.lastDeltaXZ = 0.0;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        long delay = System.currentTimeMillis() - this.lastPlaceMillis;
        this.lastDeltaXZ = deltaXZ;
        if (delay > 400L) {
            return;
        }
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        if (Math.abs(pitchDelta) < 1.0f && deltaXZ < 0.2) {
            return;
        }
        this.fail("delay=" + delay + " delta=" + deltaXZ + " sd=" + this.lastDeltaXZ);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (event instanceof BlockPlaceEvent) {
            this.lastPlaceMillis = System.currentTimeMillis();
        }
    }
}
