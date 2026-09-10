package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'B', complexType = "Interact", experimental = false, description = "Invalid interact.")
public class ScaffoldB extends AbstractCheck {
    private long lastPlaceTime;

    public ScaffoldB(PlayerData playerData) {
        super(playerData);
        this.lastPlaceTime = 0L;
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
        if (placeEvent.isCancelled() || this.isExempt(ExemptType.CANCELLED_PLACE)) {
            this.lastPlaceTime = System.currentTimeMillis();
            return;
        }
        long delay = System.currentTimeMillis() - this.lastPlaceTime;
        this.lastPlaceTime = System.currentTimeMillis();
        BlockFace face = placeEvent.getBlockAgainst().getFace(placeEvent.getBlock());
        if (face != BlockFace.SELF && delay >= 300L) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("delay=" + delay + " face=" + String.valueOf(face).toLowerCase());
        this.applyBufferDecay();
    }
}
