package me.frep.vulcan.spigot.check.impl.player.improbable;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Improbable", type = 'D', complexType = "Auto Clicker", description = "Too many combined autoclicker violations.")
public class ImprobableD extends AbstractCheck {
    private long lastFlagTime;

    public ImprobableD(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isAnimationPacket(event)) {
            return;
        }
        if (System.currentTimeMillis() - this.lastFlagTime < 60000L) {
            return;
        }
        int autoClickerViolations = this.playerData.getAutoClickerViolations();
        if (autoClickerViolations > ConfigValues.checksPlayerImprobableDMaxAutoclickerViolations) {
            this.fail("vl=" + autoClickerViolations);
            this.lastFlagTime = System.currentTimeMillis();
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
