package me.frep.vulcan.spigot.check.impl.player.improbable;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Improbable", type = 'E', complexType = "Total", description = "Too many combined violations.")
public class ImprobableE extends AbstractCheck {
    private long lastFlagTime;

    public ImprobableE(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (System.currentTimeMillis() - this.lastFlagTime < 60000L) {
            return;
        }
        int totalViolations = this.playerData.getTotalViolations();
        if (totalViolations > ConfigValues.checksPlayerImprobableEMaxTotalViolations) {
            this.fail("vl=" + totalViolations);
            this.lastFlagTime = System.currentTimeMillis();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
