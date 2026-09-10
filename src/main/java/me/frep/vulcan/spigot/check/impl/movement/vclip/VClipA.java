package me.frep.vulcan.spigot.check.impl.movement.vclip;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.MovementConstants;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "VClip", type = 'A', complexType = "Clip", description = "Large vertical movement.")
public class VClipA extends AbstractCheck {
    public VClipA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        if (airTicks < 1 || airTicks >= 5) {
            return;
        }
        if (this.playerData.getStatusTracker().getGravity() > 0.1) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        Double expected = MovementConstants.expectedJumpMotionByAirTick.get(Integer.valueOf(airTicks));
        if (expected == null) {
            expected = MovementConstants.VANILLA_JUMP_MOTION;
        }
        double difference = Math.abs(deltaY - expected.doubleValue());
        if (this.playerData.getTickTracker().getFlyingTicks() != 0) {
            return;
        }
        if (this.isExempt(
                ExemptType.VELOCITY, ExemptType.JOINED, ExemptType.TELEPORT, ExemptType.LIQUID, ExemptType.CLIMBABLE,
                ExemptType.GLIDING, ExemptType.RIPTIDE, ExemptType.COLLIDING_VERTICALLY, ExemptType.HOPPER,
                ExemptType.SLIME, ExemptType.SCAFFOLDING, ExemptType.BOAT, ExemptType.TRAPDOOR, ExemptType.STAIRS,
                ExemptType.SHULKER, ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.SLAB, ExemptType.ILLEGAL_BLOCK,
                ExemptType.FENCE, ExemptType.JUMP_BOOST, ExemptType.HONEY, ExemptType.FISHING_ROD, ExemptType.WEB,
                ExemptType.FULLY_STUCK, ExemptType.SHULKER_BOX, ExemptType.ELYTRA, ExemptType.VEHICLE, ExemptType.BED,
                ExemptType.BLOCK_PLACE, ExemptType.SLEEPING, ExemptType.CANCELLED_BREAK, ExemptType.ANVIL,
                ExemptType.PISTON, ExemptType.TURTLE_EGG, ExemptType.DOOR, ExemptType.DEATH, ExemptType.PARTIALLY_STUCK,
                ExemptType.WALL, ExemptType.PLACED_WEB, ExemptType.ENDER_PEARL, ExemptType.LEVITATION,
                ExemptType.BUBBLE_COLUMN, ExemptType.FROZEN, ExemptType.BLOCK_BREAK, ExemptType.SWEET_BERRIES,
                ExemptType.EMPTIED_BUCKET, ExemptType.FARMLAND, ExemptType.LENIENT_SCAFFOLDING, ExemptType.ATTACK_DAMAGE,
                ExemptType.SEAGRASS, ExemptType.DRIPSTONE, ExemptType.WATERLOGGED, ExemptType.CHEST, ExemptType.SOUL_SAND,
                ExemptType.GLITCHED_BLOCKS_ABOVE, ExemptType.POWDER_SNOW)) {
            return;
        }
        if (difference <= 0.75) {
            return;
        }
        this.fail("deltaY=" + deltaY + "ticks=" + airTicks);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
