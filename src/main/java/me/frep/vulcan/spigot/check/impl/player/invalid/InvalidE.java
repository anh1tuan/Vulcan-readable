package me.frep.vulcan.spigot.check.impl.player.invalid;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Invalid", type = 'E', complexType = "X/Z", description = "Too large X/Z-Axis movement.")
public class InvalidE extends AbstractCheck {
    public InvalidE(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event) || this.isOnGround()) {
            return;
        }
        double deltaXZ = this.playerData.getBlockBreakTracker().getDeltaXZ();
        if (deltaXZ <= 3.5 || deltaXZ >= 100.0) {
            return;
        }
        if (this.isExempt(
                ExemptType.TELEPORT, ExemptType.RIPTIDE, ExemptType.GLIDING, ExemptType.VELOCITY, ExemptType.JOINED,
                ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.VEHICLE, ExemptType.WORLD_CHANGE, ExemptType.DOLPHINS_GRACE,
                ExemptType.SOUL_SPEED, ExemptType.CHORUS_FRUIT, ExemptType.SLEEPING, ExemptType.LEVITATION, ExemptType.ENDER_PEARL,
                ExemptType.ENTITY_CRAM_FIX, ExemptType.HIGH_SPEED, ExemptType.SHULKER_BOX, ExemptType.SHULKER, ExemptType.CHUNK)) {
            return;
        }
        this.fail("deltaXZ=" + deltaXZ);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
