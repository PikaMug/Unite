package me.pikamug.unite.api.events.paf;

import de.simonsator.partyandfriends.api.events.party.LeftPartyEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyLeaveEvent_PAF extends PartyLeaveEvent {
    private final PartyProvider partyProvider;
    private final LeftPartyEvent event;

    public PartyLeaveEvent_PAF(PartyProvider partyProvider, LeftPartyEvent event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = event;
    }

    @Override
    public Event getPluginEvent() {
        return (Event)((Object)event);
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
