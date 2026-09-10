package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@CheckInfo(name = "Scaffold", type = 'A', complexType = "Interact", description = "Interacted with the bottom of block.")
public class ScaffoldA extends AbstractCheck {
    public ScaffoldA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (!(event instanceof PlayerInteractEvent)) {
            return;
        }
        PlayerInteractEvent interact = (PlayerInteractEvent) event;
        if (interact.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (this.playerData.getPlayer() == null) {
            return;
        }
        Block below = this.playerData.getPlayer().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
        Block clicked = interact.getClickedBlock();
        if (clicked == null || !clicked.equals(below)) {
            return;
        }
        if (interact.getBlockFace() != BlockFace.DOWN) {
            return;
        }
        if (!clicked.getType().isSolid()) {
            return;
        }
        this.fail("block=" + clicked.getType());
        this.applyBufferDecay();
    }
}
