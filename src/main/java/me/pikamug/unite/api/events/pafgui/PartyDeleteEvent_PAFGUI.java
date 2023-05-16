package me.pikamug.unite.api.events.pafgui;

import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyDeleteEvent_PAFGUI extends PartyDeleteEvent {
    private final PartyProvider partyProvider;
    private final UUID partyPlayer;

    public PartyDeleteEvent_PAFGUI(PartyProvider partyProvider, UUID partyPlayer, boolean async) {
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
    public UUID getDisbander() {
        return partyPlayer;
    }
}
