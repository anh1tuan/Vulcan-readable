package me.frep.vulcan.spigot.check.impl.player.improbable;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Improbable", type = 'A', complexType = "Combat", description = "Too many combined combat violations.")
public class ImprobableA extends AbstractCheck {
    private long lastFlagTime;

    public ImprobableA(PlayerData playerData) {
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
        if (System.currentTimeMillis() - this.lastFlagTime < 60000L) {
            return;
        }
        int combatViolations = this.playerData.getCombatViolations();
        if (combatViolations > ConfigValues.checksPlayerImprobableAMaxCombatViolations) {
            this.fail("vl=" + combatViolations);
            this.lastFlagTime = System.currentTimeMillis();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
