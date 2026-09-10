package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'H', complexType = "Average Deviation", description = "Similar deviation values.")
public class AutoClickerH extends AbstractCheck {
    private final Deque<Double> deviations = new ArrayDeque<Double>();
    private double lastDeviation;

    public AutoClickerH(PlayerData playerData) {
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
        this.deviations.addLast(Double.valueOf((double) interval));
        if (this.deviations.size() > 20) {
            this.deviations.removeFirst();
        }
        if (this.deviations.size() < 20) {
            return;
        }
        double mean = 0.0;
        for (Double sample : this.deviations) {
            mean += sample.doubleValue();
        }
        mean /= this.deviations.size();
        double deviation = 0.0;
        for (Double sample : this.deviations) {
            deviation += Math.abs(sample.doubleValue() - mean);
        }
        deviation /= this.deviations.size();
        double difference = Math.abs(deviation - this.lastDeviation);
        this.lastDeviation = deviation;
        if (difference > 0.05) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("deviation=" + deviation + " diff=" + difference);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
