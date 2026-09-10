package me.frep.vulcan.spigot.check.impl.player.improbable;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Improbable", type = 'B', complexType = "Movement", experimental = false, description = "Too many combined movement violations.")
public class ImprobableB extends AbstractCheck {
    private long lastFlagTime;

    public ImprobableB(PlayerData playerData) {
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
        int movementViolations = this.playerData.getMovementViolations();
        if (movementViolations > ConfigValues.checksPlayerImprobableBMaxMovementViolations) {
            this.fail("vl=" + movementViolations);
            this.lastFlagTime = System.currentTimeMillis();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
