package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = '6', complexType = "Nuker", description = "Invalid Block Dig packets.")
public class BadPackets6 extends AbstractCheck {
    private int startDigCount;

    public BadPackets6(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isDiggingPacket(event)) {
            WrapperPlayClientPlayerDigging digging = this.playerData.getLastDigPacket();
            if (digging != null && digging.getAction() == DiggingAction.START_DIGGING) {
                ++this.startDigCount;
                if (this.startDigCount > 3) {
                    if (this.increaseBuffer(1.0) > this.maxBufferConfig) {
                        this.fail("count=" + this.startDigCount);
                    }
                } else {
                    this.applyBufferDecay();
                }
            }
            return;
        }
        if (this.isAnimationPacket(event)) {
            this.startDigCount = 0;
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
