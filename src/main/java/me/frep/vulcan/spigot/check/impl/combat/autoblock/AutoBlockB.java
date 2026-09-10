package me.frep.vulcan.spigot.check.impl.combat.autoblock;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Block", type = 'B', complexType = "Packet", description = "Attacked while sending BlockDig packet.")
public class AutoBlockB extends AbstractCheck {
    public AutoBlockB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isInteractEntityPacket(event)) {
            return;
        }
        WrapperPlayClientInteractEntity interact = this.playerData.getLastInteractPacket();
        if (interact == null || interact.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }
        if (this.isExempt(ExemptType.CLIENT_1_9_OR_NEWER)) {
            return;
        }
        WrapperPlayClientPlayerDigging dig = this.playerData.getLastDigPacket();
        if (dig == null) {
            return;
        }
        DiggingAction action = dig.getAction();
        if (action != DiggingAction.START_DIGGING && action != DiggingAction.CANCELLED_DIGGING && action != DiggingAction.FINISHED_DIGGING) {
            return;
        }
        this.fail("attack-while-dig action=" + action);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
