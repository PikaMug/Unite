package me.pikamug.unite.api.events.paf;

import de.simonsator.partyandfriends.api.events.party.PartyCreatedEvent;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyCreateEvent_PAF extends PartyCreateEvent {
    private final PartyProvider partyProvider;
    private final PartyCreatedEvent event;

    public PartyCreateEvent_PAF(PartyProvider partyProvider, PartyCreatedEvent event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = event;
    }

    @Override
    public @NotNull Event getPluginEvent() {
        return (Event)((Object)event);
    }

    @Override
    public @NotNull PartyProvider getPartyProvider() {
        return partyProvider;
    }

    @Override
    public UUID getCreator() {
        return event.getParty().getLeader().getUniqueId();
    }
}
