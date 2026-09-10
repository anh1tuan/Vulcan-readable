package me.frep.vulcan.spigot.check.impl.combat.reach;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.CollisionUtil;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.VulcanBootstrap;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

@CheckInfo(name = "Reach", type = 'B', complexType = "Simple", description = "Hit from too far away.")
public class ReachB extends AbstractCheck {
    private Player lastTarget;

    public ReachB(PlayerData playerData) {
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
        PlayerData targetData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(this.lastTarget);
        if (targetData == null || targetData.getPlayer() == null) {
            this.lastTarget = null;
            return;
        }
        Vector attacker = new Vector(
                this.playerData.getBlockBreakTracker().getLocationX(),
                0.0,
                this.playerData.getBlockBreakTracker().getLocationZ());
        Vector victim = new Vector(
                targetData.getBlockBreakTracker().getLocationX(),
                0.0,
                targetData.getBlockBreakTracker().getLocationZ());
        double heightDiff = Math.abs(
                this.playerData.getBlockBreakTracker().getLocationY()
                        - targetData.getBlockBreakTracker().getLocationY());
        double distance = attacker.distance(victim) - 0.565;
        double maxReach = this.playerData.getStatusTracker().getEntityInteractionRange()
                + (ConfigValues.checksCombatReachBMaxReach - 3.0);
        maxReach += 0.05;
        maxReach += heightDiff * 0.9;
        maxReach += this.playerData.getBlockBreakTracker().getDeltaXZ() * 0.66;
        if (this.playerData.getBlockBreakTracker().getLastVehicleX() < 3) {
            maxReach += 0.35;
        }
        if (this.playerData.getStatusTracker().getTicksSinceQueuedTeleport() != 0) {
            maxReach += this.playerData.getStatusTracker().getEntityInteractionRange() * 0.125;
        }
        if (this.playerData.getPlayer() != null && targetData.getPlayer() != null) {
            if (CollisionUtil.collisionDouble(this.playerData.getPlayer(), targetData.getPlayer()) > 100.0) {
                maxReach += 1.25;
            }
        }
        if (this.isExempt(ExemptType.SERVER_POSITION)) {
            maxReach += 1.0;
        }
        if (this.isExempt(
                ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.PLUGIN_LOAD, ExemptType.WORLD_CHANGE,
                ExemptType.CREATIVE, ExemptType.VEHICLE)) {
            this.lastTarget = null;
            return;
        }
        this.lastTarget = null;
        if (distance <= maxReach) {
            this.applyBufferDecay();
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail(distance + " > " + maxReach);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
