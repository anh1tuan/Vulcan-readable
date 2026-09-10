package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'F', complexType = "Steer Vehicle", description = "Invalid Steer Vehicle packet.")
public class BadPacketsF extends AbstractCheck {
    public BadPacketsF(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isSteerVehiclePacket(event)) {
            return;
        }
        WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle((PacketReceiveEvent) event);
        float forward = packet.getForward();
        float sideways = packet.getSideways();
        if (Math.abs(forward) > 0.98f || Math.abs(sideways) > 0.98f) {
            this.fail("forward=" + forward + " side=" + sideways);
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
