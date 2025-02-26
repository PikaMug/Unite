package me.pikamug.unite.api.events.betterteams;

import com.booksaw.betterTeams.customEvents.DisbandTeamEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PartyDeleteEvent_BetterTeams extends PartyDeleteEvent {
    private final PartyProvider partyProvider;
    private final DisbandTeamEvent event;

    public PartyDeleteEvent_BetterTeams(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (DisbandTeamEvent) event;
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
    public UUID getDisbander() {
        return Objects.requireNonNull(event.getPlayer()).getUniqueId();
    }
}
