package me.frep.vulcan.spigot.check.impl.player.fastplace;

import com.github.retrooper.packetevents.event.PacketEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

@CheckInfo(name = "Fast Place", type = 'A', complexType = "Delay", description = "Placing blocks too quickly.")
public class FastPlaceA extends AbstractCheck {
    private final Deque<Long> placeTimes = new ArrayDeque<Long>();

    public FastPlaceA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (!(event instanceof BlockPlaceEvent)) {
            return;
        }
        BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
        if (this.isExempt(ExemptType.CANCELLED_BREAK, ExemptType.VEHICLE)) {
            return;
        }
        if (placeEvent.isCancelled()) {
            return;
        }
        if (this.isLilyPad(this.playerData.getPlayer().getItemInHand())) {
            return;
        }
        long now = System.currentTimeMillis();
        this.placeTimes.addLast(now);
        while (!this.placeTimes.isEmpty() && now - this.placeTimes.peekFirst() > 1000L) {
            this.placeTimes.removeFirst();
        }
        int maxBlocks = ConfigValues.checksPlayerFastplaceAMaxBlocks;
        if (this.placeTimes.size() <= maxBlocks) {
            return;
        }
        this.fail("placed=" + this.placeTimes.size() + " max=" + maxBlocks);
        this.applyBufferDecay();
    }

    private boolean isLilyPad(ItemStack item) {
        if (item == null) {
            return false;
        }
        Material type = item.getType();
        String name = type.name();
        return name.contains("LILY") || name.contains("WATER_LILY");
    }
}
