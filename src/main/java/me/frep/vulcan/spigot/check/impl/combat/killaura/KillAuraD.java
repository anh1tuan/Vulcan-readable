package me.frep.vulcan.spigot.check.impl.combat.killaura;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

@CheckInfo(name = "Kill Aura", type = 'D', complexType = "Multi Aura", experimental = false, description = "Attacked two entities at once.")
public class KillAuraD extends AbstractCheck {
    private int lastEntityId = -1;
    private int flyingSinceSwitch;

    public KillAuraD(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isFlyingPacket(event)) {
            ++this.flyingSinceSwitch;
            return;
        }
        if (!this.isInteractEntityPacket(event)) {
            return;
        }
        WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
        if (packet == null || packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }
        Entity target = this.playerData.getReachEntityHelper().getTargetEntity();
        int entityId = target == null ? -1 : target.getEntityId();
        if (this.lastEntityId != -1 && entityId != this.lastEntityId && this.flyingSinceSwitch <= 1) {
            this.fail("id=" + entityId + "lastId=" + this.lastEntityId);
            this.applyBufferDecay();
        }
        if (entityId != this.lastEntityId) {
            this.flyingSinceSwitch = 0;
        }
        this.lastEntityId = entityId;
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
