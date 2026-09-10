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

@CheckInfo(name = "Auto Clicker", type = 'B', complexType = "Deviation", description = "Too low standard deviation.")
public class AutoClickerB extends AbstractCheck {
    private final Deque clickIntervals = Lists.newLinkedList();

    public AutoClickerB(PlayerData playerData) {
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
        double deviation = TrigUtil.standardDeviation(this.clickIntervals);
        double average = TrigUtil.mean(this.clickIntervals);
        if (deviation < 167.0 && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("deviation=" + deviation + " average=" + average);
        } else {
            this.applyBufferDecay();
        }
        this.clickIntervals.clear();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
