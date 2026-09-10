package me.frep.vulcan.spigot.check.impl.movement.boatfly;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ServerCompatibility;
import me.frep.vulcan.spigot.VulcanBootstrap;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

@CheckInfo(name = "Boat Fly", type = 'B', complexType = "Vertical", description = "Moving too quickly horizontally.")
public class BoatFlyB extends AbstractCheck {
    public BoatFlyB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event) && !this.isVehicleMovePacket(event)) {
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
        if (ServerCompatibility.isModernProtocol() && !boat.hasGravity()) {
            return;
        }
        if (VulcanBootstrap.INSTANCE.getEntityIdMap().containsKey(Integer.valueOf(boat.getEntityId()))) {
            return;
        }
        if (this.isExempt(ExemptType.WATERLOGGED)) {
            return;
        }
        double vehicleDeltaXZ = this.playerData.getBlockBreakTracker().getVehicleDeltaXZ();
        if (vehicleDeltaXZ <= 0.25) {
            return;
        }
        this.fail("deltaXZ=" + vehicleDeltaXZ);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
