package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'Y', complexType = "Invalid Position", description = "NaN X/Y/Z values.")
public class BadPacketsY extends AbstractCheck {
    public BadPacketsY(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        double x = this.playerData.getBlockBreakTracker().getLocationX();
        double y = this.playerData.getBlockBreakTracker().getLocationY();
        double z = this.playerData.getBlockBreakTracker().getLocationZ();
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)
            || Double.isInfinite(x) || Double.isInfinite(y) || Double.isInfinite(z)) {
            this.fail("x=" + x + " y=" + y + " z=" + z);
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
