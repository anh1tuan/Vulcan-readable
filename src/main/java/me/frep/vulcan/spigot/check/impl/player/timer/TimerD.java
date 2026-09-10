package me.frep.vulcan.spigot.check.impl.player.timer;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Timer", type = 'D', complexType = "Balance", description = "Increased game speed.")
public class TimerD extends AbstractCheck {
    private long lastFlyingTime;
    private long balance;
    private int flyingCount;

    public TimerD(PlayerData playerData) {
        super(playerData);
        this.lastFlyingTime = 0L;
        this.balance = 0L;
        this.flyingCount = 0;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (this.isExempt(ExemptType.JOINED, ExemptType.TPS, ExemptType.VEHICLE)) {
            return;
        }
        if (this.playerData.getStatusTracker().getLevitationAmplifier() != 0) {
            return;
        }
        if (!this.playerData.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            return;
        }
        long now = System.currentTimeMillis();
        if (this.lastFlyingTime == 0L) {
            this.lastFlyingTime = now;
            return;
        }
        long elapsed = now - this.lastFlyingTime;
        this.lastFlyingTime = now;
        this.balance += 50L;
        this.balance -= elapsed;
        ++this.flyingCount;
        if (this.flyingCount % 400 == 0) {
            this.balance = -200L;
        }
        if (this.balance > 50L && this.getBuffer() > this.maxBufferConfig) {
            this.fail("balance=" + this.balance);
            this.applyBufferDecay();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
