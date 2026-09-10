package me.frep.vulcan.spigot.check.impl.movement.elytra;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Elytra", type = 'F', complexType = "Packet", description = "Sending Elytra packets too quickly.")
public class ElytraF extends AbstractCheck {
    private long lastElytraStartTime;

    public ElytraF(PlayerData playerData) {
        super(playerData);
        this.lastElytraStartTime = 0L;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isEntityActionPacket(event)) {
            return;
        }
        WrapperPlayClientEntityAction action = this.playerData.getLastEntityActionPacket();
        if (action == null || action.getAction() != WrapperPlayClientEntityAction.Action.START_FLYING_WITH_ELYTRA) {
            return;
        }
        long delay = System.currentTimeMillis() - this.lastElytraStartTime;
        this.lastElytraStartTime = System.currentTimeMillis();
        if (delay >= 110L) {
            this.applyBufferDecay();
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("delay=" + delay);
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
