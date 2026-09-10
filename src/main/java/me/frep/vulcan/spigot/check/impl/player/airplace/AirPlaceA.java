package me.frep.vulcan.spigot.check.impl.player.airplace;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Air Place", type = 'A', complexType = "Invalid", description = "Invalid block placement.")
public class AirPlaceA extends AbstractCheck {
    public AirPlaceA(PlayerData playerData) {
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
        Block against = placeEvent.getBlockAgainst();
        if (against == null) {
            return;
        }
        Material againstType = against.getType();
        if (againstType.isSolid()) {
            return;
        }
        this.fail("block=" + placeEvent.getBlock().getType() + " against=" + againstType);
        this.applyBufferDecay();
    }
}
