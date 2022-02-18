package me.pikamug.unite.api.events.simpleclans;

import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerJoinedClanEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyJoinEvent_SimpleClans extends PartyJoinEvent {
    private final PartyProvider partyProvider;
    private final PlayerJoinedClanEvent event;

    public PartyJoinEvent_SimpleClans(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (PlayerJoinedClanEvent) event;
    }

    @Override
    public @NotNull Event getPluginEvent() {
        return event;
    }

    @Override
    public @NotNull PartyProvider getPartyProvider() {
        return partyProvider;
    }

    @Override
    public UUID getPlayer() {
        return event.getClanPlayer().getUniqueId();
    }
}
