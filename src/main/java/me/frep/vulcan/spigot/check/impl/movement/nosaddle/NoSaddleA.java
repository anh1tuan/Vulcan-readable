package me.frep.vulcan.spigot.check.impl.movement.nosaddle;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.event.Event;

@CheckInfo(name = "No Saddle", type = 'A', complexType = "Spoof", description = "Controlling an entity without a saddle.")
public class NoSaddleA extends AbstractCheck {
    public NoSaddleA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isVehicleMovePacket(event) && !this.isSteerVehiclePacket(event)) {
            return;
        }
        if (this.playerData.getPlayer() == null || !(this.playerData.getPlayer().getVehicle() instanceof Horse)) {
            return;
        }
        Horse horse = (Horse) this.playerData.getPlayer().getVehicle();
        if (horse.isLeashed()) {
            return;
        }
        double speed = this.playerData.getBlockBreakTracker().getVehicleDeltaXZ();
        double expected = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 2.5 + 0.1;
        if (horse.getInventory().getSaddle() != null) {
            return;
        }
        if (expected - speed <= 0.3) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("speed=" + speed + "expected=" + expected);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
