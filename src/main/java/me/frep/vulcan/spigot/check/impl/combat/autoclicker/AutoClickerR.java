package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.SampleDeque;
import me.frep.vulcan.spigot.TrigUtil;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'R', complexType = "Consistency", description = "Impossible consistency.")
public class AutoClickerR extends AbstractCheck {
    private final SampleDeque clickIntervals = new SampleDeque(30);

    public AutoClickerR(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isAnimationPacket(event) || this.isExempt(ExemptType.AUTOCLICKER)) {
            return;
        }
        long interval = this.playerData.getClickTracker().getLastClickInterval();
        if (interval > 5000L) {
            this.clickIntervals.clear();
            return;
        }
        this.clickIntervals.add(Long.valueOf(interval));
        if (!this.clickIntervals.isFull()) {
            return;
        }
        int outliers = 0;
        for (Object sample : this.clickIntervals) {
            if (((Long) sample).longValue() > 150L) {
                ++outliers;
            }
        }
        double kurtosis = TrigUtil.kurtosis(this.clickIntervals);
        if (outliers == 0 && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("outliers=" + outliers + " kurtosis=" + kurtosis);
        } else {
            this.applyBufferDecay();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
