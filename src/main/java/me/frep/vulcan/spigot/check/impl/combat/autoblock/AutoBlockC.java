package me.frep.vulcan.spigot.check.impl.combat.autoblock;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;


@CheckInfo(name="Auto Block", type='C', complexType="Order", description="Attacked while sending BlockDig packet.")
public class AutoBlockC
extends AbstractCheck {
    public AutoBlockC(PlayerData playerData) {
        super(playerData);
    }
    @Override
    public void handle(PacketEvent event, long timestamp) {
        WrapperPlayClientInteractEntity interact = this.playerData.getLastInteractPacket();
        if (interact == null || interact.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;
        
        this.applyBufferDecay();

    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }

}
