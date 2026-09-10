package me.frep.vulcan.spigot.check.impl.movement.boatfly;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.VulcanBootstrap;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

@CheckInfo(name = "Boat Fly", type = 'C', complexType = "Hover", description = "Hovering in a boat.")
public class BoatFlyC extends AbstractCheck {
    public BoatFlyC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isVehicleMovePacket(event) && !this.isFlyingPacket(event)) {
            return;
        }
        if (this.playerData.getPlayer() == null) {
            return;
        }
        Entity vehicle = this.playerData.getPlayer().getVehicle();
        if (!(vehicle instanceof Boat)) {
            return;
        }
        Boat boat = (Boat) vehicle;
        if (VulcanBootstrap.INSTANCE.getEntityIdMap().containsKey(Integer.valueOf(boat.getEntityId()))) {
            return;
        }
        double vehicleDeltaY = this.playerData.getBlockBreakTracker().getVehicleDeltaY();
        if (vehicleDeltaY < -0.05 || vehicleDeltaY > 0.05) {
            return;
        }
        this.fail("accel=0 deltaY=" + vehicleDeltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
