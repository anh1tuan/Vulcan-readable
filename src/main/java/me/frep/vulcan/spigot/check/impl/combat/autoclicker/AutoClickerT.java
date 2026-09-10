package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.SampleDeque;
import me.frep.vulcan.spigot.TrigUtil;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'T', complexType = "Kurtosis", description = "Too low kurtosis")
public class AutoClickerT extends AbstractCheck {
    private final SampleDeque clickIntervals = new SampleDeque(25);

    public AutoClickerT(PlayerData playerData) {
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
        double kurtosis = TrigUtil.kurtosis(this.clickIntervals) / 1000.0;
        this.playerData.getClickTracker().setKurtosis(kurtosis);
        if (kurtosis < ConfigValues.checksCombatAutoclickerTMinKurtosis
            && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("kurtosis=" + kurtosis);
        } else {
            this.applyBufferDecay();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
