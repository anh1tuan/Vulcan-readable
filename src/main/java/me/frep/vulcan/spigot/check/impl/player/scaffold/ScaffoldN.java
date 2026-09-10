package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import java.util.ArrayList;
import java.util.List;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import me.frep.vulcan.spigot.TrigUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

@CheckInfo(name = "Scaffold", type = 'N', complexType = "Expand", description = "Invalid block face.", experimental = true)
public class ScaffoldN extends AbstractCheck {
    private final List<Block> recentPlacedBlocks = new ArrayList<Block>();

    public ScaffoldN(PlayerData playerData) {
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
        Block placed = placeEvent.getBlock();
        if (placed == null) {
            return;
        }
        Material type = placed.getType();
        if (type != null && type.toString().contains("LILY")) {
            return;
        }
        Block against = placeEvent.getBlockAgainst();
        boolean againstRecent = against != null && this.recentPlacedBlocks.contains(against);
        this.recentPlacedBlocks.clear();
        this.recentPlacedBlocks.add(placed);
        if (!againstRecent) {
            return;
        }
        if (placed.getY() >= this.playerData.getBlockBreakTracker().getLocationY()) {
            return;
        }
        BlockFace face = against.getFace(placed);
        if (face == null || face == BlockFace.DOWN || face == BlockFace.SELF) {
            return;
        }
        double offsetX = Math.abs(this.playerData.getBlockBreakTracker().getLocationX() - (double) placed.getX());
        double offsetZ = Math.abs(this.playerData.getBlockBreakTracker().getLocationZ() - (double) placed.getZ());
        double distance = TrigUtil.hypot2(offsetX, offsetZ);
        if (this.isExempt(ExemptType.FLIGHT, ExemptType.CREATIVE)) {
            return;
        }
        BlockFace expected = this.faceFromYaw(this.playerData.getActionTracker().getYaw());
        if (face == expected && distance < 1.5) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
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

    public Vector lookVector(float yaw, float pitch) {
        Vector vector = new Vector();
        double pitchRad = Math.toRadians(pitch);
        double yawRad = Math.toRadians(yaw);
        double cosPitch = Math.cos(pitchRad);
        vector.setY(-Math.sin(pitchRad));
        vector.setX(-cosPitch * Math.sin(yawRad));
        vector.setZ(cosPitch * Math.cos(yawRad));
        return vector;
    }
}
