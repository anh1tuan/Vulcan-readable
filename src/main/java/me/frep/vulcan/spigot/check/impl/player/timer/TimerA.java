package me.frep.vulcan.spigot.check.impl.player.timer;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.SampleDeque;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Timer", type = 'A', complexType = "Average", description = "Increased game speed.")
public class TimerA extends AbstractCheck {
    private final SampleDeque flyingIntervals = new SampleDeque(50);
    private long lastFlyingTime;

    public TimerA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        long now = System.currentTimeMillis();
        long interval = now - this.lastFlyingTime;
        this.lastFlyingTime = now;
        if (this.playerData.getStatusTracker().getLevitationAmplifier() != 0) {
            return;
        }
        if (this.isExempt(ExemptType.JOINED, ExemptType.TPS, ExemptType.VEHICLE, ExemptType.WEB)) {
            return;
        }
        if (interval <= 4L) {
            return;
        }
        this.flyingIntervals.add(Long.valueOf(interval));
        if (!this.flyingIntervals.isFull()) {
            return;
        }
        double averageMs = this.flyingIntervals.average();
        if (averageMs <= 0.0) {
            return;
        }
        double speed = 50.0 / averageMs;
        double percent = speed * 100.0;
        if (speed <= ConfigValues.checksPlayerTimerAMaxSpeed) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("speed=" + percent + "diff=" + (speed - ConfigValues.checksPlayerTimerAMaxSpeed));
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
