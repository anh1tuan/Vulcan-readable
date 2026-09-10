package me.frep.vulcan.spigot.check.impl.movement.entityflight;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.event.Event;

@CheckInfo(name = "Entity Flight", type = 'B', complexType = "Hover", description = "Hovering while riding an entity.")
public class EntityFlightB extends AbstractCheck {
    public EntityFlightB(PlayerData playerData) {
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
        if (this.playerData.getPlayer().isDead()) {
            return;
        }
        Entity vehicle = this.playerData.getPlayer().getVehicle();
        double vehicleDeltaY = this.playerData.getBlockBreakTracker().getVehicleDeltaY();
        int hoverTicks = this.playerData.getBlockBreakTracker().getVehicleMoveTicks();
        if (vehicle instanceof LivingEntity && ((LivingEntity) vehicle).isLeashed()) {
            return;
        }
        if (Math.abs(vehicleDeltaY) > 0.005) {
            return;
        }
        if (hoverTicks < 8) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        if (vehicle instanceof Horse || vehicle instanceof SkeletonHorse || vehicle instanceof ZombieHorse) {
            this.fail("[Horse] deltaY=" + vehicleDeltaY + "ticks=" + hoverTicks + "vel=" + hoverTicks);
        } else if (vehicle instanceof Pig) {
            this.fail("deltaY=" + vehicleDeltaY + "acceleration=" + vehicleDeltaY + "delay=" + hoverTicks);
        } else {
            this.fail("deltaY=" + vehicleDeltaY + "ticks=" + hoverTicks);
        }
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
