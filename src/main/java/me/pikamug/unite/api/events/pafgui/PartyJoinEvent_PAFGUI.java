package me.pikamug.unite.api.events.pafgui;

import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyJoinEvent_PAFGUI extends me.pikamug.unite.api.events.PartyJoinEvent {
    private final PartyProvider partyProvider;
    private final UUID partyPlayer;

    public PartyJoinEvent_PAFGUI(PartyProvider partyProvider, UUID partyPlayer, boolean async) {
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
    public UUID getPlayer() {
        return partyPlayer;
    }
}
