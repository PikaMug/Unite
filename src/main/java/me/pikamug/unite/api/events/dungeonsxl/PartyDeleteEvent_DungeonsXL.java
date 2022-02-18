package me.pikamug.unite.api.events.dungeonsxl;

import de.erethon.dungeonsxl.api.event.group.GroupDisbandEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PartyDeleteEvent_DungeonsXL extends PartyDeleteEvent {
    private final PartyProvider partyProvider;
    private final GroupDisbandEvent event;

    public PartyDeleteEvent_DungeonsXL(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (GroupDisbandEvent) event;
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
    public UUID getDisbander() {
        return Objects.requireNonNull(event.getDisbander()).getUniqueId();
    }
}
