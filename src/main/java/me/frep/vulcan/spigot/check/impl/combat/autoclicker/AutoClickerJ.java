package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'J', complexType = "Range", description = "Impossible consistency.")
public class AutoClickerJ extends AbstractCheck {
    private final List clickIntervals = Lists.newArrayList();

    public AutoClickerJ(PlayerData playerData) {
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
        if (this.clickIntervals.size() != 10) {
            return;
        }
        Collections.sort(this.clickIntervals);
        long range = ((Long) this.clickIntervals.get(this.clickIntervals.size() - 1)).longValue()
            - ((Long) this.clickIntervals.get(0)).longValue();
        if (range < 50L && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("range=" + range);
        } else {
            this.applyBufferDecay();
        }
        this.clickIntervals.clear();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
