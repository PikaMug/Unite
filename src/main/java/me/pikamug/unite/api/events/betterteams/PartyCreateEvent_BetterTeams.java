package me.pikamug.unite.api.events.betterteams;

import com.booksaw.betterTeams.customEvents.CreateTeamEvent;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PartyCreateEvent_BetterTeams extends PartyCreateEvent {
    private final PartyProvider partyProvider;
    private final CreateTeamEvent event;

    public PartyCreateEvent_BetterTeams(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (CreateTeamEvent) event;
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
