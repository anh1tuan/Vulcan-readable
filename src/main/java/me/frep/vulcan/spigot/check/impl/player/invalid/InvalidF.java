package me.frep.vulcan.spigot.check.impl.player.invalid;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Invalid", type = 'F', complexType = "Spoofed Y", description = "Impossible Y-axis change.")
public class InvalidF extends AbstractCheck {
    public InvalidF(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event) || this.isOnGround()) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        if (Math.abs(deltaY) <= 100.0) {
            return;
        }
        if (this.isExempt(ExemptType.DEATH, ExemptType.JOINED, ExemptType.TELEPORT, ExemptType.WORLD_CHANGE)) {
            return;
        }
        this.fail("deltaY=" + deltaY);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
