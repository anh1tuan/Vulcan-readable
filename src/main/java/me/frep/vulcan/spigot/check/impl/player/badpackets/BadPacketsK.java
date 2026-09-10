package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'K', complexType = "Sneak", description = "Sending EntityAction packets too quickly")
public class BadPacketsK extends AbstractCheck {
    private long lastSneakTime = -1L;

    public BadPacketsK(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isEntityActionPacket(event)) {
            return;
        }
        WrapperPlayClientEntityAction packet = this.playerData.getLastEntityActionPacket();
        if (packet == null || packet.getAction() != WrapperPlayClientEntityAction.Action.START_SNEAKING) {
            return;
        }
        long now = System.currentTimeMillis();
        long delay = now - this.lastSneakTime;
        if (delay < 90L && !this.isExempt(ExemptType.FAST)) {
            if (this.increaseBuffer(1.0) > this.maxBufferConfig) {
                this.fail("delay=" + delay);
            }
        } else {
            this.applyBufferDecay();
        }
        this.lastSneakTime = now;
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
