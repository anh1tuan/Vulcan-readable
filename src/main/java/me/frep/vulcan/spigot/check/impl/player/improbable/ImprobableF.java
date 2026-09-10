package me.frep.vulcan.spigot.check.impl.player.improbable;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Improbable", type = 'F', complexType = "Scaffold", description = "Too many combined Scaffold violations.")
public class ImprobableF extends AbstractCheck {
    private long lastFlagTime;

    public ImprobableF(PlayerData playerData) {
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
        int scaffoldViolations = this.playerData.getScaffoldViolations();
        if (scaffoldViolations > ConfigValues.checksPlayerImprobableFMaxScaffoldViolations) {
            this.fail("vl=" + scaffoldViolations);
            this.lastFlagTime = System.currentTimeMillis();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
