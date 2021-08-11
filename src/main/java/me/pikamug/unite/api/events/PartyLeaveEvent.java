package me.pikamug.unite.api.events;

import me.pikamug.unite.api.interfaces.PartyEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class PartyLeaveEvent extends Event implements PartyEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public PartyLeaveEvent(boolean async) {
        super(async);
    }

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public abstract UUID getPlayer();
}
