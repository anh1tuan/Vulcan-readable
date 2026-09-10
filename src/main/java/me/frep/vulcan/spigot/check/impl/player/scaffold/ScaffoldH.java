package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'H', complexType = "Rotations [3]", experimental = false, description = "Invalid rotations.")
public class ScaffoldH extends AbstractCheck {
    private long lastPlaceMillis;
    private float lastPitch;

    public ScaffoldH(PlayerData playerData) {
        super(playerData);
        this.lastPlaceMillis = 0L;
        this.lastPitch = 0.0f;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isRotationPacket(event)) {
            return;
        }
        if (this.isExempt(ExemptType.CINEMATIC)) {
            return;
        }
        float pitch = this.playerData.getActionTracker().getPitch();
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        this.lastPitch = pitch;
        if (System.currentTimeMillis() - this.lastPlaceMillis > 400L) {
            return;
        }
        if (Math.abs(pitchDelta) < 0.5f) {
            return;
        }
        this.fail("final=" + pitch + " dp=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (event instanceof BlockPlaceEvent) {
            this.lastPlaceMillis = System.currentTimeMillis();
        }
    }
}
