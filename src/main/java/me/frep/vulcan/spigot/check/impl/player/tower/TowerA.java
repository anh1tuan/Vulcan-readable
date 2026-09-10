package me.frep.vulcan.spigot.check.impl.player.tower;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Tower", type = 'A', complexType = "Limit", experimental = false, description = "Towering too quickly.")
public class TowerA extends AbstractCheck {
    private long lastPlaceTime;

    public TowerA(PlayerData playerData) {
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
        if (this.playerData.getPlayer() == null) {
            return;
        }
        BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
        if (placeEvent.isCancelled()) {
            return;
        }
        if (!placeEvent.getBlock().getType().isSolid()) {
            return;
        }
        long delay = System.currentTimeMillis() - this.lastPlaceTime;
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        boolean sameX = this.playerData.getPlayer().getLocation().getBlockX() == placeEvent.getBlock().getLocation().getBlockX();
        boolean sameZ = this.playerData.getPlayer().getLocation().getBlockZ() == placeEvent.getBlock().getLocation().getBlockZ();
        BlockFace face = placeEvent.getBlockAgainst().getFace(placeEvent.getBlock());
        this.lastPlaceTime = System.currentTimeMillis();
        if (face != BlockFace.UP || !sameX || !sameZ) {
            return;
        }
        if (delay >= 250L && deltaY <= 0.42) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("delay=" + delay + "deltaY=" + deltaY);
        this.applyBufferDecay();
    }
}
