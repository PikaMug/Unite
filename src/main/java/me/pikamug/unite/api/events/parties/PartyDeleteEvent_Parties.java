package me.pikamug.unite.api.events.parties;

import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostDeleteEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PartyDeleteEvent_Parties extends PartyDeleteEvent {
    private final PartyProvider partyProvider;
    private final BukkitPartiesPartyPostDeleteEvent event;

    public PartyDeleteEvent_Parties(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (BukkitPartiesPartyPostDeleteEvent) event;
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
        return Objects.requireNonNull(event.getCommandSender()).getPlayerUUID();
    }
}
