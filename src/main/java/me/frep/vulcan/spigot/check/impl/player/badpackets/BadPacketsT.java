package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'T', complexType = "Keep Alive", experimental = false, description = "Invalid KeepAlive packets.")
public class BadPacketsT extends AbstractCheck {
    private long lastKeepAliveId = -1L;

    public BadPacketsT(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isKeepAlivePacket(event)) {
            return;
        }
        WrapperPlayClientKeepAlive packet = new WrapperPlayClientKeepAlive((PacketReceiveEvent) event);
        long id = packet.getId();
        if (!this.isExempt(ExemptType.JOINED) && (id == 0L || id == this.lastKeepAliveId)) {
            this.fail("id=" + id + " lastId=" + this.lastKeepAliveId);
        }
        this.lastKeepAliveId = id;
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
