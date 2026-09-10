package me.frep.vulcan.spigot.check.impl.combat.reach;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Reach", type = 'A', complexType = "History", description = "Hit from too far away.")
public class ReachA extends AbstractCheck {
    private boolean awaitingFlying;

    public ReachA(PlayerData playerData) {
        super(playerData);
        this.awaitingFlying = false;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isInteractEntityPacket(event)) {
            Entity target = this.playerData.getReachEntityHelper().getTargetEntity();
            WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
            this.awaitingFlying = target instanceof Player
                    && packet != null
                    && packet.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK;
            return;
        }
        if (!this.isFlyingPacket(event) || !this.awaitingFlying) {
            return;
        }
        this.awaitingFlying = false;
        double distance = this.playerData.getReachEntityHelper().getClosestReachDistance();
        if (distance < 0.0) {
            this.applyBufferDecay();
            return;
        }
        double maxReach = this.playerData.getStatusTracker().getEntityInteractionRange()
                + (ConfigValues.checksCombatReachAMaxReach - 3.0);
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.CREATIVE, ExemptType.VEHICLE, ExemptType.LIQUID)) {
            return;
        }
        if (distance <= maxReach) {
            this.applyBufferDecay();
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("ticks=" + this.playerData.getReachEntityHelper().getTicksSinceAttack());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
