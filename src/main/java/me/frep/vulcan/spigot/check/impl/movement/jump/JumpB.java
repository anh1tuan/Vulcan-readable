package me.frep.vulcan.spigot.check.impl.movement.jump;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Jump", type = 'B', complexType = "Height", description = "Invalid jump motion.")
public class JumpB extends AbstractCheck {
    public JumpB(PlayerData playerData) {
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
        if (this.isExempt(
                ExemptType.JOINED, ExemptType.TELEPORT, ExemptType.RIPTIDE, ExemptType.VEHICLE, ExemptType.HONEY,
                ExemptType.GLIDING, ExemptType.LEVITATION, ExemptType.SLAB, ExemptType.FLIGHT, ExemptType.JUMP_BOOST_RAN_OUT,
                ExemptType.BOAT, ExemptType.BUBBLE_COLUMN, ExemptType.WALL, ExemptType.CREATIVE, ExemptType.SHULKER_BOX,
                ExemptType.ANVIL, ExemptType.SLIME, ExemptType.PISTON, ExemptType.TURTLE_EGG, ExemptType.DOOR,
                ExemptType.DEATH, ExemptType.ENDER_PEARL, ExemptType.PLACED_SLIME, ExemptType.CHORUS_FRUIT,
                ExemptType.MYTHIC_MOB, ExemptType.BLOCK_BREAK, ExemptType.FISHING_ROD, ExemptType.SLEEPING,
                ExemptType.SHULKER, ExemptType.SEA_PICKLE, ExemptType.SKULL)) {
            return;
        }
        if (this.playerData.getStatusTracker().getGravity() <= 0.0) {
            return;
        }
        if (this.playerData.getStatusTracker().getTicksSinceGravityChange() < 20) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        int airTicks = this.playerData.getBlockBreakTracker().getAirTicks();
        double limit = this.playerData.getStatusTracker().getJumpStrength();
        if (this.playerData.getStatusTracker().getTicksSinceDig() < 20) {
            limit += 0.15;
        }
        int jumpBoost = this.playerData.getStatusTracker().getJumpBoostAmplifier();
        if (jumpBoost > 0) {
            limit += jumpBoost * 0.125;
        }
        if (airTicks < 5) {
            limit += 0.125;
        }
        if (this.playerData.getStatusTracker().getTicksSinceJumpStrengthChange() < 20) {
            limit += Math.max(
                    this.playerData.getStatusTracker().getJumpStrength(),
                    this.playerData.getStatusTracker().getLastJumpStrength());
        }
        if (deltaY - limit - 1.0 <= 0.0) {
            return;
        }
        this.fail("deltaY=" + deltaY + " limit=" + limit + " ticks=" + airTicks
                + " tp=" + this.playerData.getStatusTracker().getTicksSinceQueuedTeleport()
                + " vY=" + this.playerData.getTickTracker().getLastVelocityY());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
