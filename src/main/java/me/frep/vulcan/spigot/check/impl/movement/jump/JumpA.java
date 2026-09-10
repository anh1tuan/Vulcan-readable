package me.frep.vulcan.spigot.check.impl.movement.jump;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.MovementConstants;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Jump", type = 'A', complexType = "Motion", description = "Invalid jump motion.")
public class JumpA extends AbstractCheck {
    public JumpA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (this.isOnGround()) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().getLocationY() < 30.0) {
            return;
        }
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        if (airTicks <= 0 || airTicks >= 9) {
            return;
        }
        if (this.playerData.getStatusTracker().getGravity() != 0.08) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (ConfigValues.ghostWaterFixEnabled && deltaXZ < 0.15 && this.playerData.getBlockBreakTracker().isStandingOnScaffold()) {
            for (Double known : MovementConstants.knownJumpMotions) {
                if (Math.abs(deltaY - known.doubleValue()) < 0.01) {
                    return;
                }
            }
        }
        Double expected = MovementConstants.expectedJumpMotionByAirTick.get(Integer.valueOf(airTicks));
        if (expected == null) {
            return;
        }
        double predicted = expected.doubleValue() + this.playerData.getStatusTracker().getJumpBoostAmplifier() * 0.1;
        double difference = Math.abs(deltaY - predicted);
        if (this.jumpExempt()) {
            return;
        }
        if (!this.playerData.getStatusTracker().isAttributesReady()) {
            return;
        }
        if (deltaY <= 0.0) {
            return;
        }
        if (difference <= 0.01) {
            return;
        }
        this.fail("deltaY=" + deltaY + " ticks=" + airTicks + " difference=" + difference);
        this.applyBufferDecay();
    }

    private boolean jumpExempt() {
        if (this.isExempt(
                ExemptType.VELOCITY, ExemptType.COLLIDING_VERTICALLY, ExemptType.HOPPER, ExemptType.SLIME,
                ExemptType.SCAFFOLDING, ExemptType.BOAT, ExemptType.TRAPDOOR, ExemptType.STAIRS, ExemptType.SHULKER,
                ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.SLAB, ExemptType.ILLEGAL_BLOCK,
                ExemptType.JUMP_BOOST_RAN_OUT, ExemptType.FENCE, ExemptType.JUMP_BOOST, ExemptType.HONEY,
                ExemptType.FISHING_ROD, ExemptType.WEB, ExemptType.FULLY_STUCK, ExemptType.SHULKER_BOX, ExemptType.VEHICLE,
                ExemptType.BED, ExemptType.SEA_PICKLE, ExemptType.CHAIN, ExemptType.SLEEPING, ExemptType.CANCELLED_BREAK,
                ExemptType.ANVIL, ExemptType.PISTON, ExemptType.TURTLE_EGG, ExemptType.DOOR, ExemptType.DEATH,
                ExemptType.PARTIALLY_STUCK, ExemptType.WALL, ExemptType.PLACED_WEB, ExemptType.BUBBLE_COLUMN,
                ExemptType.FROZEN, ExemptType.KELP, ExemptType.SNOW, ExemptType.BLOCK_BREAK, ExemptType.SWEET_BERRIES,
                ExemptType.EMPTIED_BUCKET, ExemptType.CONDUIT, ExemptType.FARMLAND, ExemptType.LENIENT_SCAFFOLDING,
                ExemptType.ATTACK_DAMAGE, ExemptType.SEAGRASS, ExemptType.DRIPSTONE, ExemptType.WATERLOGGED,
                ExemptType.CHEST, ExemptType.SOUL_SAND, ExemptType.GLITCHED_BLOCKS_ABOVE, ExemptType.POWDER_SNOW,
                ExemptType.SIGN, ExemptType.PACKET_EXPLOSION)) {
            return true;
        }
        return this.playerData.getBlockBreakTracker().isJoinedExempt()
                || this.playerData.getBlockBreakTracker().isTeleportExempt()
                || this.playerData.getBlockBreakTracker().isLiquidExempt()
                || this.playerData.getBlockBreakTracker().isClimbableExempt()
                || this.playerData.getBlockBreakTracker().isGlidingExempt()
                || this.playerData.getBlockBreakTracker().isEnderPearlExempt()
                || this.playerData.getBlockBreakTracker().isLevitationExempt()
                || this.playerData.getBlockBreakTracker().isNearWall()
                || this.playerData.getBlockBreakTracker().isNearFence()
                || this.playerData.getBlockBreakTracker().isNearSlab()
                || this.playerData.getBlockBreakTracker().getPreviousDeltaY() != 0.0
                || this.playerData.getBlockBreakTracker().getLastVehicleX() != 0.0
                || this.isExempt(ExemptType.BLOCK_PLACE);
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
