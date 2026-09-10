package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'O', complexType = "Spikes", description = "Impossible spike in CPS.")
public class AutoClickerO extends AbstractCheck {
    private double lastCps = -1.0;

    public AutoClickerO(PlayerData playerData) {
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
        double cps = this.playerData.getClickTracker().getCps();
        double spike = this.lastCps < 0.0 ? 0.0 : Math.abs(cps - this.lastCps);
        this.lastCps = cps;
        if (spike < 8.0) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("cps=" + cps + " spike=" + spike);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
