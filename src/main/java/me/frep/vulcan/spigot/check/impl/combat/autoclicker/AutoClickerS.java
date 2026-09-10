package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.SampleDeque;
import me.frep.vulcan.spigot.TrigUtil;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'S', complexType = "Distinct", description = "Checks for amount of distinct delays.")
public class AutoClickerS extends AbstractCheck {
    private final SampleDeque clickIntervals = new SampleDeque(25);

    public AutoClickerS(PlayerData playerData) {
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
        int distinct = TrigUtil.distinctCount(this.clickIntervals);
        if (distinct < 6 && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("distinct=" + distinct);
        } else {
            this.applyBufferDecay();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
