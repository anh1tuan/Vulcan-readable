package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = '9', complexType = "No Swing", description = "Hitting without swinging.")
public class BadPackets9 extends AbstractCheck {
    private boolean swung;
    private boolean lastSwung;
    private boolean attacked;

    public BadPackets9(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.playerData.getClientVersion().isOlderThan(ClientVersion.V_1_21_2)) {
            return;
        }
        if (this.isExempt(ExemptType.SPECTATOR)) {
            return;
        }
        if (this.isAnimationPacket(event)) {
            this.swung = true;
            return;
        }
        if (this.isInteractEntityPacket(event)) {
            WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
            if (packet != null && packet.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                this.attacked = true;
            }
            return;
        }
        if (!this.isFlyingPacket(event)) {
            return;
        }
        if (this.attacked && !this.lastSwung && !this.swung) {
            this.fail("swung=" + this.swung);
        }
        this.lastSwung = this.swung;
        this.swung = false;
        this.attacked = false;
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
