package me.frep.vulcan.spigot.check.impl.combat.velocity;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Velocity", type = 'B', complexType = "Horizontal", description = "Horizontal velocity modifications.")
public class VelocityB extends AbstractCheck {
    public VelocityB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (!this.playerData.getBlockBreakTracker().isClientOnGround()) {
            return;
        }
        double velocityX = this.playerData.getTickTracker().getVelocityX();
        double velocityZ = this.playerData.getTickTracker().getVelocityZ();
        double velocityXZ = Math.hypot(velocityX, velocityZ);
        if (velocityXZ < 0.01) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        double percent = deltaXZ / velocityXZ;
        if (this.isExempt(
                ExemptType.COLLIDING_VERTICALLY, ExemptType.LIQUID, ExemptType.FROZEN, ExemptType.CHUNK,
                ExemptType.FALL_DAMAGE, ExemptType.ENDER_PEARL, ExemptType.WEB, ExemptType.VOID,
                ExemptType.SLOW_FALLING, ExemptType.PLACED_WEB, ExemptType.TRAPDOOR, ExemptType.SOUL_SAND,
                ExemptType.SLIME, ExemptType.COMBO_MODE, ExemptType.FLIGHT, ExemptType.NETHERITE_ARMOR,
                ExemptType.VEHICLE, ExemptType.WALL, ExemptType.MYTHIC_MOB, ExemptType.ELYTRA,
                ExemptType.SWEET_BERRIES, ExemptType.FENCE)) {
            return;
        }
        if (percent >= 0.21) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("percent=" + percent * 100.0 + "% deltaXZ=" + deltaXZ + " velocityXZ=" + velocityXZ);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
