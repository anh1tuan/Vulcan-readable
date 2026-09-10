package me.frep.vulcan.spigot.check.impl.movement.entityflight;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Pig;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.event.Event;

@CheckInfo(name = "Entity Flight", type = 'A', complexType = "Ascension", description = "Ascending while riding an entity.")
public class EntityFlightA extends AbstractCheck {
    public EntityFlightA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isVehicleMovePacket(event) && !this.isSteerVehiclePacket(event)) {
            return;
        }
        if (this.playerData.getPlayer() == null || this.playerData.getPlayer().getVehicle() == null) {
            return;
        }
        Entity vehicle = this.playerData.getPlayer().getVehicle();
        if (!(vehicle instanceof Horse)
                && !(vehicle instanceof Pig)
                && !(vehicle instanceof Donkey)
                && !(vehicle instanceof SkeletonHorse)
                && !(vehicle instanceof ZombieHorse)
                && !(vehicle instanceof Camel)) {
            return;
        }
        double vehicleDeltaY = this.playerData.getBlockBreakTracker().getVehicleDeltaY();
        if (vehicleDeltaY <= 0.0) {
            return;
        }
        if (this.playerData.getPlayer().isDead()) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deltaY=" + vehicleDeltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
