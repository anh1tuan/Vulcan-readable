package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = 'B', complexType = "Sequence", description = "More than 20 flying packets in a row.")
public class BadPacketsB extends AbstractCheck {
    private int consecutivePositionFlying;

    public BadPacketsB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            if (this.isSteerVehiclePacket(event)) {
                this.consecutivePositionFlying = 0;
            }
            return;
        }
        WrapperPlayClientPlayerFlying flying = this.playerData.getLastFlyingPacket();
        Player player = this.playerData.getPlayer();
        if (flying == null || !flying.hasPositionChanged() || player == null || player.isInsideVehicle()) {
            this.consecutivePositionFlying = 0;
            return;
        }
        ++this.consecutivePositionFlying;
        if (this.consecutivePositionFlying > 20
            && !this.isExempt(ExemptType.MODERN_SERVER, ExemptType.CLIENT_1_9_OR_NEWER)) {
            this.fail("count=" + this.consecutivePositionFlying);
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
