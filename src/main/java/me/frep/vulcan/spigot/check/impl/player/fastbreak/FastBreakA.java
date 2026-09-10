package me.frep.vulcan.spigot.check.impl.player.fastbreak;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

@CheckInfo(name = "Fast Break", type = 'A', complexType = "Delay", description = "Breaking blocks too quickly.")
public class FastBreakA extends AbstractCheck {
    private long lastBreakTime;

    public FastBreakA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (!(event instanceof BlockBreakEvent)) {
            return;
        }
        if (this.playerData.getPlayer() == null) {
            return;
        }
        if (this.playerData.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED)) {
            return;
        }
        BlockBreakEvent breakEvent = (BlockBreakEvent) event;
        long delay = System.currentTimeMillis() - this.lastBreakTime;
        this.lastBreakTime = System.currentTimeMillis();
        if (delay >= 50L) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("delay=" + delay + " type=" + breakEvent.getBlock().getType());
        this.applyBufferDecay();
    }
}
