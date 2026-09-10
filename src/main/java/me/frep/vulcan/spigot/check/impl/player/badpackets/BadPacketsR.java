package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'R', complexType = "Post HeldItemSlot", experimental = false, description = "Post HeldItemSlot packets.")
public class BadPacketsR extends AbstractCheck {
    private long lastPacketTime;
    private boolean posted;

    public BadPacketsR(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isHeldItemChangePacket(event)) {
            this.lastPacketTime = System.currentTimeMillis();
            this.posted = false;
            return;
        }
        if (!this.isFlyingPacket(event)) {
            return;
        }
        long delay = System.currentTimeMillis() - this.lastPacketTime;
        if (this.posted && delay < 40L && this.increaseBuffer(1.0) > this.maxBufferConfig) {
            this.fail("post=" + delay);
        } else if (delay < 5L) {
            this.posted = true;
        } else {
            this.applyBufferDecay();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
