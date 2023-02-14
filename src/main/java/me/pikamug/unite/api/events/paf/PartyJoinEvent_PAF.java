package me.pikamug.unite.api.events.paf;

import de.simonsator.partyandfriends.api.events.party.PartyJoinEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyJoinEvent_PAF extends me.pikamug.unite.api.events.PartyJoinEvent {
    private final PartyProvider partyProvider;
    private final PartyJoinEvent event;

    public PartyJoinEvent_PAF(PartyProvider partyProvider, PartyJoinEvent event, boolean async) {
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
    public UUID getPlayer() {
        return event.getPlayer().getUniqueId();
    }
}
