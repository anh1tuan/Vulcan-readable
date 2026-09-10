package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'C', complexType = "Sprint", description = "Sprinting while bridging.")
public class ScaffoldC extends AbstractCheck {
    public ScaffoldC(PlayerData playerData) {
        super(playerData);
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
        if (this.playerData.getPlayer() == null || !this.playerData.getPlayer().isSprinting()) {
            return;
        }
        if (placeEvent.getBlockPlaced().getY() >= this.playerData.getBlockBreakTracker().getLocationY()) {
            return;
        }
        this.fail("ticks=" + this.playerData.getBlockBreakTracker().getAirTicks()
                + " delay=" + this.playerData.getStatusTracker().getTicksSinceQueuedTeleport());
        this.applyBufferDecay();
    }
}
