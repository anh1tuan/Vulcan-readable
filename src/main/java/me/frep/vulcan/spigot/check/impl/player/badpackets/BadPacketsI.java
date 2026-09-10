package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'I', complexType = "Entity Action", description = "Sent EntityAction while attacking.")
public class BadPacketsI extends AbstractCheck {
    public BadPacketsI(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isEntityActionPacket(event)) {
            return;
        }
        if (this.isExempt(ExemptType.MODERN_SERVER, ExemptType.CLIENT_1_9_OR_NEWER, ExemptType.FAST, ExemptType.SPECTATOR, ExemptType.DEATH)) {
            return;
        }
        if (this.playerData.getReachEntityHelper().getTicksSinceAttack() < 1) {
            this.fail("ticks=" + this.playerData.getReachEntityHelper().getTicksSinceAttack());
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
