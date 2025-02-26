package me.pikamug.unite.api.events.betterteams;

import com.booksaw.betterTeams.customEvents.PlayerJoinTeamEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyJoinEvent_BetterTeams extends PartyJoinEvent {
    private final PartyProvider partyProvider;
    private final PlayerJoinTeamEvent event;

    public PartyJoinEvent_BetterTeams(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (PlayerJoinTeamEvent) event;
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
    public UUID getPlayer() {
        return event.getPlayer().getUniqueId();
    }
}
