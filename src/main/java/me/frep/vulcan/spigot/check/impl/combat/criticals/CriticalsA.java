package me.frep.vulcan.spigot.check.impl.combat.criticals;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Criticals", type = 'A', complexType = "Ground", description = "Tried to hit Critical hit on ground.")
public class CriticalsA extends AbstractCheck {
    public CriticalsA(PlayerData playerData) {
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
        double deltaY = Math.abs(this.playerData.getBlockBreakTracker().getDeltaY());
        double modulo = this.playerData.getBlockBreakTracker().getLocationY() % 0.015625;
        if (this.isExempt(
                ExemptType.LIQUID, ExemptType.TRAPDOOR, ExemptType.COLLIDING_VERTICALLY, ExemptType.TELEPORT,
                ExemptType.BOAT, ExemptType.WEB, ExemptType.ENDER_PEARL, ExemptType.CHORUS_FRUIT, ExemptType.ELYTRA,
                ExemptType.SERVER_POSITION, ExemptType.CARPET, ExemptType.VEHICLE, ExemptType.FARMLAND,
                ExemptType.GLIDING, ExemptType.SLAB)) {
            return;
        }
        if (!this.playerData.getBlockBreakTracker().isClientOnGround()) {
            return;
        }
        if (deltaY > 0.0625 && modulo != 0.0) {
            return;
        }
        this.fail("deltaY=" + deltaY + "modulo=" + modulo);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
