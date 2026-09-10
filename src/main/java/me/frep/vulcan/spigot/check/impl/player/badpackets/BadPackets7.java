package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Bad Packets", type = '7', complexType = "No Swing", description = "Hitting without swinging.")
public class BadPackets7 extends AbstractCheck {
    private final boolean modernClient;
    private boolean swung;
    private boolean lastSwung;
    private boolean attacked;

    public BadPackets7(PlayerData playerData) {
        super(playerData);
        this.modernClient = this.playerData.getClientVersion().isNewerThan(ClientVersion.V_1_8);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
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
        if (this.attacked && this.modernClient && !this.lastSwung && !this.swung) {
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
