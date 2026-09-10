package me.frep.vulcan.spigot.check.impl.combat.autoblock;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Auto Block", type = 'A', complexType = "Sequence", description = "Attacked while sending BlockPlace packet.")
public class AutoBlockA extends AbstractCheck {
    public AutoBlockA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isInteractEntityPacket(event)) {
            return;
        }
        WrapperPlayClientInteractEntity interact = this.playerData.getLastInteractPacket();
        if (interact == null || interact.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }
        if (this.isExempt(ExemptType.CLIENT_1_9_OR_NEWER)) {
            return;
        }
        if (this.playerData.getBlockBreakTracker().getTicksSinceBlockPlace() > 1) {
            return;
        }
        this.fail("attack-while-place ticks=" + this.playerData.getBlockBreakTracker().getTicksSinceBlockPlace());
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
