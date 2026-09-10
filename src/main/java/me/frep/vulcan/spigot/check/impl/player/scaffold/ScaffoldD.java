package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Scaffold", type = 'D', complexType = "Rotations", experimental = false, description = "Invalid rotations.")
public class ScaffoldD extends AbstractCheck {
    private long lastPlaceMillis;

    public ScaffoldD(PlayerData playerData) {
        super(playerData);
        this.lastPlaceMillis = 0L;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isRotationPacket(event) && !this.isFlyingPacket(event)) {
            return;
        }
        if (this.isExempt(ExemptType.CINEMATIC)) {
            return;
        }
        long sincePlace = System.currentTimeMillis() - this.lastPlaceMillis;
        if (sincePlace > 500L) {
            return;
        }
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        if (Math.abs(pitchDelta) < 15.0f) {
            return;
        }
        this.fail("rotation=" + sincePlace / 100L + " dp=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (event instanceof org.bukkit.event.block.BlockPlaceEvent) {
            this.lastPlaceMillis = System.currentTimeMillis();
        }
    }
}
