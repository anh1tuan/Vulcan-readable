package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'P', complexType = "Identical", description = "Identical statistical values.")
public class AutoClickerP extends AbstractCheck {
    private long lastInterval;

    public AutoClickerP(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isBlockPlacePacket(event) || !this.isAnimationPacket(event)) {
            return;
        }
        long interval = this.playerData.getClickTracker().getLastClickInterval();
        if (interval <= 0L || interval >= 5000L) {
            return;
        }
        boolean identical = this.lastInterval != 0L && interval == this.lastInterval;
        this.lastInterval = interval;
        if (!identical) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("interval=" + interval);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
