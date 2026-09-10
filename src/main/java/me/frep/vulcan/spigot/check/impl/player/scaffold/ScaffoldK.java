package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import java.util.HashMap;
import java.util.Map;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'K', complexType = "Limit", description = "Bridging too quickly.")
public class ScaffoldK extends AbstractCheck {
    private int sneakTicks;
    private final Map<Long, Long> placeTimes;
    private int lastPlaceTick;

    public ScaffoldK(PlayerData playerData) {
        super(playerData);
        this.placeTimes = new HashMap<Long, Long>();
        this.sneakTicks = 0;
        this.lastPlaceTick = 0;
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isEntityActionPacket(event)) {
            return;
        }
        WrapperPlayClientEntityAction action = new WrapperPlayClientEntityAction((PacketReceiveEvent) event);
        if (action.getAction() == WrapperPlayClientEntityAction.Action.START_SNEAKING) {
            this.sneakTicks = 0;
        }
        if (action.getAction() == WrapperPlayClientEntityAction.Action.STOP_SNEAKING) {
            this.sneakTicks = 0;
        }
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (!(event instanceof BlockPlaceEvent)) {
            return;
        }
        BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
        if (placeEvent.isCancelled()) {
            return;
        }
        long now = System.currentTimeMillis();
        this.placeTimes.put(Long.valueOf(now), Long.valueOf(now));
        this.placeTimes.entrySet().removeIf(entry -> now - entry.getKey().longValue() > 1000L);
        int blocks = this.placeTimes.size();
        this.lastPlaceTick = blocks;
        if (blocks < 8) {
            return;
        }
        this.fail("blocks=" + blocks);
        this.applyBufferDecay();
    }
}
