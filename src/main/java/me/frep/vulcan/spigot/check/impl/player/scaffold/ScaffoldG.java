package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'G', complexType = "Speed", experimental = false, description = "Bridging too quickly.")
public class ScaffoldG extends AbstractCheck {
    private long lastPlaceMillis;

    public ScaffoldG(PlayerData playerData) {
        super(playerData);
        this.lastPlaceMillis = 0L;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (!(event instanceof BlockPlaceEvent)) {
            return;
        }
        BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
        if (placeEvent.isCancelled()) {
            return;
        }
        long delay = System.currentTimeMillis() - this.lastPlaceMillis;
        this.lastPlaceMillis = System.currentTimeMillis();
        double acceleration = this.playerData.getBlockBreakTracker().getDeltaXZ();
        float yawDelta = this.playerData.getActionTracker().getYawDelta();
        if (delay >= 100L) {
            return;
        }
        this.fail("angle=" + yawDelta + " delay=" + delay + " acceleration=" + acceleration);
        this.applyBufferDecay();
    }
}
