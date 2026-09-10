package me.frep.vulcan.spigot.check.impl.player.invalid;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Invalid", type = 'A', complexType = "Invalid", description = "Possible transaction disabler. Do not ban for this check.")
public class InvalidA extends AbstractCheck {
    private int flyingWithoutTransaction;

    public InvalidA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        Player player = this.playerData.getPlayer();
        if (player == null || player.getAllowFlight() || this.playerData.getCheckState().isKeepAliveDelayed()) {
            this.flyingWithoutTransaction = 0;
            return;
        }
        ++this.flyingWithoutTransaction;
        if (this.flyingWithoutTransaction > 100) {
            this.fail("ticks=" + this.flyingWithoutTransaction);
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
