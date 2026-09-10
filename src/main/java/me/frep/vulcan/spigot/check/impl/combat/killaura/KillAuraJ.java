package me.frep.vulcan.spigot.check.impl.combat.killaura;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Kill Aura", type = 'J', complexType = "Frequency", experimental = false, description = "Attack frequency.")
public class KillAuraJ extends AbstractCheck {
    private int flyingSinceAttack;
    private int lastFlyingSinceAttack;
    private int streak;

    public KillAuraJ(PlayerData playerData) {
        super(playerData);
        this.flyingSinceAttack = 0;
        this.lastFlyingSinceAttack = 0;
        this.streak = 0;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (this.isInteractEntityPacket(event)) {
            WrapperPlayClientInteractEntity packet = this.playerData.getLastInteractPacket();
            if (packet == null || packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                return;
            }
            if (this.playerData.getClickTracker().getCps() > 7.2
                    && this.flyingSinceAttack < 4
                    && this.lastFlyingSinceAttack < 4) {
                ++this.streak;
            } else {
                this.streak = 0;
            }
            this.lastFlyingSinceAttack = this.flyingSinceAttack;
            this.flyingSinceAttack = 0;
            if (this.streak > 4 && this.getBuffer() > this.maxBufferConfig) {
                this.fail("cps=" + this.playerData.getClickTracker().getCps() + " streak=" + this.streak);
                this.applyBufferDecay();
            }
            return;
        }
        if (this.isFlyingPacket(event)) {
            ++this.flyingSinceAttack;
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
