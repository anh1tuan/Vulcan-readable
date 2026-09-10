package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'Q', complexType = "Average Deviation", description = "Too low average deviation.")
public class AutoClickerQ extends AbstractCheck {
    private final Deque<Double> samples = new ArrayDeque<Double>();

    public AutoClickerQ(PlayerData playerData) {
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
        this.samples.addLast(Double.valueOf((double) interval));
        if (this.samples.size() > 20) {
            this.samples.removeFirst();
        }
        if (this.samples.size() < 20) {
            return;
        }
        double mean = 0.0;
        for (Double sample : this.samples) {
            mean += sample.doubleValue();
        }
        mean /= this.samples.size();
        double deviation = 0.0;
        for (Double sample : this.samples) {
            deviation += Math.abs(sample.doubleValue() - mean);
        }
        deviation /= this.samples.size();
        if (deviation > 1.5) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deviation=" + deviation);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
