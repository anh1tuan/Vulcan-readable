package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'J', complexType = "Acceleration [2]", experimental = false, description = "Invalid pitch change.")
public class ScaffoldJ extends AbstractCheck {
    private long lastPlaceMillis;
    private float lastPitchDelta;

    public ScaffoldJ(PlayerData playerData) {
        super(playerData);
        this.lastPlaceMillis = 0L;
        this.lastPitchDelta = 0.0f;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event) && !this.isRotationPacket(event)) {
            return;
        }
        if (System.currentTimeMillis() - this.lastPlaceMillis > 400L) {
            return;
        }
        float pitchDelta = this.playerData.getActionTracker().getPitchDelta();
        float acceleration = Math.abs(pitchDelta - this.lastPitchDelta);
        this.lastPitchDelta = pitchDelta;
        if (acceleration < 1.0f) {
            return;
        }
        this.fail("acceleration=" + acceleration + " deltaPitch=" + pitchDelta);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (event instanceof BlockPlaceEvent) {
            this.lastPlaceMillis = System.currentTimeMillis();
        }
    }
}
