package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'G', complexType = "Hotbar", description = "Invalid HeldItemSlot packet.")
public class BadPacketsG extends AbstractCheck {
    private int lastSlot = -1;

    public BadPacketsG(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isHeldItemChangePacket(event)) {
            return;
        }
        WrapperPlayClientHeldItemChange packet = this.playerData.getLastHeldItemChangePacket();
        if (packet == null) {
            return;
        }
        int slot = packet.getSlot();
        if (slot == this.lastSlot) {
            this.fail("slot=" + slot + " lastSlot=" + this.lastSlot);
        }
        this.lastSlot = slot;
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
