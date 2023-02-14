package me.pikamug.unite.api.events.paf;

import de.simonsator.partyandfriends.api.events.party.LeftPartyEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyDeleteEvent_PAF extends PartyDeleteEvent {
    private final PartyProvider partyProvider;
    private final LeftPartyEvent event;

    public PartyDeleteEvent_PAF(PartyProvider partyProvider, LeftPartyEvent event, boolean async) {
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
    public UUID getDisbander() {
        return event.getPlayer().getUniqueId();
    }
}
