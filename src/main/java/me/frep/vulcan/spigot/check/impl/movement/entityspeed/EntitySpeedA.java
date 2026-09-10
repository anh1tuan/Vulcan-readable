package me.frep.vulcan.spigot.check.impl.movement.entityspeed;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.event.Event;

@CheckInfo(name = "Entity Speed", type = 'A', complexType = "Limit", description = "Riding an entity too quickly.")
public class EntitySpeedA extends AbstractCheck {
    public EntitySpeedA(PlayerData playerData) {
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
        if (vehicle instanceof Boat) {
            return;
        }
        double speed = this.playerData.getBlockBreakTracker().getVehicleDeltaXZ();
        double max = 0.5;
        if (vehicle instanceof Horse) {
            Horse horse = (Horse) vehicle;
            if (horse.hasMetadata("NPC")) {
                return;
            }
            max = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 2.25 + 0.1;
        } else if (vehicle instanceof Pig && vehicle instanceof LivingEntity) {
            max = 0.4;
        }
        if (this.playerData.getStatusTracker().getServerPositionLookTicks() < 20) {
            max += 0.5;
        }
        if (speed <= max) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("speed=" + speed + "max=" + max);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
