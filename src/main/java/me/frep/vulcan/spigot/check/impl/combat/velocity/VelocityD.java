package me.frep.vulcan.spigot.check.impl.combat.velocity;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Velocity", type = 'D', complexType = "Horizontal", description = "Horizontal velocity modifications.", experimental = true)
public class VelocityD extends AbstractCheck {
    private static final float SPRINT_MODIFIER = 0.02f;
    private static final float GROUND_FRICTION = 0.6f;

    public VelocityD(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double velocityX = this.playerData.getTickTracker().getVelocityX();
        double velocityZ = this.playerData.getTickTracker().getVelocityZ();
        double velocityXZ = Math.hypot(velocityX, velocityZ);
        if (velocityXZ < 0.1) {
            return;
        }
        if (this.isExempt(
                ExemptType.COLLIDING_VERTICALLY, ExemptType.LIQUID, ExemptType.FROZEN, ExemptType.CHUNK,
                ExemptType.FALL_DAMAGE, ExemptType.ENDER_PEARL, ExemptType.WEB, ExemptType.VOID,
                ExemptType.SLOW_FALLING, ExemptType.PLACED_WEB, ExemptType.TRAPDOOR, ExemptType.SOUL_SAND,
                ExemptType.SLIME, ExemptType.FLIGHT, ExemptType.NETHERITE_ARMOR, ExemptType.VEHICLE,
                ExemptType.WALL, ExemptType.ELYTRA, ExemptType.SWEET_BERRIES, ExemptType.FENCE)) {
            return;
        }
        double walkSpeed = this.playerData.getStatusTracker().getWalkSpeed();
        if (walkSpeed <= 0.0) {
            walkSpeed = 0.1;
        }
        float movementSpeed = (float) (walkSpeed + this.playerData.getStatusTracker().getSpeedAmplifier() * 0.2 * walkSpeed);
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double closestPredicted = Double.MAX_VALUE;
        for (int direction = 0; direction < 8; ++direction) {
            float inputX = 0.0f;
            float inputZ = 0.0f;
            switch (direction) {
                case 0:
                    inputX = 1.0f;
                    break;
                case 1:
                    inputX = 1.0f;
                    inputZ = -1.0f;
                    break;
                case 2:
                    inputZ = -1.0f;
                    break;
                case 3:
                    inputX = -1.0f;
                    inputZ = -1.0f;
                    break;
                case 4:
                    inputX = -1.0f;
                    break;
                case 5:
                    inputX = -1.0f;
                    inputZ = 1.0f;
                    break;
                case 6:
                    inputZ = 1.0f;
                    break;
                default:
                    inputX = 1.0f;
                    inputZ = 1.0f;
                    break;
            }
            if (inputX != 0.0f && inputZ != 0.0f) {
                inputX *= 0.70710677f;
                inputZ *= 0.70710677f;
            }
            double predictedX = velocityX * GROUND_FRICTION + (double) (inputX * movementSpeed * SPRINT_MODIFIER);
            double predictedZ = velocityZ * GROUND_FRICTION + (double) (inputZ * movementSpeed * SPRINT_MODIFIER);
            double predictedXZ = Math.hypot(predictedX, predictedZ);
            double gap = Math.abs(deltaXZ - predictedXZ);
            if (gap < closestPredicted) {
                closestPredicted = gap;
            }
        }
        double noInputPredicted = velocityXZ * GROUND_FRICTION;
        double percent = noInputPredicted <= 0.0 ? 1.0 : deltaXZ / noInputPredicted;
        if (percent >= 0.21 && closestPredicted < 0.03) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("percent=" + percent * 100.0 + "% deltaXZ=" + deltaXZ + " velocityXZ=" + velocityXZ + " closest=" + closestPredicted);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
