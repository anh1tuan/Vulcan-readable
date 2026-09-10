package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'Z', complexType = "Spectate", description = "Invalid Spectate Packets.")
public class BadPacketsZ extends AbstractCheck {
    public BadPacketsZ(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isSpectatePacket(event)) {
            return;
        }
        Player player = this.playerData.getPlayer();
        if (player == null) {
            return;
        }
        if (player.getGameMode() != GameMode.SPECTATOR) {
            this.fail("gamemode=" + player.getGameMode());
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
