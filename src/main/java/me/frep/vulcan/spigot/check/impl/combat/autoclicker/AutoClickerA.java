package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.google.common.collect.Lists;
import java.util.Deque;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.TrigUtil;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Clicker", type = 'A', complexType = "Limit", description = "Left clicking too quickly.")
public class AutoClickerA extends AbstractCheck {
    private final Deque clickIntervals = Lists.newLinkedList();
    private long lastClickMillis;

    public AutoClickerA(PlayerData playerData) {
        super(playerData);
        this.lastClickMillis = 0L;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isAnimationPacket(event) || this.isExempt(ExemptType.AUTOCLICKER_NON_DIG)) {
            return;
        }
        long now = System.currentTimeMillis();
        this.clickIntervals.add(Long.valueOf(now - this.lastClickMillis));
        this.lastClickMillis = now;
        if (this.clickIntervals.size() != 20) {
            return;
        }
        double cps = TrigUtil.clicksPerSecond(this.clickIntervals);
        if (cps > (double) ConfigValues.checksCombatAutoclickerAMaxCps && cps > 5.0) {
            this.fail("cps=" + cps);
        }
        this.clickIntervals.clear();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
