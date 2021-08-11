package me.pikamug.unite.api.events.parties;

import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostCreateEvent;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PartyCreateEvent_Parties extends PartyCreateEvent {
    private final PartyProvider partyProvider;
    private final BukkitPartiesPartyPostCreateEvent event;

    public PartyCreateEvent_Parties(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (BukkitPartiesPartyPostCreateEvent) event;
    }

    @Override
    public @NotNull Event getPluginEvent() {
        return event;
    }

    @Override
    public PartyProvider getPartyProvider() {
        return partyProvider;
    }

    @Override
    public UUID getCreator() {
        return Objects.requireNonNull(event.getCreator()).getPlayerUUID();
    }
}
