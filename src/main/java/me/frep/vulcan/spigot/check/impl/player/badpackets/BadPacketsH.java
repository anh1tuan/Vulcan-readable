package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'H', complexType = "Packet Order", description = "Invalid attack packet order.")
public class BadPacketsH extends AbstractCheck {
    private boolean attackWithoutFlying;

    public BadPacketsH(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isInteractEntityPacket(event)) {
            WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
            if (packet != null && packet.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                this.attackWithoutFlying = true;
            }
            return;
        }
        if (this.isFlyingPacket(event)) {
            this.attackWithoutFlying = false;
            return;
        }
        if (this.isAnimationPacket(event) && this.attackWithoutFlying) {
            this.fail("attack-order");
            this.applyBufferDecay();
            this.attackWithoutFlying = false;
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
