package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ServerCompatibility;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'A', complexType = "Abilities", description = "Spoofed abilities packets.")
public class BadPacketsA extends AbstractCheck {
    public BadPacketsA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isAbilitiesPacket(event)) {
            return;
        }
        if (this.playerData.getPlayer() == null) {
            return;
        }
        if (this.playerData.getPlayer().getAllowFlight()) {
            return;
        }
        WrapperPlayClientPlayerAbilities packet = new WrapperPlayClientPlayerAbilities((PacketReceiveEvent) event);
        if (!packet.isFlightAllowed()) {
            return;
        }
        if (!ServerCompatibility.isServer1_9OrNewer()) {
            return;
        }
        this.fail("flightAllowed=" + packet.isFlightAllowed());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
