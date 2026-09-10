package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = '8', complexType = "Nuker", description = "Invalid Block Dig packets.")
public class BadPackets8 extends AbstractCheck {
    private int digsThisTick;
    private long lastTickMillis;

    public BadPackets8(PlayerData playerData) {
        super(playerData);
        this.digsThisTick = 0;
        this.lastTickMillis = 0L;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isDiggingPacket(event)) {
            if (this.isFlyingPacket(event)) {
                this.digsThisTick = 0;
            }
            return;
        }
        long now = System.currentTimeMillis();
        if (now - this.lastTickMillis > 50L) {
            this.digsThisTick = 0;
            this.lastTickMillis = now;
        }
        ++this.digsThisTick;
        if (this.digsThisTick < 3) {
            return;
        }
        this.fail("digs=" + this.digsThisTick);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
