package me.pikamug.unite.api.events.simpleclans;

import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerKickedClanEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyLeaveEvent_SimpleClans extends PartyLeaveEvent {
    private final PartyProvider partyProvider;
    private final PlayerKickedClanEvent event;

    public PartyLeaveEvent_SimpleClans(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (PlayerKickedClanEvent) event;
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
