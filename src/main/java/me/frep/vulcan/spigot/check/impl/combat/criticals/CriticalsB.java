package me.frep.vulcan.spigot.check.impl.combat.criticals;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Criticals", type = 'B', complexType = "Modulo", description = "Tried to hit Critical hit on ground.")
public class CriticalsB extends AbstractCheck {
    public CriticalsB(PlayerData playerData) {
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
        if (this.getTicksSinceAttack() >= 2) {
            return;
        }
        double modulo = this.playerData.getBlockBreakTracker().getLocationY() % 0.015625;
        if (this.isExempt(
                ExemptType.LIQUID, ExemptType.TRAPDOOR, ExemptType.COLLIDING_VERTICALLY, ExemptType.TELEPORT,
                ExemptType.BOAT, ExemptType.WEB, ExemptType.ENDER_PEARL, ExemptType.CHORUS_FRUIT, ExemptType.ELYTRA,
                ExemptType.SERVER_POSITION, ExemptType.CARPET, ExemptType.VEHICLE, ExemptType.FARMLAND,
                ExemptType.GLIDING, ExemptType.SLAB)) {
            return;
        }
        if (Math.abs(modulo) >= 1.0E-5) {
            return;
        }
        this.fail("deltaY=" + modulo);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
