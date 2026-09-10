package me.frep.vulcan.spigot.check.impl.combat.autoblock;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Block", type = 'D', complexType = "Order", description = "Invalid attack order.")
public class AutoBlockD extends AbstractCheck {
    private boolean interactedThisTick;
    private boolean attackedThisTick;

    public AutoBlockD(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isFlyingPacket(event)) {
            this.interactedThisTick = false;
            this.attackedThisTick = false;
            return;
        }
        if (!this.isInteractEntityPacket(event)) {
            return;
        }
        WrapperPlayClientInteractEntity interact = this.playerData.getLastInteractPacket();
        if (interact == null) {
            return;
        }
        WrapperPlayClientInteractEntity.InteractAction action = interact.getAction();
        if (action == WrapperPlayClientInteractEntity.InteractAction.INTERACT
                || action == WrapperPlayClientInteractEntity.InteractAction.INTERACT_AT) {
            this.interactedThisTick = true;
            return;
        }
        if (action != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }
        this.attackedThisTick = true;
        if (this.isExempt(ExemptType.CLIENT_1_9_OR_NEWER)) {
            return;
        }
        if (!this.interactedThisTick) {
            return;
        }
        this.fail("order attack-after-interact interacted=" + this.interactedThisTick + " attacked=" + this.attackedThisTick);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
