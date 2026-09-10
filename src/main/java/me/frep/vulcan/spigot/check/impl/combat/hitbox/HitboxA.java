package me.frep.vulcan.spigot.check.impl.combat.hitbox;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

@CheckInfo(name = "Hitbox", type = 'A', complexType = "History", description = "Attacked while not looking at target.")
public class HitboxA extends AbstractCheck {
    private Player lastTarget;

    public HitboxA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isInteractEntityPacket(event)) {
            Entity target = this.playerData.getReachEntityHelper().getTargetEntity();
            WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
            if (!(target instanceof Player)
                    || packet == null
                    || packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                this.lastTarget = null;
                return;
            }
            this.lastTarget = (Player) target;
            return;
        }
        if (!this.isFlyingPacket(event) || this.lastTarget == null) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.VEHICLE)) {
            this.lastTarget = null;
            return;
        }
        Vector origin = new Vector(
                this.playerData.getBlockBreakTracker().getLocationX(),
                0.0,
                this.playerData.getBlockBreakTracker().getLocationZ());
        Vector target = new Vector(
                this.lastTarget.getLocation().getX(),
                0.0,
                this.lastTarget.getLocation().getZ());
        Vector toTarget = target.clone().subtract(origin);
        if (toTarget.lengthSquared() < 1.0E-6) {
            this.lastTarget = null;
            return;
        }
        float yaw = this.playerData.getActionTracker().getYaw();
        Vector look = new Vector(-Math.sin(Math.toRadians(yaw)), 0.0, Math.cos(Math.toRadians(yaw)));
        double angle = toTarget.normalize().angle(look);
        this.lastTarget = null;
        if (angle < 1.4) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("angle=" + angle);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
