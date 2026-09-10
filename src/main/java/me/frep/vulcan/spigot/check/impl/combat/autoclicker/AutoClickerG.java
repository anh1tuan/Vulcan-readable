package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.google.common.collect.Lists;
import java.util.Deque;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.TrigUtil;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'G', complexType = "Outliers", description = "Too low outliers.")
public class AutoClickerG extends AbstractCheck {
    private final Deque clickIntervals = Lists.newLinkedList();

    public AutoClickerG(PlayerData playerData) {
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
        if (this.clickIntervals.size() != 50) {
            return;
        }
        int outliers = 0;
        for (Object sample : this.clickIntervals) {
            if (((Long) sample).longValue() > 150L) {
                ++outliers;
            }
        }
        double kurtosis = TrigUtil.kurtosis(this.clickIntervals);
        if (outliers < 3 && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("outliers=" + outliers + " kurtosis=" + kurtosis);
        } else {
            this.applyBufferDecay();
        }
        this.clickIntervals.clear();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
