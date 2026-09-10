package me.frep.vulcan.spigot.check.impl.combat.killaura;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Kill Aura", type = 'K', complexType = "Pattern", description = "Attack pattern.")
public class KillAuraK extends AbstractCheck {
    private final List attackIntervals = Lists.newArrayList();
    private long lastAttackTime;

    public KillAuraK(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isInteractEntityPacket(event)) {
            return;
        }
        WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
        if (packet == null || packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }
        if (this.isExempt(ExemptType.FAST)) {
            return;
        }
        long now = System.currentTimeMillis();
        if (this.lastAttackTime != 0L) {
            this.attackIntervals.add(Long.valueOf(now - this.lastAttackTime));
        }
        this.lastAttackTime = now;
        if (this.attackIntervals.size() != 10) {
            return;
        }
        Collections.sort(this.attackIntervals);
        long range = ((Long) this.attackIntervals.get(this.attackIntervals.size() - 1)).longValue()
            - ((Long) this.attackIntervals.get(0)).longValue();
        if (range < 50L && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("range=" + range);
        } else {
            this.applyBufferDecay();
        }
        this.attackIntervals.clear();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
