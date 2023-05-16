package me.pikamug.unite.api.events.simpleclans;

import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import net.sacredlabyrinth.phaed.simpleclans.events.PreCreateClanEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PartyCreateEvent_SimpleClans extends PartyCreateEvent {
    private final PartyProvider partyProvider;
    private final PreCreateClanEvent event;

    public PartyCreateEvent_SimpleClans(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (PreCreateClanEvent) event;
    }

    @Override
    public Event getPluginEvent() {
        return event;
    }

    @Override
    public @NotNull PartyProvider getPartyProvider() {
        return partyProvider;
    }

    @Override
    public UUID getCreator() {
        return Objects.requireNonNull(event.getPlayer()).getUniqueId();
    }
}
