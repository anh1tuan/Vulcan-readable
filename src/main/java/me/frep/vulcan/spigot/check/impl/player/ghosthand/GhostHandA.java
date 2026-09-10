package me.frep.vulcan.spigot.check.impl.player.ghosthand;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

@CheckInfo(name = "Ghost Hand", type = 'A', complexType = "Bed", description = "Invalid bed break.")
public class GhostHandA extends AbstractCheck {
    public GhostHandA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (!(event instanceof BlockBreakEvent)) {
            return;
        }
        BlockBreakEvent breakEvent = (BlockBreakEvent) event;
        Block block = breakEvent.getBlock();
        if (block == null) {
            return;
        }
        Material type = block.getType();
        if (type == null || !type.toString().contains("BED")) {
            return;
        }
        if (this.playerData.getPlayer() == null) {
            return;
        }
        if (this.playerData.getPlayer().getTargetBlock(null, 6).equals(block)) {
            return;
        }
        this.fail("block=" + type);
        this.applyBufferDecay();
    }
}
