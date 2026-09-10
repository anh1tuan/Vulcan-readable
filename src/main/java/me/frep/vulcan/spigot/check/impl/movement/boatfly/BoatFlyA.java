package me.frep.vulcan.spigot.check.impl.movement.boatfly;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.VulcanBootstrap;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

@CheckInfo(name = "Boat Fly", type = 'A', complexType = "Vertical", description = "Moving upwards in a boat.")
public class BoatFlyA extends AbstractCheck {
    public BoatFlyA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (!(event instanceof PlayerMoveEvent)) {
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
        if (vehicleDeltaY <= 0.01) {
            return;
        }
        this.fail("deltaY=" + vehicleDeltaY);
        this.applyBufferDecay();
    }
}
