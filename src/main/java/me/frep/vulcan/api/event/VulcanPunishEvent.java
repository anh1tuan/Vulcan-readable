package me.frep.vulcan.api.event;

import me.frep.vulcan.api.check.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanPunishEvent
extends Event
implements Cancellable {
    private boolean cancelled;
    private final Player player;
    private final Check check;
    private static final HandlerList handlers = new HandlerList();

    public VulcanPunishEvent(Player player, Check check) {
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
}

