package me.pikamug.unite.api.events.mcmmo;

import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyJoinEvent_mcMMO extends PartyJoinEvent {
    private final PartyProvider partyProvider;
    private final McMMOPartyChangeEvent event;

    public PartyJoinEvent_mcMMO(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (McMMOPartyChangeEvent) event;
    }

    @Override
    public @NotNull Event getPluginEvent() {
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
