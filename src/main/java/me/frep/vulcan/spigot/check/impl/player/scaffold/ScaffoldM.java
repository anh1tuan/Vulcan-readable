package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import me.frep.vulcan.spigot.TrigUtil;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'M', complexType = "Expand", description = "Invalid block face.")
public class ScaffoldM extends AbstractCheck {
    public ScaffoldM(PlayerData playerData) {
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
        if (placeEvent.getBlockPlaced().getY() >= this.playerData.getBlockBreakTracker().getLocationY()) {
            return;
        }
        double offsetX = Math.abs(this.playerData.getBlockBreakTracker().getLocationX() - (double) placeEvent.getBlockPlaced().getX());
        double offsetZ = Math.abs(this.playerData.getBlockBreakTracker().getLocationZ() - (double) placeEvent.getBlockPlaced().getZ());
        double distance = TrigUtil.hypot2(offsetX, offsetZ);
        BlockFace face = placeEvent.getBlockAgainst().getFace(placeEvent.getBlock());
        BlockFace expected = this.faceFromYaw(this.playerData.getActionTracker().getYaw());
        if (face == expected && distance < 6.0) {
            return;
        }
        this.fail("face=" + face + " expected=" + expected + " dist=" + distance);
        this.applyBufferDecay();
    }

    private BlockFace faceFromYaw(float yaw) {
        float wrapped = (yaw - 90.0f) % 360.0f;
        if (wrapped < 0.0f) {
            wrapped += 360.0f;
        }
        if (wrapped < 45.0f) {
            return BlockFace.WEST;
        }
        if (wrapped < 135.0f) {
            return BlockFace.NORTH;
        }
        if (wrapped < 225.0f) {
            return BlockFace.EAST;
        }
        if (wrapped < 315.0f) {
            return BlockFace.SOUTH;
        }
        return BlockFace.WEST;
    }
}
