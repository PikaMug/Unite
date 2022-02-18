package me.pikamug.unite.api.events.mcmmo;

import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyLeaveEvent_mcMMO extends PartyLeaveEvent {
    private final PartyProvider partyProvider;
    private final McMMOPartyChangeEvent event;

    public PartyLeaveEvent_mcMMO(PartyProvider partyProvider, Event event, boolean async) {
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
