package me.pikamug.unite.api.events.pafgui;

import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyCreateEvent_PAFGUI extends PartyCreateEvent {
    private final PartyProvider partyProvider;
    private final UUID partyPlayer;

    public PartyCreateEvent_PAFGUI(PartyProvider partyProvider, UUID partyPlayer, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.partyPlayer = partyPlayer;
    }

    @Override
    public Event getPluginEvent() {
        return null;
    }

    @Override
    public @NotNull PartyProvider getPartyProvider() {
        return partyProvider;
    }

    @Override
    public UUID getCreator() {
        return partyPlayer;
    }
}
