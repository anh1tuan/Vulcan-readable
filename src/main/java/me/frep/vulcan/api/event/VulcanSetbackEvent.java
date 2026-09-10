package me.frep.vulcan.api.event;

import me.frep.vulcan.api.check.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanSetbackEvent
extends Event
implements Cancellable {
    private final Player player;
    private boolean cancelled;
    private final Check check;
    private final long timestamp = System.currentTimeMillis();
    private static final HandlerList handlers = new HandlerList();

    public VulcanSetbackEvent(Player player, Check check) {
        super(true);
        this.player = player;
        this.check = check;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Check getCheck() {
        return this.check;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}

