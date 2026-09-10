package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'J', complexType = "Held Item Slot", description = "Sent HeldItemSlot while placing.")
public class BadPacketsJ extends AbstractCheck {
    public BadPacketsJ(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isHeldItemChangePacket(event)) {
            return;
        }
        if (this.isExempt(ExemptType.CLIENT_1_9_OR_NEWER, ExemptType.MODERN_SERVER, ExemptType.CREATIVE, ExemptType.SPECTATOR)) {
            return;
        }
        if (this.playerData.getStatusTracker().getTicksSinceBlockPlace() < 1) {
            this.fail("placeTicks=" + this.playerData.getStatusTracker().getTicksSinceBlockPlace());
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
