package me.pikamug.unite.api.events.mcmmo;

import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyDeleteEvent_mcMMO extends PartyDeleteEvent {
    private final PartyProvider partyProvider;
    private final McMMOPartyChangeEvent event;

    public PartyDeleteEvent_mcMMO(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (McMMOPartyChangeEvent) event;
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
    public UUID getDisbander() {
        return event.getPlayer().getUniqueId();
    }
}
