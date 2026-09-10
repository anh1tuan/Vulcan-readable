package me.frep.vulcan.spigot.check.impl.combat.killaura;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Kill Aura", type = 'A', complexType = "Post", description = "Post UseEntity packets.")
public class KillAuraA extends AbstractCheck {
    private boolean flyingBeforeAttack;

    public KillAuraA(PlayerData playerData) {
        super(playerData);
        this.flyingBeforeAttack = false;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isFlyingPacket(event)) {
            this.flyingBeforeAttack = true;
            return;
        }
        if (!this.isInteractEntityPacket(event)) {
            return;
        }
        WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
        if (packet == null || packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }
        boolean post = this.flyingBeforeAttack;
        this.flyingBeforeAttack = false;
        if (!post) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED)) {
            return;
        }
        this.fail("post-attack");
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
