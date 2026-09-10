package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'C', complexType = "Pitch", description = "Impossible pitch.")
public class BadPacketsC extends AbstractCheck {
    public BadPacketsC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        float pitch = this.playerData.getActionTracker().getPitch();
        if (Math.abs(pitch) > 90.0f) {
            this.fail("pitch=" + pitch);
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
