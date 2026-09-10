package me.frep.vulcan.spigot.check.impl.player.invalid;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Invalid", type = 'B', complexType = "Invalid", description = "Spamming ghost block setbacks. Do not ban for this check.")
public class InvalidB extends AbstractCheck {
    public InvalidB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        int ghostBlockAirTicks = this.playerData.getBlockBreakTracker().getGhostBlockAirTicks();
        if (ghostBlockAirTicks > 5) {
            this.fail("ticks=" + ghostBlockAirTicks);
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
