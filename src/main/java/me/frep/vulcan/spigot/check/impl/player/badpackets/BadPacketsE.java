package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'E', complexType = "Interact", description = "Interacted with themselves.")
public class BadPacketsE extends AbstractCheck {
    public BadPacketsE(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isInteractEntityPacket(event)) {
            return;
        }
        if (this.isExempt(ExemptType.SPECTATOR)) {
            return;
        }
        WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
        if (packet == null) {
            return;
        }
        if (packet.getEntityId() == this.playerData.getEntityId()) {
            this.fail("entityId=" + packet.getEntityId());
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
